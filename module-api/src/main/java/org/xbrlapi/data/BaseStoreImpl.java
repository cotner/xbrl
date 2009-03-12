package org.xbrlapi.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ArcroleType;
import org.xbrlapi.Concept;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Instance;
import org.xbrlapi.Item;
import org.xbrlapi.Language;
import org.xbrlapi.Locator;
import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.Resource;
import org.xbrlapi.RoleType;
import org.xbrlapi.SchemaDeclaration;
import org.xbrlapi.Stub;
import org.xbrlapi.Tuple;
import org.xbrlapi.XML;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.resource.DefaultMatcherImpl;
import org.xbrlapi.data.resource.Matcher;
import org.xbrlapi.impl.FragmentComparator;
import org.xbrlapi.impl.StubImpl;
import org.xbrlapi.networks.Analyser;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.NetworksImpl;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.networks.RelationshipImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.utilities.XMLDOMBuilder;


/**
 * Abstract base implementation of the data store
 * providing all methods of the store interface that
 * do not depend on the nature of the underlying data store
 * implementation.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public abstract class BaseStoreImpl implements Store, Serializable {

	protected static Logger logger = Logger.getLogger(BaseStoreImpl.class);
	
    /**
     * The DOM document used to construct DOM representations
     * of subtrees of documents in the store.
     */    
    protected Document storeDOM = null;

    /**
     * Resource matcher
     */
    protected Matcher matcher = new DefaultMatcherImpl();
    
    /**
     * @see org.xbrlapi.data.Store#setMatcher(Matcher)
     */
    public synchronized void setMatcher(Matcher matcher) throws XBRLException {
        if (matcher == null) throw new XBRLException("The matcher cannot be null");
        this.matcher = matcher;
    }

    /**
     * @see org.xbrlapi.data.Store#getMatcher()
     */
    public Matcher getMatcher() {
        return this.matcher;
    }    
    
    /**
     * Namespace bindings
     */
    protected HashMap<String,String> namespaceBindings = new HashMap<String,String>();    

    /**
     * @see org.xbrlapi.data.Store#setNamespaceBinding(String,String)
     */
    public synchronized void setNamespaceBinding(String namespace, String prefix) throws XBRLException {
        this.namespaceBindings.put(namespace,prefix);
    }

    /**
     * List of URIs to use when filtering query results to only get matches
     * to a specific set of documents.
     */
    private List<URI> uris = null;

    /**
     * @see org.xbrlapi.data.Store#setFilteringURIs(List)
     */
    public synchronized void setFilteringURIs(List<URI> uris) {
        this.uris = uris;
    }
    
    /**
     * @see org.xbrlapi.data.Store#getFilteringURIs()
     */
    public List<URI> getFilteringURIs() {
        return this.uris;
    }    
    
    /**
     * @see org.xbrlapi.data.Store#clearFilteringURIs()
     */
    public void clearFilteringURIs() {
        this.uris = null;
    }

    /**
     * @see org.xbrlapi.data.Store#isFilteringByURIs()
     */
    public synchronized boolean isFilteringByURIs() {
        if (this.uris == null) return false;
        return true;
    }
    
    /**
     * @return an X Query clause that restricts the set of fragments returned by 
     * a query to those from a specific set of URIs.
     */
    protected synchronized String getURIFilteringQueryClause() {

        if (isFilteringByURIs()) {
            String uriFilter = "0";
            for (URI uri: this.getFilteringURIs()) {
                uriFilter = uriFilter + " or @uri='" + uri + "'";
            }
            uriFilter = "[" + uriFilter + "]";
            logger.debug(uriFilter);
            return uriFilter;
        }
        return "";
    }
    
	public BaseStoreImpl() {
		super();
	}

	/**
	 * Close the data store.
	 * Throws XBRLException if the data store cannot be closed. 
	 */
	public void close() throws XBRLException {

	}
	
    /**
     * @see org.xbrlapi.data.Store#persistLoaderState(Map)
     */
    public synchronized void persistLoaderState(Map<URI,String> documents) throws XBRLException {
        try {
            for (URI uri: documents.keySet()) {
                persistStub(uri,documents.get(uri));
            }
        } catch (XBRLException e) {
            throw new XBRLException("The loader state could not be stored.",e);
        }
    }

    /**
     * @see org.xbrlapi.data.Store#persistStub(URI,String)
     */
    public void persistStub(URI uri, String reason) throws XBRLException {

        deleteDocument(uri);

        String documentId = getDocumentId(uri);
        Stub stub = new StubImpl(documentId);
        stub.setIndex(documentId);
        stub.setMetaAttribute("uri",uri.toString());
        stub.setMetaAttribute("stub","");
        stub.setMetaAttribute("reason",reason);
        persist(stub);
    }
    
    /**
     * Default implementation does nothing.
     * @see Store#sync()
     */
    public synchronized void sync() throws XBRLException {
        ;
    }

    /**
     * This implementation generates the document ID
     * with a prefix that is a random string of characters 
     * including a-z, A-Z and 0-9.
     * If, by chance, the random string has already been 
     * used for another document in the data store, then 
     * another random string is generated and this repeats until
     * the random string is unique in the data store.
     * @see org.xbrlapi.data.Store#getDocumentId(URI)
     */    
    public String getDocumentId(URI document) throws XBRLException {
        
        // The document is not in the data store so generate a new document ID.
        String randomString = random();
        while (this.hasFragment(randomString + "_1") || this.hasFragment(randomString)) {
            randomString = random();
        }
        return randomString;
    }
    
    /**
     * Generate a random string.
     * @return a randomly generated string consisting of digits and
     * a-z or A-Z only.
     */
    private String random() {
        String random = "";
        for (int i=0; i<6; i++) {
            int code = (new Long(Math.round(Math.random()*61))).intValue();
            code = code + 48;
            if (code < 58) {
                random = random + new Character((char)code).toString();
            } else {
                code = code + 7;
                if (code < 91) {
                    random = random + new Character((char)code).toString();
                } else {
                    code = code + 6;
                    random = random + new Character((char)code).toString();
                }
            }
        }
        return random;
    }
    
    /**
     * @param bs The given byte array.
     * @return a hex string representation of the given byte array.
     */
    private String bytesToHex(byte[] bs) {
        StringBuffer ret = new StringBuffer(bs.length);
        for (int i = 0; i < bs.length; i++) {
            String hex = Integer.toHexString(0x0100 + (bs[i] & 0x00FF)).substring(1);
            ret.append((hex.length() < 2 ? "0" : "") + hex);
        }
        return ret.toString();
    }    

    /**
     * Serialize the specified XML DOM to the specified destination.
     * @param what the root element of the DOM to be serialised.
     * @param destination The destination output stream to be serialised to.
     * @throws XBRLException if the DOM cannot be serialised
     * because the destination cannot be written to or some other
     * different problem occurs during serialisation.
     */
    public void serialize(Element what, OutputStream destination) throws XBRLException {
		try {
			OutputFormat format = new OutputFormat(what.getOwnerDocument(), "UTF-8", true);
			
			// TODO Make sure that BaseStoreImpl serialization uses the latest Xalan code.
			XMLSerializer output = new XMLSerializer(destination, format);
			output.setNamespaces(true);
			output.serialize(what);
		} catch (IOException e) {
			throw new XBRLException("The information could not be serialised.", e);
		}
    }
    
    /**
     * Serialize the specified XML DOM to System.out.
     * @param what the root element of the DOM to be serialised.
     * @throws XBRLException
     */
    public void serialize(Element what) throws XBRLException {
		serialize(what,System.out);
    }
    
    /**
     * Serialize the specified fragment.
     * @param fragment The fragment to be serialised.
     * @throws XBRLException
     */
    public void serialize(XML fragment) throws XBRLException {
        serialize(fragment.getMetadataRootElement(),System.out);
    }    
    
    /**
     * Serialize the specified XML DOM node.
     * @param what the root element of the DOM to be serialised.
     * @return a string containing the serialized XML.
     * @throws XBRLException
     */
    public String serializeToString(Element what) throws XBRLException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serialize(what, baos);
		return baos.toString();
    }
    
    
    /**
	  * @see org.xbrlapi.data.Store#deleteDocument(URI)
     */
    public void deleteDocument(URI uri) throws XBRLException {
        URI matchURI = this.getMatcher().getMatch(uri);
        String query = "/*[@uri='"+ matchURI + "']";
        List<XML> resources = this.<XML>query(query);
        for (XML resource: resources) {
            this.remove(resource.getIndex());
        }
    }
    

    /**
	  * @see org.xbrlapi.data.Store#deleteRelatedDocuments(URI)
	  */
	private static HashMap<URI,Boolean> documentsToDelete = new HashMap<URI,Boolean>(); 
    public void deleteRelatedDocuments(URI uri) throws XBRLException {
    	deleteDocument(uri);
    	List<Fragment> fragments = this.<Fragment>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@targetDocumentURI='"+ uri + "']");
    	for (Fragment fragment: fragments) {
            if (! documentsToDelete.containsKey(fragment.getURI())) {
                documentsToDelete.put(fragment.getURI(),new Boolean(true));
            }
    		Iterator<URI> iterator = documentsToDelete.keySet().iterator();
    		while (iterator.hasNext()) {
    			URI myURI = iterator.next();
    			if (documentsToDelete.get(myURI)) {
        			deleteRelatedDocuments(myURI);
        			documentsToDelete.put(myURI,new Boolean(false));
    			}
    		}
    	}
    }
    
    /**
     * @see org.xbrlapi.data.Store#getReferencingDocuments(URI)
     */
    public List<URI> getReferencingDocuments(URI uri) throws XBRLException {
        String query = "/*[@targetDocumentURI='"+ uri + "']";
        List<Fragment> fragments = this.<Fragment>query(query);

        List<URI> uris = new Vector<URI>();
        HashMap<URI,String> map = new HashMap<URI,String>(); 

        for (Fragment fragment: fragments) {
            URI doc = fragment.getURI();
            if (!map.containsKey(doc)) {
                map.put(doc,"");
                uris.add(doc);
            }
        }
        
        return uris;
    }
    
    /**
     * @see org.xbrlapi.data.Store#getReferencedDocuments(URI)
     */
    public List<URI> getReferencedDocuments(URI uri) throws XBRLException {
        String query = "/*[@uri='" + uri + "' and @targetDocumentURI]";
        List<Fragment> fragments = this.<Fragment>query(query);

        List<URI> uris = new Vector<URI>();
        HashMap<URI,String> map = new HashMap<URI,String>(); 

        for (Fragment fragment: fragments) {
            try {
                URI target = new URI(fragment.getMetaAttribute("targetDocumentURI"));
                if (!map.containsKey(target)) {
                    map.put(target,"");
                    uris.add(target);
                }
            } catch (URISyntaxException e) {
                throw new XBRLException(fragment.getMetaAttribute("targetDocumentURI") + " has an invalid URI syntax.");
            }
        }
        
        return uris;
    }
    

    /**
     * Serialize the specified XML DOM to the specified destination.
     * create the necessary directory if it does not exist.  Use 
     * the file to create a file outputstream.
     * @param what the root element of the DOM to be serialised.
     * @param destination The destination file to be serialised to.
     * @throws XBRLException if the DOM cannot be serialised
     * because the destination cannot be written to or some other
     * different problem occurs during serialisation.
	 */
	public void serialize(Element what, File destination) throws XBRLException {
		
		File parentFile = destination.getParentFile();
		
		if (parentFile != null) parentFile.mkdirs();

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(destination.toString());
			serialize(what, fileOutputStream);
		} catch (FileNotFoundException e) {
			throw new XBRLException("The file to be written to cannot be found.", e);
		}
		
	}
    
    /**
     * Serialize the specified XML DOM to the specified destination file.
     * @param what the root element of the DOM to be serialised.
     * @param destination The destination file to be serialised to.
     * @throws XBRLException if the DOM cannot be serialised
     * because the destination cannot be written to or some other
     * different problem occurs during serialisation.
	 */
	public void serialize(Element what, String destination) throws XBRLException {
		serialize(what, new File(destination));
	}
	
    /**
     * @see Store#getStoredURIs()
     */
    public List<URI> getStoredURIs() throws XBRLException {
    	LinkedList<URI> uris = new LinkedList<URI>();
    	List<Fragment> rootFragments = this.getRootFragments();
    	for (int i=0; i<rootFragments.size(); i++) {
    		uris.add(rootFragments.get(i).getURI());
    	}
    	return uris;
    }

    /**
     * @see org.xbrlapi.data.Store#hasDocument(URI)
     */
    public boolean hasDocument(URI uri) throws XBRLException {
        URI matchURI = getMatcher().getMatch(uri);
        List<Fragment> rootFragments = this.<Fragment>query("/*[@uri='" + matchURI + "' and @parentIndex='none']");
        return (rootFragments.size() > 0) ? true : false;
    }

    
    /**
     * Get a single document in the store as a DOM.  Note that this will
     * not reflect the original document in some ways.  Importantly, 
     * entities will be resolved and document type declarations will be missing.
     * Document encodings may also differ.  If the original document is required,
     * simply use the supplied URI to get a copy of the original document.
     * @param uri The string representation of the URI of the 
     * document to be retrieved.
     * @return a DOM Document containing the XML representation of the
     * file at the specified URI.  Returns null if the store does not
     * contain a document with the given URI.
     * @throws XBRLException if the document cannot be constructed as a DOM.
     */
    public Element getDocumentAsDOM(URI uri) throws XBRLException {
    	return getSubtree(this.getRootFragmentForDocument(uri));
    }
	
	/**
     * Get a single document in the store as a DOM including annotations.
     * @param uri The string representation of the URI of the 
     * document to be retrieved.
     * @return an annotated DOM Document containing the XML representation of the
     * file at the specified URI.  Returns null if the store does not
     * contain a document with the given URI.
     * @throws XBRLException if more or less than one document is found in the store matching 
     * the supplied URI.
     */
    private Element getAnnotatedDocumentAsDOM(URI uri) throws XBRLException {
        URI matchURI = getMatcher().getMatch(uri);
        List<Fragment> fragments = query("/*[@uri='" + matchURI + "' and @parentIndex='none']");
        if (fragments.size() > 1) throw new XBRLException("More than one document was found in the data store.");
        if (fragments.size() == 0) throw new XBRLException("No documents were found in the data store.");
        Fragment fragment = fragments.get(0);
        Element document = this.getAnnotatedSubtree(fragment);
        document.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":index",fragment.getIndex());
        return document;
    }
    
	/**
	 * Returns the root element of the subtree starting with the
	 * fragment with the specified index.  All subtrees for a given store
	 * instance are produced from the one XML DOM and so can be appended
	 * to eachother as required.
	 * @param f The fragment at the root of the subtree.
	 * @return The root element of the subtree headed by the fragment
	 * with the specified index.
	 * @throws XBRLException if the subtree cannot be constructed.
	 */
	public Element getSubtree(Fragment f) throws XBRLException {

		// Make sure that the DOM is initialised.
		if (storeDOM == null) {
			storeDOM = (new XMLDOMBuilder()).newDocument();
		}

		// Get the DOM representation of the fragment
		Element d = null;
		try {
		    d = (Element) storeDOM.importNode(f.getDataRootElement(), true);
		} catch (Exception e) {
		    f.getStore().serialize(f.getMetadataRootElement());
		    throw new XBRLException("The data could not be plugged into the DOM for fragment " + f.getIndex(),e);
		}
		    
		// Get the child fragment IDs
		List<Fragment> fs = this.query("/"+ Constants.XBRLAPIPrefix + ":" + "fragment[@parentIndex='" + f.getIndex() + "']");
		
		// With no children, just return the fragment
		if (fs.size() == 0) {
			return d;

		}

		// Sort the child fragments into insertion order
		Comparator<Fragment> comparator = new FragmentComparator();
		TreeSet<Fragment> fragments = new TreeSet<Fragment>(comparator);
    	for (int i=0; i<fs.size(); i++) {
    		fragments.add(fs.get(i));
    	}
    	
    	// Iterate child fragments in insertion order, inserting them
    	Iterator<Fragment> iterator = fragments.iterator();
    	while (iterator.hasNext()) {

    		//Get child fragment
    		Fragment childFragment = iterator.next();

    		// Get XML DOM for child fragment using recursion
    		Element child = getSubtree(childFragment);

    		// Get parent element of child fragment in current fragment
    		Element parentElement = childFragment.getParentElement(d);

    		// Determine the number of preceding siblings
    		int precedingSiblings = (new Integer(childFragment.getPrecedingSiblings())).intValue();
    		
    		// Get the following sibling of this child fragment
    		Element followingSibling = getFollowingSibling(parentElement, precedingSiblings);

    		// Do the fragment insertion
            parentElement.insertBefore(child, followingSibling);
    		
    	}
		return d;
	}
	
    /**
     * Get the following sibling of this fragment's root in the parent fragment's data.
     * @param parentElement The parent element in the parent fragment's data.
     * @param precedingSiblings The number of sibling elements preceding the element of interest.
     * @return the following sibling of this fragment's root (or null if there is no preceding sibling).
     */
    protected Element getFollowingSibling(Element parentElement, int precedingSiblings) {
    	
    	// Traverse the parent data DOM to find the relevant child node
		int siblingCount = 0;
		NodeList children = parentElement.getChildNodes();
		for (int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				siblingCount++;  // We have found a sibling element
				if (siblingCount > precedingSiblings) return (Element) child;
			}
		}
		return null;
    	
    }	

	/**
	 * Returns the root element of the annotated subtree starting with the
	 * fragment with the specified index.  All subtrees for a given store
	 * instance are produced from the one XML DOM and so can be appended
	 * to eachother as required.
	 * @param f The fragment at the root of the subtree.
	 * @return the root element of the subtree headed by the fragment
	 * with the specified index.
	 * @throws XBRLException if the subtree cannot be constructed.
	 */
	private Element getAnnotatedSubtree(Fragment f) throws XBRLException {
		
    	//logger.debug((new Date()) + ":Getting fragment " + f.getIndex());
    	
		// Make sure that the DOM is initialised.
		if (storeDOM == null) {
			storeDOM = (new XMLDOMBuilder()).newDocument();
		}

		// Get the DOM representation of the fragment
		Element d = (Element) storeDOM.importNode(f.getDataRootElement(), true);
		
		// Get the child fragment IDs
		List<Fragment> fs = this.query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + f.getIndex() + "']");
		
		// With no children, just return the fragment
		if (fs.size() == 0) {
			return d;

		}

		// Sort the child fragments into insertion order
		TreeSet<Fragment> fragments = new TreeSet<Fragment>(new FragmentComparator());
		for (int i=0; i<fs.size(); i++) {
    		fragments.add(fs.get(i));
    	}
    	
    	// Iterate child fragments in insertion order, inserting them
    	Iterator<Fragment> iterator = fragments.iterator();
    	while (iterator.hasNext()) {
    		
    		// Get child fragment
    		Fragment childFragment = iterator.next();

    		// Get XML DOM for child fragment using recursion
    		Element child = getAnnotatedSubtree(childFragment);
	    	child.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":index",childFragment.getIndex());

    		// Get parent element of child fragment in current fragment
    		Element parentElement = childFragment.getParentElement(d);

    		// Determine the number of preceding siblings
    		int precedingSiblings = (new Integer(childFragment.getPrecedingSiblings())).intValue();
    		
    		// Get the following sibling of this child fragment
    		Element followingSibling = getFollowingSibling(parentElement, precedingSiblings);

    		// Do the fragment insertion
            parentElement.insertBefore(child, followingSibling);
    		
    	}
		return d;
	}
	
	
    /**
     * Get all documents in the store as a single DOM.  Note that this will
     * not reflect the original documents in some ways.  Importantly, 
     * entities will be resolved and document type declarations will be missing.
     * Document encodings may also differ.  If the original documents are required,
     * simply use the URIs captured in the data store to get a copy of the original 
     * document.  @see org.xbrlapi.data.Store#getStoredURIs() for more details.
     * Get all data in the store as a single XML DOM object.
     * @return the XML DOM representation of the XBRL information in the 
     * data store.
     * @throws XBRLException if the DOM cannot be constructed.
     */
    public Document getStoreAsDOM() throws XBRLException {

		if (storeDOM == null) {
			storeDOM = (new XMLDOMBuilder()).newDocument();
		}
		
    	Element root = storeDOM.createElementNS(Constants.XBRLAPINamespace,Constants.XBRLAPIPrefix + ":dts");
		
		List<URI> uris = getStoredURIs();
		Iterator<URI> iterator = uris.iterator();
		while (iterator.hasNext()) {
			URI uri = iterator.next();
			Element e = getDocumentAsDOM(uri);
			root.appendChild(e);			
		}
    	storeDOM.appendChild(root);
    	return storeDOM;
    }
		
	/**
     * Get all data in the store as a single XML DOM object including
     * the annotations used in the 
     * <a href="http://www.sourceforge.net/xbrlcomposer/">XBRLComposer</a> project.
     * @return the composed data store as a DOM object.
     * @throws XBRLException if the composed data store cannot be constructed.
     */
    public Document formCompositeDocument() throws XBRLException {
    	
		if (storeDOM == null) {
			storeDOM = (new XMLDOMBuilder()).newDocument();
		}
		
    	Element root = storeDOM.createElementNS(Constants.CompNamespace,Constants.CompPrefix + ":dts");
		
		List<URI> uris = getStoredURIs();
		long counter = 1;
		for (URI uri: uris) {
	    	Element file = storeDOM.createElementNS(Constants.CompNamespace,Constants.CompPrefix + ":file");
	    	file.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":uri", uri.toString());
	    	file.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":index","composite_" + new Long(counter++).toString());
			root.appendChild(file);
			file.appendChild(getAnnotatedDocumentAsDOM(uri));
		}
    	root.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":index","composite_" + new Long(counter++).toString());
    	storeDOM.appendChild(root);
    	return storeDOM;
    	
    }
    

    

    
    /**
     * @see org.xbrlapi.data.Store#getStubs()
     */
    public List<Stub> getStubs() throws XBRLException {
        return this.<Stub>gets("Stub");
    }
    
    /**
     * @see org.xbrlapi.data.Store#getStub(URI)
     */
    public Stub getStub(URI uri) throws XBRLException {
        URI matchURI = getMatcher().getMatch(uri);
        List<Stub> stubs = this.<Stub>query("/*[@type='org.xbrlapi.impl.StubImpl' and @uri='" + matchURI + "']");
        if (stubs.size() == 0) return null;
        if (stubs.size() > 1) throw new XBRLException("There are " + stubs.size() + " stubs for " + uri);
        return stubs.get(0);
    }
    
    /**
     * @see org.xbrlapi.data.Store#getStub(URI stubId)
     */
    public void removeStub(String stubId) throws XBRLException {
        if (hasFragment(stubId)) remove(stubId);
    }
    
    /**
     * @see org.xbrlapi.data.Store#remove(XML)
     */
    public void remove(XML fragment) throws XBRLException {
        remove(fragment.getIndex());
    }    
    
    
    /**
     * @see org.xbrlapi.data.Store#getDocumentsToDiscover()
     */
    public synchronized List<URI> getDocumentsToDiscover() throws XBRLException {
        List<Stub> stubs = getStubs();
        LinkedList<URI> list = new LinkedList<URI>();
        for (Stub stub: stubs) {
            String uri = stub.getMetaAttribute("uri");
            try {
                list.add(new URI(uri));
            } catch (URISyntaxException e) {
                throw new XBRLException("URI " + uri + " is malformed", e);
            }
        }
        return list;
    }
    
	/**
	 * Saves the individual documents in the data store 
	 * into a directory structure that is placed into
	 * the specified directory.  The directory structure that is 
	 * created mirrors the structure of the URIs of the documents. 
	 * Note that the URIs of the documents that are written out
	 * will be reflected in the paths to those documents
	 * using the same rules as those applied for document caching.
	 * @param destination The folder in which the directory structure and
	 * the documents in the data store are to be saved.  
	 * @throws XBRLException If the root folder does not exist or 
	 * is not a directory or if the documents in the store cannot 
	 * be saved to the local file system.
	 */
	public void saveDocuments(File destination) throws XBRLException {
		saveDocuments(destination, "");
	}
	
	/**
	 * Serializes those documents in the data store with a URI that
	 * begins with the specified URI prefix. They are saved to the local
	 * file system in the same manner as is applied for the saveDocuments
	 * method that operates on all documents in the data store.
	 * @param destination The folder in which the directory structure and
	 * the documents in the data store are to be saved.
	 * @param uriPrefix All documents in the data store with a URI that begins 
	 * with the string specified by uriPrefix will be saved to the local
	 * file system.
	 * @throws XBRLException If the root folder does not exist or 
	 * is not a directory or if the documents in the store cannot 
	 * be saved to the local file system.
	 */
	public void saveDocuments(File destination, String uriPrefix) throws XBRLException {
		
		if (! destination.exists()) throw new XBRLException("The specified directory does not exist.");
		
		if (! destination.isDirectory()) throw new XBRLException("A directory rather than a file must be specified.");
		
		List<URI> uris = getStoredURIs();
		Iterator<URI> iterator = uris.iterator();
		while (iterator.hasNext()) {			
			URI uri = iterator.next();
			if (uri.toString().startsWith(uriPrefix)) {
				CacheImpl cache = new CacheImpl(destination);
				File file = cache.getCacheFile(uri);
				Element e = getDocumentAsDOM(uri);
				serialize(e,file);
			}
		}
		
	}
	
	/**
	 * Creates a single DOM structure from all documents in the 
	 * data store and saves this single XML structure in the
	 * specified file.
	 * @param file The file to save the Store content to.
	 * @throws XBRLException if the documents in the store cannot be
	 * saved to the single file.
	 */
	public void saveStoreAsSingleDocument(File file) throws XBRLException {
		serialize(getStoreAsDOM().getDocumentElement(),file);
	}
    
    /**
     * Convert a DOM element (and its descendents) to a string.
     * @param element The element to convert to a string.
     * @return The string that is the serialised element.
     * @throws XBRLException if an IO exception occurs.
     */
    protected String DOM2String(Element element) {
        StringWriter sw = new StringWriter();
        String out = null;
    	try {
			org.apache.xml.serialize.OutputFormat format = new org.apache.xml.serialize.OutputFormat("xml", "UTF-8", true);
			org.apache.xml.serialize.XMLSerializer output = new org.apache.xml.serialize.XMLSerializer(sw, format);
			output.setNamespaces(true);
		    output.serialize(element);
	        sw.flush();
	        out = sw.toString();	        
	        sw.close();
    	} catch (IOException e) {
            // StringWriter does not generate any IOExceptions; it can create only OutOfMemoryError
    	}
        return out;
    }
 
    /**
     * @see org.xbrlapi.data.Store#gets(String)
     */
    public <F extends XML> List<F> gets(String interfaceName) throws XBRLException {
        String query = "/*[@type='org.xbrlapi.impl." + interfaceName + "Impl']";
        if (interfaceName.indexOf(".") > -1) {
            query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='" + interfaceName + "']";
        }
    	return this.<F>query(query);
    }
    
    /**
     * @see org.xbrlapi.data.Store#getChildFragments(String, String)
     */
    public <F extends Fragment> List<F> getChildFragments(String interfaceName, String parentIndex) throws XBRLException {
    	return this.<F>query("/*[@type='org.xbrlapi.impl." + interfaceName + "Impl' and @parentIndex='" + parentIndex + "']");
    }    
    
    /**
     * @see org.xbrlapi.data.Store#getAllNetworks()
     */
    public Networks getAllNetworks() throws XBRLException {

        Networks networks = new NetworksImpl(this);
        
        // First get the set of arcs using the arc role
        List<Arc> arcs = this.<Arc>gets("Arc");
        for (Arc arc: arcs) {
            List<ArcEnd> sources = arc.getSourceFragments();
            List<ArcEnd> targets = arc.getTargets();
            for (ArcEnd source: sources) {
                for (ArcEnd target: targets) {
                    Fragment s = null;
                    Fragment t = null;
                    if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
                        s = ((Locator) source).getTarget();
                    else s = source;
                    if (target.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
                        t = ((Locator) target).getTarget();
                    else t = target;
                    Relationship relationship = new RelationshipImpl(arc,s,t);
                    networks.addRelationship(relationship);
                }
            }
        }
        return networks;        
    }
    
    /**
     * @see org.xbrlapi.data.Store#getNetworks()
     */
    public Networks getNetworks() throws XBRLException {

        Networks networks = new NetworksImpl(this);
        
        // First get the set of arcs using the arc role
        List<Arc> arcs = this.<Arc>gets("Arc");
        for (Arc arc: arcs) {
            List<ArcEnd> sources = arc.getSourceFragments();
            List<ArcEnd> targets = arc.getTargets();
            for (ArcEnd source: sources) {
                for (ArcEnd target: targets) {
                    Fragment s = null;
                    Fragment t = null;
                    if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
                        s = ((Locator) source).getTarget();
                    else s = source;
                    if (target.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
                        t = ((Locator) target).getTarget();
                    else t = target;
                    Relationship relationship = new RelationshipImpl(arc,s,t);
                    networks.addRelationship(relationship);
                }
            }
        }
        return networks;        
    }

    /**
     * @see org.xbrlapi.data.Store#getNetworks(URI)
     */
    public Networks getNetworks(URI arcrole) throws XBRLException {

        Networks networks = new NetworksImpl(this);
    	
    	// First get the set of arcs using the arc role
		List<Arc> arcs = getArcs(arcrole);
		logger.info(arcs.size());
    	for (Arc arc: arcs) {
    		List<ArcEnd> sources = arc.getSourceFragments();
    		List<ArcEnd> targets = arc.getTargets();
    		for (ArcEnd source: sources) {
    			for (ArcEnd target: targets) {
    				Fragment s = null;
    				Fragment t = null;
            		if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
            			s = ((Locator) source).getTarget();
            		else s = source;
            		if (target.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
            			t = ((Locator) target).getTarget();
            		else t = target;
        			Relationship relationship = new RelationshipImpl(arc,s,t);
        			networks.addRelationship(relationship);
    			}
    		}
    	}
    	return networks;    	
    }
    
    /**
     * @see org.xbrlapi.data.Store#getNetworks(URI,URI)
     */
    public Networks getNetworks(URI linkRole, URI arcrole) throws XBRLException {

        Networks networks = new NetworksImpl(this);

        Map<String,ExtendedLink> links = new HashMap<String,ExtendedLink>();
        
        // First get the set of arcs using the arc role
        List<Arc> arcs = getArcs(arcrole);
        for (Arc arc: arcs) {
            ExtendedLink link = null;
            String linkIndex = arc.getParentIndex();
            if (links.containsKey(linkIndex)) {
                link = links.get(linkIndex);
            } else {
                link = (ExtendedLink) arc.getParent();
                links.put(link.getIndex(),link);
            }
            URI myLinkrole = link.getLinkRole();
            if (myLinkrole.equals(linkRole)) {
                List<ArcEnd> sources = arc.getSourceFragments();
                List<ArcEnd> targets = arc.getTargets();
                for (ArcEnd source: sources) {
                    for (ArcEnd target: targets) {
                        Fragment s = null;
                        Fragment t = null;
                        if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
                            s = ((Locator) source).getTarget();
                        else s = source;
                        if (target.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
                            t = ((Locator) target).getTarget();
                        else t = target;
                        Relationship relationship = new RelationshipImpl(arc,s,t);
                        networks.addRelationship(relationship);
                    }
                }
                
            }
        }
        return networks;        
    }

    
    /**
     * @param arcrole The arcrole to use to identify the arcs to retrieve.
     * @return the list of arc fragments with a given arc role value.
     * @throws XBRLException
     */
    private List<Arc> getArcs(URI arcrole) throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/*[@xlink:arcrole='" + arcrole + "' and @xlink:type='arc']]";
    	return this.<Arc>query(query);
    }
    
    
    
    /**
     * Utility method to return a list of fragments in a data store
     * that have a type corresponding to the specified fragment interface name and
     * that are in the document with the specified URI.
     * @param uri The URI of the document to get the fragments from.
     * @param interfaceName The name of the interface.  EG: If a list of
     *  org.xbrlapi.impl.ReferenceArcImpl fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an Arc would not return all ReferenceArcs as well as other
     *  types of arcs.
     * @return a list of fragments with the given fragment type and in the given document.
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getsFromDocument(URI uri, String interfaceName) throws XBRLException {
        URI matchURI = getMatcher().getMatch(uri);
        return this.<F>query("/*[@uri='"+ matchURI + "' and @type='org.xbrlapi.impl." + interfaceName + "Impl']");
    }    
    
    /**
     * @return a list of all of the root-level facts in the data store (those facts
     * that are children of the root element of an XBRL instance).  Returns an empty list 
     * if no facts are found.
     * @throws XBRLException
     */
    public List<Fact> getFacts() throws XBRLException {
    	List<Instance> instances = this.<Instance>gets("Instance");
    	return getFactsFromInstances(instances);
    }
    

    

    
    /**
     * Helper method for common code in the getTuple methods.
     * @param instances The instances to retrieve tuples for.
     * @return a list of root tuples in the instances.
     * @throws XBRLException
     */
    private List<Tuple> getTuplesFromInstances(List<Instance> instances) throws XBRLException {
    	List<Fact> facts = getFactsFromInstances(instances);
    	List<Tuple> tuples = new Vector<Tuple>();
    	for (Fact fact: facts) {
    		if (fact.getType().equals("org.xbrlapi.org.impl.TupleImpl"))
    			tuples.add((Tuple) fact);
    	}
    	return tuples;
    }    
    

    


    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level facts in the specified document.
     * @throws XBRLException
     */
    public List<Fact> getFacts(URI uri) throws XBRLException {
    	List<Instance> instances = this.<Instance>getsFromDocument(uri,"Instance");
    	return this.getFactsFromInstances(instances);
    }
    

    


    /**
     * @see org.xbrlapi.data.Store#getRootFragmentForDocument(URI)
     */
    public <F extends Fragment> F getRootFragmentForDocument(URI uri) throws XBRLException {
    	List<F> fragments = this.<F>query("/*[@uri='" + uri + "' and @parentIndex='none']");
    	if (fragments.size() == 0) return null;
    	if (fragments.size() > 1) throw new XBRLException("Two fragments identify themselves as roots of the one document.");
    	return fragments.get(0);
    }

    
    /**
     * @see org.xbrlapi.data.Store#getRootFragments()
     */
    public <F extends Fragment> List<F> getRootFragments() throws XBRLException {
        long start = System.currentTimeMillis();
        logger.debug("Getting all root fragments.");
    	List<F> roots =  this.<F>query("/*[@parentIndex='none']");
        logger.debug("Retrieval of root fragments took " + (System.currentTimeMillis() - start) + " milliseconds.");
        return roots;
    }

    /**
     * @see org.xbrlapi.data.Store#getLanguage(String, String)
     */
    public Language getLanguage(String encoding, String code) throws XBRLException {
        if (encoding == null) throw new XBRLException("The language code must not be null.");
        if (code == null) throw new XBRLException("The language name encoding must not be null.");
        String query = "/"+ Constants.XBRLAPIPrefix + ":" + "fragment[@type='org.xbrlapi.impl.LanguageImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/lang:language/lang:encoding='" + encoding.toUpperCase() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/lang:language/lang:code='" + code.toUpperCase() + "']";
        List<Language> languages = this.<Language>query(query);
        if (languages.size() == 0) return null;
        return languages.get(0);
    }
    
    
    
    /**
     * @see org.xbrlapi.data.Store#getLanguages(String)
     */
    public List<Language> getLanguages(String code) throws XBRLException {
        if (code == null) throw new XBRLException("The language code must not be null.");
        String query = "/*[@type='org.xbrlapi.impl.LanguageImpl' and */lang:language/lang:code='" + code.toUpperCase() + "']";
        return this.<Language>query(query);
    }

    private Networks networks = null;
    /**
     * @see Store#getStoredNetworks()
     */
    public synchronized Networks getStoredNetworks() {
        return networks;
    }



    /**
     * @see Store#setStoredNetworks(org.xbrlapi.networks.Networks)
     */
    public synchronized void setStoredNetworks(Networks networks) {
        this.networks = networks;
    }

    /**
     * @see Store#queryForString(String)
     */
    public String queryForString(String query) throws XBRLException {
        Set<String> strings = queryForStrings(query);
        if (strings.size() == 0) return null;
        if (strings.size() > 1) throw new XBRLException(query + " returned more than one string.");
        for (String string: strings) return string;
        throw new XBRLException("This exception cannot be thrown. There is a bug in the software.");
    }
    


    
    /**
     * This method is provided as a helper method for the getFact methods.
     * @param instances The list of instance fragments to extract facts from.
     * @return The list of facts in the instances.
     * @throws XBRLException
     */
    private List<Fact> getFactsFromInstances(List<Instance> instances) throws XBRLException {
        List<Fact> facts = new Vector<Fact>();
        for (Instance instance: instances) {
            facts.addAll(instance.getFacts());
        }
        return facts;
    }
    
    /**
     * Helper method for common code in the getItem methods.
     * @param instances The instances to retrieve items for.
     * @return a list of root items in the instances.
     * @throws XBRLException
     */
    private List<Item> getItemsFromInstances(List<Instance> instances) throws XBRLException {
        List<Fact> facts = getFactsFromInstances(instances);
        List<Item> items = new Vector<Item>();
        for (Fact fact: facts) {
            if (! fact.getType().equals("org.xbrlapi.org.impl.TupleImpl"))
                items.add((Item) fact);
        }
        return items;
    }
    
    
    
    /**
     * @return a list of all of the root-level items in the data store(those items
     * that are children of the root element of an XBRL instance).
     * TODO eliminate the redundant retrieval of tuples from the getItems methods.
     * @throws XBRLException
     */
    public List<Item> getItems() throws XBRLException {
        List<Instance> instances = this.<Instance>gets("Instance");
        return getItemsFromInstances(instances);
    }
    
    /**
     * @return a list of all of the tuples in the data store.
     * @throws XBRLException
     */
    public List<Tuple> getTuples() throws XBRLException {
        List<Instance> instances = this.<Instance>gets("Instance");
        return this.getTuplesFromInstances(instances);
    }


    
    /**
     * @param uri The URI of the document to get the items from.
     * @return a list of all of the root-level items in the data store.
     * @throws XBRLException
     */
    public List<Item> getItems(URI uri) throws XBRLException {
        List<Instance> instances = this.<Instance>getsFromDocument(uri,"Instance");
        return this.getItemsFromInstances(instances);
    }
    
    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level tuples in the specified document.
     * @throws XBRLException
     */
    public List<Tuple> getTuples(URI uri) throws XBRLException {
        List<Instance> instances = this.<Instance>getsFromDocument(uri,"Instance");
        return this.getTuplesFromInstances(instances);
    }

    /**
     * @see XBRLStore#getNetworkRoots(URI, String, URI, URI, String, URI)
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> List<F> getNetworkRoots(URI linkNamespace, String linkName, URI linkRole, URI arcNamespace, String arcName, URI arcrole) throws XBRLException {
        
        // Get the links that contain the network declaring arcs.
        String linkQuery = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ExtendedLinkImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*[namespace-uri()='" + linkNamespace + "' and local-name()='" + linkName + "' and @xlink:role='" + linkRole + "']]";
        List<ExtendedLink> links = this.<ExtendedLink>query(linkQuery);
        
        // Get the arcs that declare the relationships in the network.
        // For each arc map the ids of the fragments at their sources and targets.
        HashMap<String,String> sourceIds = new HashMap<String,String>();
        HashMap<String,String> targetIds = new HashMap<String,String>();
        for (int i=0; i<links.size(); i++) {
            ExtendedLink link = links.get(i);
            List<Arc> arcs = link.getArcs();
            for (Arc arc: arcs) {
                if (arc.getNamespace().equals(arcNamespace))
                    if (arc.getLocalname().equals(arcName))
                        if (arc.getArcrole().equals(arcrole)) {
                            List<ArcEnd> sources = arc.getSourceFragments();
                            List<ArcEnd> targets = arc.getTargets();
                            for (int k=0; k<sources.size(); k++) {
                                sourceIds.put(sources.get(k).getIndex(),"");
                            }
                            for (int k=0; k<sources.size(); k++) {
                                targetIds.put(targets.get(k).getIndex(),"");
                            }
                        }
            }
        }
        
        // Get the root resources in the network
        List<F> roots = new Vector<F>();
        Iterator<String> iterator = sourceIds.keySet().iterator();
        while (iterator.hasNext()) {
            String id = iterator.next();
            if (! targetIds.containsKey(id)) {
                Fragment target = this.get(id);
                if (! target.isa("org.xbrlapi.impl.LocatorImpl"))
                    roots.add((F) target);
                else {
                    roots.add((F) ((Locator) target).getTarget());
                }
            }
        }
        return roots;
    }

    /**
     * @see XBRLStore#getNetworkRoots(URI, URI)
     */
    public List<Fragment> getNetworkRoots(URI linkRole, URI arcrole) throws XBRLException {
        
        // Get the links that contain the network declaring arcs.
        String linkQuery = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ExtendedLinkImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*[@xlink:role='" + linkRole + "']]";
        List<ExtendedLink> links = this.<ExtendedLink>query(linkQuery);
        
        // Get the arcs that declare the relationships in the network.
        // For each arc map the ids of the fragments at their sources and targets.
        HashMap<String,String> sourceIds = new HashMap<String,String>();
        HashMap<String,String> targetIds = new HashMap<String,String>();
        for (int i=0; i<links.size(); i++) {
            ExtendedLink link = links.get(i);
            List<Arc> arcs = link.getArcs();
            for (Arc arc: arcs) {
                if (arc.getArcrole().equals(arcrole)) {
                    List<ArcEnd> sources = arc.getSourceFragments();
                    List<ArcEnd> targets = arc.getTargets();
                    for (int k=0; k<sources.size(); k++) {
                        sourceIds.put(sources.get(k).getIndex(),"");
                    }
                    for (int k=0; k<sources.size(); k++) {
                        targetIds.put(targets.get(k).getIndex(),"");
                    }
                }
            }
        }
        
        // Get the root resources in the network
        List<Fragment> roots = new Vector<Fragment>();
        for (String id: sourceIds.keySet()) {
            if (! targetIds.containsKey(id)) {
                roots.add(this.<Fragment>get(id));
            }
        }
        return roots;
    }    
    
    
    /**
     * @param namespace The namespace for the concept.
     * @param name The local name for the concept.
     * @return the concept fragment for the specified namespace and name.
     * @throws XBRLException if more than one matching concept is found in the data store
     * or if no matching concepts are found in the data store.
     */
    public Concept getConcept(URI namespace, String name) throws XBRLException {
        
        List<SchemaDeclaration> candidates = this.<SchemaDeclaration>query("/*[*/xsd:element[@name='" + name + "']]");
        List<Concept> matches = new Vector<Concept>();
        for (SchemaDeclaration candidate: candidates) {
            if (candidate.getTargetNamespace().equals(namespace)) {
                try {
                    matches.add((Concept) candidate);
                } catch (Exception e) {
                    ;// Casting to a concept failed so it is not a concept.
                }
            }
        }
        
        if (matches.size() == 0) 
            throw new XBRLException("No matching concepts were found for " + namespace + ":" + name + ".");
        
        if (matches.size() > 1) 
            throw new XBRLException(new Integer(matches.size()) + "matching concepts were found for " + namespace + ":" + name + ".");
        
        return matches.get(0);
    }

    /**
     * @return a list of roleType fragments
     * @throws XBRLException
     */
    public List<RoleType> getRoleTypes() throws XBRLException {
        return this.<RoleType>gets("RoleType");
    }
    
    /**
     * @see org.xbrlapi.data.XBRLStore#getRoleTypes(URI)
     */
    public List<RoleType> getRoleTypes(URI uri) throws XBRLException {
        String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/link:roleType/@roleURI='" + uri + "']";
        return this.<RoleType>query(query);
    }    
    
    /**
     * @return a list of ArcroleType fragments
     * @throws XBRLException
     */
    public List<ArcroleType> getArcroleTypes() throws XBRLException {
        return this.<ArcroleType>gets("ArcroleType");
    }
    
    /**
     * @return a list of arcroleType fragments that define a given arcrole.
     * @throws XBRLException
     */
    public List<ArcroleType> getArcroleTypes(String uri) throws XBRLException {
        String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/link:arcroleType/@arcroleURI='" + uri + "']";
        return this.<ArcroleType>query(query);
    }
    
    /**
     * @see org.xbrlapi.data.XBRLStore#getResourceRoles()
     */
    public List<URI> getResourceRoles() throws XBRLException {
        HashMap<URI,String> roles = new HashMap<URI,String>();
        List<Resource> resources = this.<Resource>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:type='resource']");
        for (Resource resource: resources) {
            URI role = resource.getResourceRole();
            if (! roles.containsKey(role)) roles.put(role,"");
        }
        List<URI> result = new Vector<URI>();
        result.addAll(roles.keySet());
        return result;
    }    


    /**
     * @see org.xbrlapi.data.XBRLStore#getMinimumDocumentSet(URI)
     */
    public List<URI> getMinimumDocumentSet(URI uri) throws XBRLException {
        List<URI> starters = new Vector<URI>();
        starters.add(uri);
        return this.getMinimumDocumentSet(starters);
    }
    
    /**
     * @see org.xbrlapi.data.XBRLStore#getMinimumDocumentSet(List)
     */
    public List<URI> getMinimumDocumentSet(List<URI> starters) throws XBRLException {
        
        List<URI> allDocuments = new Vector<URI>();        
        List<URI> documentsToCheck = new Vector<URI>();        
        Map<URI,String> foundDocuments = new HashMap<URI,String>();

        for (URI starter: starters) {
            if (! foundDocuments.containsKey(starter)) {
                foundDocuments.put(starter,"");
                documentsToCheck.add(starter);
            }
        }
        
        while (documentsToCheck.size() > 0) {
            URI doc = documentsToCheck.get(0);
            allDocuments.add(doc);
            List<URI> newDocuments = this.getReferencedDocuments(doc);
            for (URI newDocument: newDocuments) {
                if (! foundDocuments.containsKey(newDocument)) {
                    foundDocuments.put(newDocument,"");
                    documentsToCheck.add(newDocument);
                }
            }
            documentsToCheck.remove(0);
        }

        return allDocuments;

    }
 
    /**
     * @see XBRLStore#getExtendedLinksWithRole(URI)
     */
    public List<ExtendedLink> getExtendedLinksWithRole(URI linkrole) throws XBRLException {
        String query = "/*[*/*[@xlink:type='extended' and @xlink:role='" + linkrole + "']]";
        List<ExtendedLink> links = this.<ExtendedLink>query(query);
        return links;
    }

    /**
     * Tracks the fragments that have been processed to get minimal networks with a given arcrole
     */
    private HashMap<String,Fragment> processedFragments = new HashMap<String,Fragment>();


    /**
     * @see XBRLStore#getMinimalNetworksWithArcrole(Fragment, URI)
     */
    public Networks getMinimalNetworksWithArcrole(Fragment fragment, URI arcrole) throws XBRLException {
        List<Fragment> list = new Vector<Fragment>();
        list.add(fragment);
        return this.getMinimalNetworksWithArcrole(list,arcrole);
    }    
    
    /**
     * @see XBRLStore#getMinimalNetworksWithArcrole(FragmentList, URI)
     */
    public Networks getMinimalNetworksWithArcrole(List<Fragment> fragments, URI arcrole) throws XBRLException {
        
        Networks networks = new NetworksImpl(this);

        processedFragments = new HashMap<String,Fragment>();
        
        for (Fragment fragment: fragments) {
            networks = augmentNetworksForFragment(fragment,arcrole,networks);
        }
        return networks;
    }
    
    /**
     * This method is recursive.
     * @param fragment The fragment to use as the target for the relationships to be added to the networks
     * @param arcrole The arcrole for the networks to augment.
     * @param networks The networks system to augment.
     * @return The networks after augmentation.
     * @throws XBRLException
     */
    private Networks augmentNetworksForFragment(Fragment fragment, URI arcrole, Networks networks) throws XBRLException {
        if (processedFragments.containsKey(fragment.getIndex())) {
            return networks;
        }
        for (Relationship relationship: this.getActiveRelationshipsTo(fragment.getIndex(),null,arcrole)) {
            networks.addRelationship(relationship);
        }
        for (Network network: networks.getNetworks(arcrole)) {
            SortedSet<Relationship> activeRelationships = network.getActiveRelationshipsTo(fragment.getIndex());
            logger.debug(fragment.getIndex() + " has " + activeRelationships.size() + " parent fragments.");
            for (Relationship activeRelationship: activeRelationships) {
                Fragment source = activeRelationship.getSource();
                networks = augmentNetworksForFragment(source,arcrole,networks);
            }
        }
        return networks;
    }

    /**
     * @see org.xbrlapi.data.XBRLStore#getArcroles()
     */
    public Set<URI> getArcroles() throws XBRLException {
        String query = "/*/*/*[@xlink:type='arc']/@xlink:arcrole";
        Set<String> values = this.queryForStrings(query);
        Set<URI> arcroles = new TreeSet<URI>();
        for (String value: values) {
            try {
                arcroles.add(new URI(value));
            } catch (URISyntaxException e) {
                throw new XBRLException(value + " is an arcrole with an invalid URI syntax.",e);
            }
        }
        return arcroles;
    }

    /**
     * @see org.xbrlapi.data.XBRLStore#getLinkRoles()
     */
    public Set<URI> getLinkRoles() throws XBRLException {
        String query = "/*[@type='org.xbrlapi.impl.ExtendedLinkImpl']/*/*/@xlink:role";
        Set<String> values = this.queryForStrings(query);
        Set<URI> linkRoles = new TreeSet<URI>();
        for (String value: values) {
            try {
                linkRoles.add(new URI(value));
            } catch (URISyntaxException e) {
                throw new XBRLException(value + " is a link role with an invalid URI syntax.",e);
            }
        }
        return linkRoles;
    }

    /**
     * @see org.xbrlapi.data.XBRLStore#getLinkRoles(URI)
     */
    public Set<URI> getLinkRoles(URI arcrole) throws XBRLException {
        
        String query = "/*[*/*[@xlink:type='arc' and @xlink:arcrole='" + arcrole + "']]/@parentIndex";
        Set<String> linkIndices = this.queryForStrings(query);
        Set<URI> linkRoles = new TreeSet<URI>();
        for (String index: linkIndices) {
            ExtendedLink link = this.get(index);
            linkRoles.add(link.getLinkRole());
        }
        return linkRoles;
    }
    
    /**
     * @see org.xbrlapi.data.XBRLStore#getArcroles(URI)
     */
    public Set<URI> getArcroles(URI linkRole) throws XBRLException {
        
        String query = "/*[@type='org.xbrlapi.impl.ExtendedLinkImpl' and */*/@xlink:role='" + linkRole + "']/@index";
        Set<String> linkIndices = this.queryForStrings(query);
        Set<String> arcroles = new TreeSet<String>();
        for (String linkIndex: linkIndices) {
            query = "/*[@parentIndex='" + linkIndex + "' and */*/@xlink:type='arc']/*/*/@xlink:arcrole";
            Set<String> candidates = queryForStrings(query);
            arcroles.addAll(candidates);
        }
        Set<URI> result = new TreeSet<URI>();
        for (String arcrole: arcroles) {
            try {
                result.add(new URI(arcrole));
            } catch (URISyntaxException e) {
                throw new XBRLException(arcrole + " has an invalid URI syntax.", e);
            }
        }
        return result;

    }

    /**
     * @see Store#getTargets(String, URI, URI)
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> List<F> getTargets(String sourceIndex, URI linkRole, URI arcrole) throws XBRLException {
        
        Set<F> targets = new HashSet<F>();
        if (this.isUsingPersistedNetworks()) {
            SortedSet<PersistedRelationship> persistedRelationships = getAnalyser().getRelationshipsFrom(sourceIndex,linkRole,arcrole);
            for (PersistedRelationship relationship: persistedRelationships) {
                try {
                    targets.add((F) relationship.getTarget());
                } catch (ClassCastException e) {
                    throw new XBRLException("A target fragment is of the wrong type: " + relationship.getTarget().getType(),e);
                }
            }
            return new Vector<F>(targets);
        }
        
        SortedSet<Relationship> relationships = this.getActiveRelationshipsFrom(sourceIndex,linkRole,arcrole);
        for (Relationship relationship: relationships) {
            targets.add((F) relationship.getTarget());
        }
        return new Vector<F>(targets);
    }
    
    /**
     * @see Store#getSources(String, URI, URI)
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> List<F> getSources(String targetIndex, URI linkRole, URI arcrole) throws XBRLException {
        
        Set<F> sources = new HashSet<F>();
        if (this.isUsingPersistedNetworks()) {
            SortedSet<PersistedRelationship> persistedRelationships = getAnalyser().getRelationshipsTo(targetIndex,linkRole,arcrole);
            for (PersistedRelationship relationship: persistedRelationships) {
                try {
                    sources.add((F) relationship.getSource());
                } catch (ClassCastException e) {
                    throw new XBRLException("A target fragment is of the wrong type: " + relationship.getSource().getType(),e);
                }
            }
            return new Vector<F>(sources);
        }
        
        SortedSet<Relationship> relationships = this.getActiveRelationshipsTo(targetIndex,linkRole,arcrole);
        for (Relationship relationship: relationships) {
            sources.add((F) relationship.getSource());
        }
        return new Vector<F>(sources);
    }    
    
    /**
     * @see Store#getActiveRelationshipsFrom(String,URI,URI)
     */
    public SortedSet<Relationship> getActiveRelationshipsFrom(String sourceIndex,URI linkRole, URI arcrole) throws XBRLException {

        SortedSet<Relationship> relationships = new TreeSet<Relationship>();
        Networks networks = this.getNetworksFrom(sourceIndex,linkRole,arcrole);
        for (Network network: networks) {
            relationships.addAll(network.getActiveRelationshipsFrom(sourceIndex));
        }
        
        return relationships;
    }
    
    /**
     * @see Store#getPersistedActiveRelationshipsFrom(String,URI,URI)
     */
    public SortedSet<PersistedRelationship> getPersistedActiveRelationshipsFrom(String sourceIndex,URI linkRole, URI arcrole) throws XBRLException {

        if (this.isUsingPersistedNetworks()) {
            Analyser analyser = this.getAnalyser();
            return analyser.getRelationshipsFrom(sourceIndex,linkRole,arcrole);
        }
        return new TreeSet<PersistedRelationship>();

    }    
    
    /**
     * @see Store#getActiveRelationshipsTo(String,URI,URI)
     */
    public SortedSet<Relationship> getActiveRelationshipsTo(String targetIndex,URI linkRole, URI arcrole) throws XBRLException {

        SortedSet<Relationship> relationships = new TreeSet<Relationship>();
        if (this.isUsingPersistedNetworks()) {
            Analyser analyser = this.getAnalyser();
            SortedSet<PersistedRelationship> persistedRelationships = analyser.getRelationshipsTo(targetIndex,linkRole,arcrole);
            for (PersistedRelationship pr: persistedRelationships) {
                relationships.add(new RelationshipImpl(pr.getArc(),pr.getSource(),pr.getTarget()));
            }
        } else {
            Networks networks = this.getNetworksFrom(targetIndex,linkRole,arcrole);
            for (Network network: networks) {
                relationships.addAll(network.getActiveRelationshipsTo(targetIndex));
            }
        }
        
        return relationships;
    }
    
    /**
     * @see Store#getPersistedActiveRelationshipsTo(String,URI,URI)
     */
    public SortedSet<PersistedRelationship> getPersistedActiveRelationshipsTo(String targetIndex,URI linkRole, URI arcrole) throws XBRLException {

        if (this.isUsingPersistedNetworks()) {
            Analyser analyser = this.getAnalyser();
            return analyser.getRelationshipsTo(targetIndex,linkRole,arcrole);
        }
        return new TreeSet<PersistedRelationship>();
    }    
    
    /**
     * @see Store#getNetworksFrom(String,URI,URI)
     */
    public Networks getNetworksFrom(String sourceIndex,URI linkRole, URI arcrole) throws XBRLException {
        Fragment source = this.get(sourceIndex);

        Networks networks = new NetworksImpl(this);
        Relationship relationship = null;

        // If we have a resource, it could be related directly via arcs to relatives.
        if (source.isa("org.xbrlapi.impl.ResourceImpl")) {
            if ((linkRole == null) || ((Resource) source).getExtendedLink().getLinkRole().equals(linkRole)) {
                List<Arc> arcs = ((ArcEnd) source).getArcsFromWithArcrole(arcrole);

                for (Arc arc: arcs) {
                    List<ArcEnd> targets = arc.getTargets();
                    for (ArcEnd end: targets) {
                        if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                            Fragment target = ((Locator) end).getTarget();
                            relationship = new RelationshipImpl(arc,source,target);
                        } else {
                            relationship = new RelationshipImpl(arc,source,end);
                        }               
                        networks.addRelationship(relationship);
                    }
                }
            }
        }

        HashMap<String,ExtendedLink> links = new HashMap<String,ExtendedLink>();
        
        // Next get the locators for the fragment to find indirect relatives
        List<Locator> locators = source.getReferencingLocators();
        LOCATORS: for (Locator locator: locators) {
            if (linkRole != null) {
                ExtendedLink link = null;
                String parentIndex = locator.getParentIndex();
                if (links.containsKey(parentIndex)) {
                    link = links.get(parentIndex);
                } else {
                    link = locator.getExtendedLink();
                    links.put(parentIndex,link);
                }
                if (! link.getLinkRole().equals(linkRole)) continue LOCATORS;
            }
            List<Arc> arcs = locator.getArcsFromWithArcrole(arcrole);
            for (Arc arc: arcs) {
                List<ArcEnd> targets = arc.getTargets();
                for (ArcEnd end: targets) {
                    if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                        Fragment target = ((Locator) end).getTarget();
                        relationship = new RelationshipImpl(arc,source,target);
                    } else {
                        relationship = new RelationshipImpl(arc,source,end);
                    }
                    networks.addRelationship(relationship);
                }
            }
        }

        return networks;        

    }
    
    /**
     * @see Store#getNetworksTo(String,URI,URI)
     */
    public Networks getNetworksTo(String targetIndex,URI linkRole, URI arcrole) throws XBRLException {
        Fragment target = this.get(targetIndex);

        Networks networks = new NetworksImpl(this);
        Relationship relationship = null;

        // If we have a resource, it could be related directly via arcs to relatives.
        if (target.isa("org.xbrlapi.impl.ResourceImpl")) {
            if ((linkRole == null) || ((Resource) target).getExtendedLink().getLinkRole().equals(linkRole)) {
                List<Arc> arcs = ((ArcEnd) target).getArcsToWithArcrole(arcrole);

                for (Arc arc: arcs) {
                    List<ArcEnd> targets = arc.getSourceFragments();
                    for (ArcEnd end: targets) {
                        if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                            Fragment source = ((Locator) end).getTarget();
                            relationship = new RelationshipImpl(arc,source,target);
                        } else {
                            relationship = new RelationshipImpl(arc,end,target);
                        }               
                        networks.addRelationship(relationship);
                    }
                }
            }
        }

        HashMap<String,ExtendedLink> links = new HashMap<String,ExtendedLink>();
        
        // Next get the locators for the fragment to find indirect relatives
        List<Locator> locators = target.getReferencingLocators();
        LOCATORS: for (Locator locator: locators) {
            if (linkRole != null) {
                ExtendedLink link = null;
                String parentIndex = locator.getParentIndex();
                if (links.containsKey(parentIndex)) {
                    link = links.get(parentIndex);
                } else {
                    link = locator.getExtendedLink();
                    links.put(parentIndex,link);
                }
                if (! link.getLinkRole().equals(linkRole)) continue LOCATORS;
            }
            List<Arc> arcs = locator.getArcsToWithArcrole(arcrole);
            for (Arc arc: arcs) {
                List<ArcEnd> sources = arc.getSourceFragments();
                for (ArcEnd end: sources) {
                    if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                        Fragment source = ((Locator) end).getTarget();
                        relationship = new RelationshipImpl(arc,source,target);
                    } else {
                        relationship = new RelationshipImpl(arc,end,target);
                    }
                    networks.addRelationship(relationship);
                }
            }
        }

        return networks;        

    }    
    
    
    
    
    // The persisted networks analyser
    private Analyser analyser = null;
    
    /**
     * @see Store#getAnalyser()
     */
    public Analyser getAnalyser() {
        return analyser;
    }
    
    /**
     * @see Store#setAnalyser(Analyser)
     */
    public void setAnalyser(Analyser analyser) {
        this.analyser = analyser;
    }
    
    /**
     * @see Store#isUsingPersistedNetworks()
     */
    public boolean isUsingPersistedNetworks() {
        return (analyser != null);
    }    
 

    
}
