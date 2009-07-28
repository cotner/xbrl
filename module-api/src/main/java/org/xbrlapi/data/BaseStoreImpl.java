package org.xbrlapi.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.xbrlapi.LabelResource;
import org.xbrlapi.Language;
import org.xbrlapi.Locator;
import org.xbrlapi.ReferenceResource;
import org.xbrlapi.Relationship;
import org.xbrlapi.Resource;
import org.xbrlapi.RoleType;
import org.xbrlapi.SchemaDeclaration;
import org.xbrlapi.Stub;
import org.xbrlapi.Tuple;
import org.xbrlapi.XML;
import org.xbrlapi.cache.Cache;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.resource.DefaultMatcherImpl;
import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.data.resource.Matcher;
import org.xbrlapi.impl.FragmentComparator;
import org.xbrlapi.impl.RelationshipImpl;
import org.xbrlapi.impl.RelationshipOrderComparator;
import org.xbrlapi.impl.StubImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.networks.AllAnalyserImpl;
import org.xbrlapi.networks.Analyser;
import org.xbrlapi.networks.AnalyserImpl;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.NetworksImpl;
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
public abstract class BaseStoreImpl implements Store {

	private final static Logger logger = Logger.getLogger(BaseStoreImpl.class);

    /**
     * The DOM document used to construct DOM representations
     * of subtrees of documents in the store.
     */    
    transient protected Document storeDOM = null;

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
    protected HashMap<URI,String> namespaceBindings = new HashMap<URI,String>();    

    /**
     * @see org.xbrlapi.data.Store#setNamespaceBinding(URI,String)
     */
    public synchronized void setNamespaceBinding(URI namespace, String prefix) throws XBRLException {
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
    protected synchronized String getURIFilteringPredicate() {

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

        try {
            deleteDocument(uri);
        } catch (XBRLException e) {
            reason += " (NB: Failed to delete the document from the data store. " + e.getMessage()+")";
        }

        String documentId = getId(uri.toString() + "_stub");
        Stub stub = new StubImpl(documentId,uri,reason);
        persist(stub);
    }
    
    /**
     * Default implementation does nothing.
     * @see Store#sync()
     */
    public synchronized void sync() throws XBRLException {
    }

    /**
     * This implementation generates the document ID
     * with a prefix that is a random string of characters 
     * including a-z, A-Z and 0-9.
     * If, by chance, the random string has already been 
     * used for another document in the data store, then 
     * another random string is generated and this repeats until
     * the random string is unique in the data store.
     * @param input The string that may be used to generate the ID.
     * @see org.xbrlapi.data.Store#getDocumentId(URI)
     */    
    public String getId(String input) throws XBRLException {
        
        // The document is not in the data store so generate a new document ID.
        String randomString = random();
        while (this.hasXMLResource(randomString + "_1") || this.hasXMLResource(randomString)) {
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
     * Algorithm:
     * <ol>
     *  <li>Check if the matcher indicate that the URI has matching URIs.</li>
     *  <li>If the URI has matching URIs:
     *   <li>If the URI is the one used for the document in the data store:
     *    <li>Delete the URI from the matcher, getting the new matching URI back as
     *    a result of the deletion operation and update the document fragments to use 
     *    the new matching URI instead of the deleted URI.</li>
     *   </li>
     *   <li>Otherwise, just delete the URI from the matcher and we are done.</li>
     *  </li>
     *  <li>Otherwise, the URI does not have matching URIs so just delete the
     *  URI from the matcher and delete the relevant fragments from the data store.</li>
     * </ol>
	 * @see org.xbrlapi.data.Store#deleteDocument(URI)
     */
    public void deleteDocument(URI uri) throws XBRLException {

        logger.debug("Deleting " + uri + " from the data store.");
        URI matchURI = getMatcher().getMatch(uri);
        URI newMatchURI = getMatcher().delete(uri);
        
        String query = "for $fragment in #roots# where $fragment/@uri='"+ matchURI + "' return $fragment";

        if (newMatchURI == null) {
            Set<String> indices = this.queryForIndices(query);
            for (String index: indices) {
                remove(index);
            }
            return;
        } else if (matchURI.equals(newMatchURI)) {
            return;
        }

        Set<String> indices = this.queryForIndices(query);
        for (String index: indices) {
            Fragment fragment = this.<Fragment>getXMLResource(index);
            fragment.setURI(newMatchURI);
            persist(fragment);
        }

        // Eliminate any document stub
        Stub stub = this.getStub(uri);
        if (stub != null) this.remove(stub);
        
    }
    

 
    public void deleteRelatedDocuments(URI uri) throws XBRLException {
        
        HashMap<URI,Boolean> documentsToDelete = new HashMap<URI,Boolean>();    	
        
        deleteDocument(uri);
    	
        List<Fragment> fragments = this.<Fragment>queryForXMLResources("#roots#[@targetDocumentURI='"+ uri + "']");
    	
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
        String query = "#roots#[@targetDocumentURI='"+ uri + "']";
        List<Fragment> fragments = this.<Fragment>queryForXMLResources(query);

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
        String query = "#roots#[@uri='" + uri + "' and @targetDocumentURI]";
        List<Fragment> fragments = this.<Fragment>queryForXMLResources(query);

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
     * @see Store#getDocumentURIs()
     */
    public Set<URI> getDocumentURIs() throws XBRLException {

        Set<URI> uris = new HashSet<URI>();
        
        Set<String> uriStrings =  this.queryForStrings("for $root in #roots#[@parentIndex=''] return string($root/@uri)");

        try {
            for (String uriString: uriStrings) {
                uris.add(new URI(uriString));
            }
        } catch (URISyntaxException e) {
            throw new XBRLException("A document URI has invalid syntax.",e);
        }
        return uris;
    }

    /**
     * @see org.xbrlapi.data.Store#hasDocument(URI)
     */
    public boolean hasDocument(URI uri) throws XBRLException {
        URI matchURI = null;
        try {
            matchURI = getMatcher().getMatch(uri);
        } catch (XBRLException e) {
            logger.warn(uri + " could not be matched. " + e.getMessage());
            matchURI = uri;
        }
        String query = "for $root in #roots# where $root/@uri='" + matchURI + "' and $root/@parentIndex='' return string($root/@index)";
        Set<String> rootIndices = queryForStrings(query);
        if (rootIndices.size() == 1) return true;
        if (rootIndices.size() == 0) return false;
        throw new XBRLException("There are two root fragments in the store for " + uri);
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
        List<Fragment> fragments = queryForXMLResources("#roots#[@uri='" + matchURI + "' and @parentIndex='']");
        if (fragments.size() > 1) throw new XBRLException("More than one document was found in the data store.");
        if (fragments.size() == 0) throw new XBRLException("No documents were found in the data store.");
        Fragment fragment = fragments.get(0);
        Element document = this.getAnnotatedSubtree(fragment);
        document.setAttributeNS(Constants.CompNamespace.toString(),Constants.CompPrefix + ":index",fragment.getIndex());
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
	 * @throws XBRLException if the fragment is null.
	 */
	public Element getSubtree(Fragment f) throws XBRLException {

	    if (f == null) {
	        throw new XBRLException("The fragment must not be null.");
	    }
	    
		// Make sure that the DOM is initialised.
		if (storeDOM == null) {
			storeDOM = (new XMLDOMBuilder()).newDocument();
		}

		// Get the DOM representation of the fragment
		Element d = null;
		try {
		    d = (Element) storeDOM.importNode(f.getDataRootElement(), true);
		} catch (Exception e) {
		    throw new XBRLException("The data could not be plugged into the DOM for fragment " + f.getIndex(),e);
		}
		    
		// Get the child fragment IDs
		List<Fragment> unorderedFragments = this.queryForXMLResources("#roots#[@parentIndex='" + f.getIndex() + "']");
		
		// With no children, just return the fragment
		if (unorderedFragments.size() == 0) {
			return d;
		}

		// Sort the child fragments into insertion order
		TreeSet<Fragment> sortedFragments = new TreeSet<Fragment>(new FragmentComparator());
    	for (Fragment fragment: unorderedFragments) {
    		sortedFragments.add(fragment);
    	}
    	
    	// Iterate sorted child fragments in insertion order, inserting them
    	for (Fragment childFragment: sortedFragments) {

    		// Get XML DOM for child fragment
    		Element child = getSubtree(childFragment);

    		// Get parent element of child fragment in current fragment
    		Element parentElement = childFragment.getParentElement(d);

            // Do the fragment insertion
    		parentElement.appendChild(child);

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
		List<Fragment> fs = this.queryForXMLResources("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + f.getIndex() + "']");
		
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
	    	child.setAttributeNS(Constants.CompNamespace.toString(),Constants.CompPrefix + ":index",childFragment.getIndex());

    		// Get parent element of child fragment in current fragment
    		Element parentElement = childFragment.getParentElement(d);

    		// Do the fragment insertion
            parentElement.appendChild(child);
    		
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
		
    	Element root = storeDOM.createElementNS(Constants.XBRLAPINamespace.toString(),Constants.XBRLAPIPrefix + ":dts");
		
		Set<URI> uris = getDocumentURIs();
		for (URI uri: uris) {
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
    public Document getCompositeDocument() throws XBRLException {
    	
		if (storeDOM == null) {
			storeDOM = (new XMLDOMBuilder()).newDocument();
		}
		
    	Element root = storeDOM.createElementNS(Constants.CompNamespace.toString(),Constants.CompPrefix + ":dts");
		
		Set<URI> uris = getDocumentURIs();
		long counter = 1;
		for (URI uri: uris) {
	    	Element file = storeDOM.createElementNS(Constants.CompNamespace.toString(),Constants.CompPrefix + ":file");
	    	file.setAttributeNS(Constants.CompNamespace.toString(),Constants.CompPrefix + ":uri", uri.toString());
	    	file.setAttributeNS(Constants.CompNamespace.toString(),Constants.CompPrefix + ":index","composite_" + new Long(counter++).toString());
			root.appendChild(file);
			file.appendChild(getAnnotatedDocumentAsDOM(uri));
		}
    	root.setAttributeNS(Constants.CompNamespace.toString(),Constants.CompPrefix + ":index","composite_" + new Long(counter++).toString());
    	storeDOM.appendChild(root);
    	return storeDOM;
    	
    }
    

    

    
    /**
     * @see org.xbrlapi.data.Store#getStubs()
     */
    public List<Stub> getStubs() throws XBRLException {
        List<Stub> stubs = this.<Stub>getXMLResources("Stub");
        return stubs;
    }
    
    /**
     * @see org.xbrlapi.data.Store#getStub(URI)
     */
    public Stub getStub(URI uri) throws XBRLException {
        List<Stub> stubs = this.<Stub>queryForXMLResources("#roots#[@type='org.xbrlapi.impl.StubImpl' and @resourceURI='" + uri + "']");
        if (stubs.size() == 0) return null;
        if (stubs.size() > 1) throw new XBRLException("There are " + stubs.size() + " stubs for " + uri);
        return stubs.get(0);
    }
    
    /**
     * @see org.xbrlapi.data.Store#getStub(URI stubId)
     */
    public void removeStub(String stubId) throws XBRLException {
        if (hasXMLResource(stubId)) remove(stubId);
    }
    
    /**
     * @see org.xbrlapi.data.Store#remove(XML)
     */
    public void remove(XML xml) throws XBRLException {
        remove(xml.getIndex());
    }    
    
    
    /**
     * @see org.xbrlapi.data.Store#getDocumentsToDiscover()
     */
    public synchronized List<URI> getDocumentsToDiscover() throws XBRLException {
        List<Stub> stubs = getStubs();
        Vector<URI> list = new Vector<URI>();
        for (Stub stub: stubs) {
            list.add(stub.getResourceURI());
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
		
		Set<URI> uris = getDocumentURIs();
		Iterator<URI> iterator = uris.iterator();
		while (iterator.hasNext()) {			
			URI uri = iterator.next();
			if (uri.toString().startsWith(uriPrefix)) {
				Cache cache = new CacheImpl(destination);
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
     * @see org.xbrlapi.data.Store#getXMLResources(String)
     */
    public <F extends XML> List<F> getXMLResources(String interfaceName) throws XBRLException {
        String query = "for $root in #roots# where $root/@type='org.xbrlapi.impl." + interfaceName + "Impl' return $root";
        if (interfaceName.indexOf(".") > -1) {
            query = "for $root in #roots# where $root/@type='" + interfaceName + "' return $root";
        }
    	return this.<F>queryForXMLResources(query);
    }
    
    /**
     * @see org.xbrlapi.data.Store#getChildFragments(String, String)
     */
    public <F extends Fragment> List<F> getChildFragments(String interfaceName, String parentIndex) throws XBRLException {
    	return this.<F>queryForXMLResources("#roots#[@type='org.xbrlapi.impl." + interfaceName + "Impl' and @parentIndex='" + parentIndex + "']");
    }    
    

    
    /**
     * @see org.xbrlapi.data.Store#getNetworks()
     */
    public Networks getNetworks() throws XBRLException {

        Networks networks = new NetworksImpl(this);

        if (this.isPersistingRelationships()) {
            Analyser analyser = new AllAnalyserImpl(this);
            List<Relationship> relationships = analyser.getAllRelationships();
            networks.addRelationships(relationships);
            return networks;
        }
        
        // First get the set of arcs using the arc role
        Set<String> arcIndices = getArcIndices();
        for (String index: arcIndices) {
            Arc arc = this.getXMLResource(index);
            List<ArcEnd> sources = arc.getSourceFragments();
            List<ArcEnd> targets = arc.getTargetFragments();
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
                    networks.addRelationship(new RelationshipImpl(arc,s,t));
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
    	
        // First get the relevant extended links
        Set<URI> linkRoles = this.getLinkRoles(arcrole);
        
        for (URI linkRole: linkRoles) {
            Set<String> linkIndices = getExtendedLinkIndices(linkRole);
            for (String linkIndex: linkIndices) {

                Set<String> arcIndices = getArcIndices(arcrole,linkIndex);
                for (String index: arcIndices) {
                    Arc arc = this.getXMLResource(index);
                    List<ArcEnd> sources = arc.getSourceFragments();
                    List<ArcEnd> targets = arc.getTargetFragments();
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
                            networks.addRelationship(new RelationshipImpl(arc,s,t));
                        }
                    }
                }
            }
        }
        
        logger.info("network size = " + networks.getSize());
    	return networks;    	
    }
    
    /**
     * @see org.xbrlapi.data.Store#getNetworks(URI,URI)
     */
    public Networks getNetworks(URI linkRole, URI arcrole) throws XBRLException {

        Networks networks = new NetworksImpl(this);

        Set<String> linkIndices = getExtendedLinkIndices(linkRole);
        logger.info(linkIndices.size() + " extended links with given link and arc roles.");
        for (String linkIndex: linkIndices) {

            Set<String> arcIndices = getArcIndices(arcrole,linkIndex);
            logger.info(arcIndices.size() + " arcs with arcrole in given extended link.");
            for (String index: arcIndices) {
                Arc arc = this.getXMLResource(index);
                List<ArcEnd> sources = arc.getSourceFragments();
                List<ArcEnd> targets = arc.getTargetFragments();
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
                        networks.addRelationship(new RelationshipImpl(arc,s,t));
                    }
                }
            }
        } 
        
        logger.info("network size = " + networks.getSize());
        return networks;        
    }

    
    /**
     * @param arcrole The arcrole to use to identify the arcs to retrieve.
     * @return the list of arc fragments with a given arc role value.
     * @throws XBRLException
     */
    private List<Arc> getArcs(URI arcrole) throws XBRLException {
    	String query = "#roots#[*/*[@xlink:arcrole='" + arcrole + "' and @xlink:type='arc']]";
    	List<Arc> arcs = this.<Arc>queryForXMLResources(query);
    	logger.debug("#arcs with given arcrole = " + arcs.size());
    	return arcs;
    }
    
    /**
     * @see Store#getLabels(String, URI, URI, String)
     */
    public List<LabelResource> getLabels(String fragment, URI linkRole, URI resourceRole, String language) throws XBRLException {

        if (this.isPersistingRelationships()) {
            String query = "#roots#[@label and @sourceIndex='" + fragment + "'";
            if (linkRole != null) query += " and @linkRole='" + linkRole + "'"; 
            if (resourceRole != null) query += " and @targetRole='" + resourceRole + "'"; 
            if (language != null) query += " and @targetLanguage='" + language + "'"; 
            query += "]";
            List<Relationship> relationships = this.<Relationship>queryForXMLResources(query);
            List<LabelResource> labels = new Vector<LabelResource>();
            for (Relationship relationship: relationships) {
                labels.add(relationship.<LabelResource>getTarget());
            }
            return labels;
        }

        Networks labelNetworks = this.getNetworksFrom(fragment,linkRole,Constants.LabelArcrole);
        labelNetworks.addAll(this.getNetworksFrom(fragment,linkRole,Constants.GenericLabelArcrole));
        
        List<LabelResource> labels = new Vector<LabelResource>();
        for (Network network: labelNetworks) {
            for (Relationship relationship: network.getAllActiveRelationships()) {
                LabelResource label = (LabelResource) relationship.getTarget();
                String l = label.getLanguage();
                URI r = label.getResourceRole();
                if (resourceRole == null && language == null) labels.add(label);
                else if (resourceRole == null) if (label.getLanguage().equals(l)) labels.add(label); 
                else if (language == null) if (label.getResourceRole().equals(r)) labels.add(label); 
            }
        }
        return labels;
        
    }
    
    
    /**
     * @see Store#getLabels(String, URI, String)
     */
    public List<LabelResource> getLabels(String fragment, URI resourceRole, String language) throws XBRLException {
        return this.getLabels(fragment,null,resourceRole,language);
    }
    
    /**
     * @see Store#getLabels(String, String)
     */
    public List<LabelResource> getLabels(String fragment, String language) throws XBRLException {
        return this.getLabels(fragment,null,null,language);
    }
    
    /**
     * @see Store#getLabels(String)
     */
    public List<LabelResource> getLabels(String fragment) throws XBRLException {
        return this.getLabels(fragment,null,null,null);
    }    
    
    /**
     * @see Store#getLabels(String, URI)
     */
    public List<LabelResource> getLabels(String fragment, URI resourceRole) throws XBRLException {
        return this.getLabels(fragment,null,resourceRole,null);
    }
    
    /**
     * @see Store#getReferences(String, URI, URI, String)
     */
    public List<ReferenceResource> getReferences(String fragment, URI linkRole, URI resourceRole, String language) throws XBRLException {

        if (this.isPersistingRelationships()) {
            String query = "#roots#[@reference and @sourceIndex='" + fragment + "'";
            if (linkRole != null) query += " and @linkRole='" + linkRole + "'"; 
            if (resourceRole != null) query += " and @targetRole='" + resourceRole + "'"; 
            if (language != null) query += " and @targetLanguage='" + language + "'"; 
            query += "]";
            List<Relationship> relationships = this.<Relationship>queryForXMLResources(query);
            List<ReferenceResource> references = new Vector<ReferenceResource>();
            for (Relationship relationship: relationships) {
                references.add(relationship.<ReferenceResource>getTarget());
            }
            return references;
        }

        Networks referenceNetworks = this.getNetworksFrom(fragment,linkRole,Constants.ReferenceArcrole);
        referenceNetworks.addAll(this.getNetworksFrom(fragment,linkRole,Constants.GenericReferenceArcrole));
        
        List<ReferenceResource> references = new Vector<ReferenceResource>();
        for (Network network: referenceNetworks) {
            for (Relationship relationship: network.getAllActiveRelationships()) {
                ReferenceResource reference = (ReferenceResource) relationship.getTarget();
                String l = reference.getLanguage();
                URI r = reference.getResourceRole();
                if (resourceRole == null && language == null) references.add(reference);
                else if (resourceRole == null) if (reference.getLanguage().equals(l)) references.add(reference); 
                else if (language == null) if (reference.getResourceRole().equals(r)) references.add(reference); 
            }
        }
        return references;

    }
        
    /**
     * @see Store#getReferences(String, URI, String)
     */
    public List<ReferenceResource> getReferences(String fragment, URI resourceRole, String language) throws XBRLException {
        return this.getReferences(fragment,null,resourceRole,language);
    }
    
    /**
     * @see Store#getReferences(String, String)
     */
    public List<ReferenceResource> getReferences(String fragment, String language) throws XBRLException {
        return this.getReferences(fragment,null,null,language);
    }
    
    /**
     * @see Store#getReferences(String)
     */
    public List<ReferenceResource> getReferences(String fragment) throws XBRLException {
        return this.getReferences(fragment,null,null,null);
    }        
    
    /**
     * @see Store#getReferences(String, URI)
     */
    public List<ReferenceResource> getReferences(String fragment, URI resourceRole) throws XBRLException {
        return this.getReferences(fragment,null,resourceRole,null);
    }
    
    /**
     * @see Store#getArcs(URI, String)
     */
    public List<Arc> getArcs(URI arcrole, String linkIndex) throws XBRLException {
        String query = "#roots#[@parentIndex='" + linkIndex + "' and */*[@xlink:arcrole='" + arcrole + "' and @xlink:type='arc']]";
        List<Arc> arcs = this.<Arc>queryForXMLResources(query);
        logger.debug("#arcs with given arcrole in given extended link = " + arcs.size());
        return arcs;
    }
    
    /**
     * @see Store#getArcs(String)
     */
    public List<Arc> getArcs(String linkIndex) throws XBRLException {
        String query = "#roots#[@type='org.xbrlapi.impl.ArcImpl' and @parentIndex='" + linkIndex + "']";
        List<Arc> arcs = this.<Arc>queryForXMLResources(query);
        return arcs;
    }    
    
    /**
     * @see Store#getArcIndices(URI)
     */
    public Set<String> getArcIndices(URI arcrole) throws XBRLException {
        String query = "#roots#[*/*[@xlink:arcrole='" + arcrole + "' and @xlink:type='arc']]/@index";
        return this.queryForStrings(query);
    }
    
    /**
     * @param arcrole The arcrole to use to identify the arcs to retrieve.
     * @param linkIndex The index of the extended link containing the arcs to retrieve.
     * @return the list of indices of arcs matching the selection criteria.
     * @throws XBRLException
     */
    private Set<String> getArcIndices(URI arcrole, String linkIndex) throws XBRLException {
        String query = "#roots#[@parentIndex='" + linkIndex + "' and */*[@xlink:arcrole='" + arcrole + "' and @xlink:type='arc']]/@index";
        Set<String> indices = this.queryForStrings(query);
        return indices;
    }
    
    /**
     * @see Store#getArcIndices(String)
     */
    public Set<String> getArcIndices(String linkIndex) throws XBRLException {
        String query = "#roots#[@parentIndex='" + linkIndex + "' and */*/@xlink:type='arc']/@index";
        Set<String> indices = this.queryForStrings(query);
        return indices;
    }    
    
    /**
     * @see Store#getExtendedLinkIndices(URI)
     */
    public Set<String> getExtendedLinkIndices(URI linkRole) throws XBRLException {
        String query = "#roots#[*/*[@xlink:role='" + linkRole + "' and @xlink:type='extended']]/@index";
        Set<String> indices = this.queryForStrings(query);
        logger.debug("# extended links with given link role = " + indices.size());
        return indices;
    }
    

    
    
    
    /**
     * @return the list of indices of arcs in the data store.
     * @throws XBRLException
     */
    private Set<String> getArcIndices() throws XBRLException {
        String query = "#roots#[*/*[@xlink:type='arc']]/@index";
        return this.queryForStrings(query);
    }    

    /**
     * @see Store#getFragmentsFromDocument(URI, String)
     */
    public <F extends Fragment> List<F> getFragmentsFromDocument(URI uri, String interfaceName) throws XBRLException {
        URI matchURI = getMatcher().getMatch(uri);
        String query = "#roots#[@uri='"+ matchURI + "' and @type='org.xbrlapi.impl." + interfaceName + "Impl']";        
        if (interfaceName.indexOf(".") > -1) {
            query = "#roots#[@uri='"+ matchURI + "' and @type='" + interfaceName + "']";
        }
        return this.<F>queryForXMLResources(query);
    }
    
    /**
     * @see Store#getFragmentIndicesFromDocument(URI, String)
     */
    public Set<String> getFragmentIndicesFromDocument(URI uri, String interfaceName) throws XBRLException {
        URI matchURI = getMatcher().getMatch(uri);
        String query = "#roots#[@uri='"+ matchURI + "' and @type='org.xbrlapi.impl." + interfaceName + "Impl']";        
        if (interfaceName.indexOf(".") > -1) {
            query = "#roots#[@uri='"+ matchURI + "' and @type='" + interfaceName + "']";
        }
        Set<String> result = this.queryForIndices(query);
        return result;
    }
    
    /**
     * @see Store#getFragmentIndices(String)
     */
    public Set<String> getFragmentIndices(String interfaceName) throws XBRLException {
        long start = System.currentTimeMillis();
        String query = "#roots#[@type='org.xbrlapi.impl." + interfaceName + "Impl']";        
        if (interfaceName.indexOf(".") > -1) {
            query = "#roots#[@type='" + interfaceName + "']";
        }
        query += "/@index";
        Set<String> result = this.queryForStrings(query);
        Double seconds = new Double(System.currentTimeMillis() - start) / new Double(1000);
        logger.info("Seconds required = " + seconds);
        return result;
    }    
    

    

    
    /**
     * @return a list of all of the root-level facts in the data store (those facts
     * that are children of the root element of an XBRL instance).  Returns an empty list 
     * if no facts are found.
     * @throws XBRLException
     */
    public List<Fact> getFacts() throws XBRLException {
    	List<Instance> instances = this.<Instance>getXMLResources("Instance");
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
    	List<Instance> instances = this.<Instance>getFragmentsFromDocument(uri,"Instance");
    	return this.getFactsFromInstances(instances);
    }
    

    


    /**
     * @see org.xbrlapi.data.Store#getRootFragmentForDocument(URI)
     */
    public <F extends Fragment> F getRootFragmentForDocument(URI uri) throws XBRLException {
    	List<F> fragments = this.<F>queryForXMLResources("#roots#[@uri='" + uri + "' and @parentIndex='']");
    	if (fragments.size() == 0) return null;
    	if (fragments.size() > 1) throw new XBRLException("Two fragments identify themselves as roots of the one document.");
    	return fragments.get(0);
    }

    
    /**
     * @see org.xbrlapi.data.Store#getRootFragments()
     */
    public <F extends Fragment> List<F> getRootFragments() throws XBRLException {
    	List<F> roots =  this.<F>queryForXMLResources("#roots#[@uri and @parentIndex='']");
    	return roots;
    }

    /**
     * @see org.xbrlapi.data.Store#getLanguage(String, String)
     */
    public Language getLanguage(String encoding, String code) throws XBRLException {
        if (encoding == null) throw new XBRLException("The language code must not be null.");
        if (code == null) throw new XBRLException("The language name encoding must not be null.");
        String query = "#roots#[@type='org.xbrlapi.impl.LanguageImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/lang:language/lang:encoding='" + encoding.toUpperCase() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/lang:language/lang:code='" + code.toUpperCase() + "']";
        List<Language> languages = this.<Language>queryForXMLResources(query);
        if (languages.size() == 0) return null;
        return languages.get(0);
    }
    
    
    
    /**
     * @see org.xbrlapi.data.Store#getLanguages(String)
     */
    public List<Language> getLanguages(String code) throws XBRLException {
        if (code == null) throw new XBRLException("The language code must not be null.");
        String query = "#roots#[@type='org.xbrlapi.impl.LanguageImpl' and */lang:language/lang:code='" + code.toUpperCase() + "']";
        return this.<Language>queryForXMLResources(query);
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
        List<Instance> instances = this.<Instance>getXMLResources("Instance");
        return getItemsFromInstances(instances);
    }
    
    /**
     * @return a list of all of the tuples in the data store.
     * @throws XBRLException
     */
    public List<Tuple> getTuples() throws XBRLException {
        List<Instance> instances = this.<Instance>getXMLResources("Instance");
        return this.getTuplesFromInstances(instances);
    }


    
    /**
     * @param uri The URI of the document to get the items from.
     * @return a list of all of the root-level items in the data store.
     * @throws XBRLException
     */
    public List<Item> getItems(URI uri) throws XBRLException {
        List<Instance> instances = this.<Instance>getFragmentsFromDocument(uri,"Instance");
        return this.getItemsFromInstances(instances);
    }
    
    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level tuples in the specified document.
     * @throws XBRLException
     */
    public List<Tuple> getTuples(URI uri) throws XBRLException {
        List<Instance> instances = this.<Instance>getFragmentsFromDocument(uri,"Instance");
        return this.getTuplesFromInstances(instances);
    }



    /**
     * @see Store#getNetworkRoots(URI, URI)
     */
    public <F extends Fragment> Set<F> getNetworkRoots(URI linkRole, URI arcrole) throws XBRLException {
        
        if (this.isPersistingRelationships()) {
            return getAnalyser().<F>getRoots(linkRole,arcrole);
        }
        
        // Get the links that contain the network declaring arcs.
        String linkQuery = "#roots#[@type='org.xbrlapi.impl.ExtendedLinkImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*[@xlink:role='" + linkRole + "']]";
        List<ExtendedLink> links = this.<ExtendedLink>queryForXMLResources(linkQuery);
        
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
                    List<ArcEnd> targets = arc.getTargetFragments();
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
        Set<F> roots = new TreeSet<F>();
        for (String id: sourceIds.keySet()) {
            if (! targetIds.containsKey(id)) {
                roots.add(this.<F>getXMLResource(id));
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
        
        List<SchemaDeclaration> candidates = this.<SchemaDeclaration>queryForXMLResources("#roots#[*/xsd:element[@name='" + name + "']]");
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
        return this.<RoleType>getXMLResources("RoleType");
    }
    
    /**
     * @see org.xbrlapi.data.Store#getRoleTypes(URI)
     */
    public List<RoleType> getRoleTypes(URI uri) throws XBRLException {
        String query = "#roots#["+ Constants.XBRLAPIPrefix+ ":" + "data/link:roleType/@roleURI='" + uri + "']";
        return this.<RoleType>queryForXMLResources(query);
    }    
    
    /**
     * @return a list of ArcroleType fragments
     * @throws XBRLException
     */
    public List<ArcroleType> getArcroleTypes() throws XBRLException {
        return this.<ArcroleType>getXMLResources("ArcroleType");
    }
    
    /**
     * @return a list of arcroleType fragments that define a given arcrole.
     * @throws XBRLException
     */
    public List<ArcroleType> getArcroleTypes(URI uri) throws XBRLException {
        String query = "#roots#["+ Constants.XBRLAPIPrefix+ ":" + "data/link:arcroleType/@arcroleURI='" + uri + "']";
        return this.<ArcroleType>queryForXMLResources(query);
    }
    
    /**
     * @see org.xbrlapi.data.Store#getResourceRoles()
     */
    public List<URI> getResourceRoles() throws XBRLException {
        HashMap<URI,String> roles = new HashMap<URI,String>();
        List<Resource> resources = this.<Resource>queryForXMLResources("#roots#["+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:type='resource']");
        for (Resource resource: resources) {
            URI role = resource.getResourceRole();
            if (! roles.containsKey(role)) roles.put(role,"");
        }
        List<URI> result = new Vector<URI>();
        result.addAll(roles.keySet());
        return result;
    }    


    /**
     * @see org.xbrlapi.data.Store#getMinimumDocumentSet(URI)
     */
    public List<URI> getMinimumDocumentSet(URI uri) throws XBRLException {
        List<URI> starters = new Vector<URI>();
        starters.add(uri);
        return this.getMinimumDocumentSet(starters);
    }
    
    /**
     * @see org.xbrlapi.data.Store#getMinimumDocumentSet(List)
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
     * @see Store#getExtendedLinks(URI)
     */
    public List<ExtendedLink> getExtendedLinks(URI linkrole) throws XBRLException {
        String query = "#roots#[*/*[@xlink:type='extended' and @xlink:role='" + linkrole + "']]";
        List<ExtendedLink> links = this.<ExtendedLink>queryForXMLResources(query);
        return links;
    }




    /**
     * @see Store#getMinimalNetworksWithArcrole(Fragment, URI)
     */
    public synchronized Networks getMinimalNetworksWithArcrole(Fragment fragment, URI arcrole) throws XBRLException {
        Set<Fragment> set = new HashSet<Fragment>();
        set.add(fragment);
        return this.getMinimalNetworksWithArcrole(set,arcrole);
    }    
    
    /**
     * @see Store#getMinimalNetworksWithArcrole(Set, URI)
     */
    public synchronized Networks getMinimalNetworksWithArcrole(Set<Fragment> fragments, URI arcrole) throws XBRLException {
        
        Analyser backup = getAnalyser();
        if (backup!= null) setAnalyser(new AnalyserImpl(this));

        try {
            Networks networks = new NetworksImpl(this);
    
            Set<Fragment> processedFragments = new HashSet<Fragment>();
            
            for (Fragment fragment: fragments) {
                networks = augmentNetworksForFragment(fragment,arcrole,networks,processedFragments);
            }
            return networks;
        } catch (Throwable t) {        
            if (backup != null) this.setAnalyser(backup);
            throw new XBRLException("There was a problem getting minimal networks with arcrole " + arcrole,t);
        }
    }
    
    /**
     * This method is recursive.
     * @param fragment The fragment to use as the target for the relationships to be added to the networks.
     * @param arcrole The arcrole for the networks to augment.
     * @param networks The networks system to augment.
     * @return The networks after augmentation.
     * @throws XBRLException
     */
    private synchronized Networks augmentNetworksForFragment(Fragment fragment, URI arcrole, Networks networks, Set<Fragment> processedFragments) throws XBRLException {
        if (processedFragments.contains(fragment)) {
            return networks;
        }
        processedFragments.add(fragment);
        
        for (Relationship relationship: this.getRelationshipsTo(fragment.getIndex(),null,arcrole)) {
            networks.addRelationship(relationship);
        }
        for (Network network: networks.getNetworks(arcrole)) {
            SortedSet<Relationship> activeRelationships = network.getActiveRelationshipsTo(fragment.getIndex());
            logger.debug(fragment.getIndex() + " has " + activeRelationships.size() + " parent fragments.");
            for (Relationship activeRelationship: activeRelationships) {
                Fragment source = activeRelationship.getSource();
                networks = augmentNetworksForFragment(source,arcrole,networks,processedFragments);
            }
        }
        return networks;
    }

    /**
     * @see org.xbrlapi.data.Store#getArcroles()
     */
    public Set<URI> getArcroles() throws XBRLException {
        String query = "#roots#/*/*[@xlink:type='arc']/@xlink:arcrole";
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
     * @see org.xbrlapi.data.Store#getLinkRoles()
     */
    public Set<URI> getLinkRoles() throws XBRLException {
        String query = "#roots#[@type='org.xbrlapi.impl.ExtendedLinkImpl']/*/*/@xlink:role";
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
     * @see org.xbrlapi.data.Store#getLinkRoles(URI)
     */
    public Set<URI> getLinkRoles(URI arcrole) throws XBRLException {
        
        String query = "#roots#[*/*[@xlink:type='arc' and @xlink:arcrole='" + arcrole + "']]/@parentIndex";
        Set<String> linkIndices = this.queryForStrings(query);
        Set<URI> linkRoles = new TreeSet<URI>();
        for (String index: linkIndices) {
            ExtendedLink link = this.getXMLResource(index);
            linkRoles.add(link.getLinkRole());
        }
        return linkRoles;
    }
    
    /**
     * @see org.xbrlapi.data.Store#getArcroles(URI)
     */
    public Set<URI> getArcroles(URI linkRole) throws XBRLException {
        
        String query = "#roots#[@type='org.xbrlapi.impl.ExtendedLinkImpl' and */*/@xlink:role='" + linkRole + "']/@index";
        Set<String> linkIndices = this.queryForStrings(query);
        Set<String> arcroles = new TreeSet<String>();
        for (String linkIndex: linkIndices) {
            query = "#roots#[@parentIndex='" + linkIndex + "' and */*/@xlink:type='arc']/*/*/@xlink:arcrole";
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
        
        SortedSet<Relationship> relationships = this.getRelationshipsFrom(sourceIndex,linkRole,arcrole);
        Set<F> targets = new HashSet<F>();
        for (Relationship relationship: relationships) {
            try {
                targets.add((F) relationship.getTarget());
            } catch (ClassCastException e) {
                throw new XBRLException("A target fragment is of the wrong type: " + relationship.getTarget().getType(),e);
            }
        }
        return new Vector<F>(targets);
        
    }
    
    /**
     * @see Store#getSources(String, URI, URI)
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> List<F> getSources(String targetIndex, URI linkRole, URI arcrole) throws XBRLException {
        
        SortedSet<Relationship> relationships = this.getRelationshipsTo(targetIndex,linkRole,arcrole);
        Set<F> sources = new HashSet<F>();
        for (Relationship relationship: relationships) {
            try {
                sources.add((F) relationship.getSource());
            } catch (ClassCastException e) {
                throw new XBRLException("A source fragment is of the wrong type: " + relationship.getSource().getType(),e);
            }
        }
        return new Vector<F>(sources);        

    }    
    
    /**
     * @see Store#getRelationshipsFrom(String,URI,URI)
     */
    public SortedSet<Relationship> getRelationshipsFrom(String sourceIndex,URI linkRole, URI arcrole) throws XBRLException {

        if (this.isPersistingRelationships()) {
            Analyser analyser = this.getAnalyser();
            return analyser.getRelationshipsFrom(sourceIndex,linkRole,arcrole);
        }

        SortedSet<Relationship> relationships = new TreeSet<Relationship>(new RelationshipOrderComparator());
        Networks networks = this.getNetworksFrom(sourceIndex,linkRole,arcrole);
        for (Network network: networks) {
            relationships.addAll(network.getActiveRelationshipsFrom(sourceIndex));
        }
        return relationships;
    }
    
    /**
     * @see Store#hasAllRelationships(URI)
     */
    public boolean hasAllRelationships(URI document) throws XBRLException {

        if (this.isPersistingRelationships()) {
            Analyser analyser = this.getAnalyser();
            return analyser.hasAllRelationships(document);
        }
        return false;

    }    
    

    
    /**
     * If using persisted relationships then the set of relationships used to 
     * generate the results can be modified by appropriate choice of 
     * @link org.xbrlapi.networks.Analyser implementation.  Otherwise only active
     * relationships are used (those that are not prohibited or over-ridden).
     * @see Store#getRelationshipsTo(String,URI,URI)
     */
    public SortedSet<Relationship> getRelationshipsTo(String targetIndex,URI linkRole, URI arcrole) throws XBRLException {

        if (this.isPersistingRelationships()) {
            Analyser analyser = this.getAnalyser();
            return analyser.getRelationshipsTo(targetIndex,linkRole,arcrole);
        }
        SortedSet<Relationship> relationships = new TreeSet<Relationship>(new RelationshipOrderComparator());
        Networks networks = this.getNetworksTo(targetIndex,linkRole,arcrole);
        for (Network network: networks) {
            relationships.addAll(network.getActiveRelationshipsTo(targetIndex));
        }
        
        return relationships;
    }    
    
    /**
     * @see Store#getNetworksFrom(String,URI,URI)
     */
    public Networks getNetworksFrom(String sourceIndex,URI linkRole, URI arcrole) throws XBRLException {

        Networks networks = new NetworksImpl(this);

        if (this.isPersistingRelationships()) {
            Analyser analyser = new AllAnalyserImpl(this);
            SortedSet<Relationship> relationships = analyser.getRelationshipsFrom(sourceIndex,linkRole,arcrole);
            networks.addRelationships(relationships);
            return networks;
        }
        
        Fragment source = this.getXMLResource(sourceIndex);

        Relationship relationship = null;

        // If we have a resource, it could be related directly via arcs to relatives.
        if (source.isa("org.xbrlapi.impl.ResourceImpl")) {
            if ((linkRole == null) || ((Resource) source).getExtendedLink().getLinkRole().equals(linkRole)) {
                
                List<Arc> arcs = null;
                if (arcrole == null) arcs = ((ArcEnd) source).getArcsFrom();
                else  arcs = ((ArcEnd) source).getArcsFromWithArcrole(arcrole);

                for (Arc arc: arcs) {
                    List<ArcEnd> targets = arc.getTargetFragments();
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
                List<ArcEnd> targets = arc.getTargetFragments();
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
        
        Networks networks = new NetworksImpl(this);

        if (this.isPersistingRelationships()) {
            Analyser analyser = new AllAnalyserImpl(this);
            SortedSet<Relationship> relationships = analyser.getRelationshipsTo(targetIndex,linkRole,arcrole);
            networks.addRelationships(relationships);
            return networks;
        }

        Fragment target = this.getXMLResource(targetIndex);

        Relationship relationship = null;

        // If we have a resource, it could be related directly via arcs to relatives.
        if (target.isa("org.xbrlapi.impl.ResourceImpl")) {
            if ((linkRole == null) || ((Resource) target).getExtendedLink().getLinkRole().equals(linkRole)) {
                List<Arc> arcs = null;
                if (arcrole == null) arcs = ((ArcEnd) target).getArcsTo();
                else  arcs = ((ArcEnd) target).getArcsToWithArcrole(arcrole);

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
    transient private Analyser analyser = null;
    
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
     * @see Store#isPersistingRelationships()
     */
    public boolean isPersistingRelationships() {
        return (analyser != null);
    }

    /**
     * @see org.xbrlapi.data.Store#getNetworksFrom(java.lang.String, java.net.URI)
     */
    public Networks getNetworksFrom(String sourceIndex, URI arcrole) throws XBRLException {
        return getNetworksFrom(sourceIndex,null,arcrole);       
    }
    
    /**
     * @see org.xbrlapi.data.Store#getNetworksFrom(java.lang.String)
     */
    public Networks getNetworksFrom(String sourceIndex) throws XBRLException {
        return getNetworksFrom(sourceIndex,null,null);       
    }    

    /**
     * @see org.xbrlapi.data.Store#getNetworksTo(java.lang.String, java.net.URI)
     */
    public Networks getNetworksTo(String targetIndex, URI arcrole) throws XBRLException {
        return getNetworksTo(targetIndex,null,arcrole);
    }
    
    /**
     * @see org.xbrlapi.data.Store#getNetworksTo(java.lang.String)
     */
    public Networks getNetworksTo(String targetIndex) throws XBRLException {
        return getNetworksTo(targetIndex,null,null);
    }
    
 
    /**
     * @see Store#getMissingDocumentURIs()
     */
    public Set<URI> getMissingDocumentURIs() throws XBRLException {
        Set<URI> result = new HashSet<URI>();
        String query = "for $targetURI in #roots#[*/*[@xlink:type='locator' or @xlink:type='simple'], return distinct-values(string($fragment/@targetDocumentURI))";
        Set<String> uris = this.queryForStrings(query);
        for (String uri: uris) {
            try {
                result.add(new URI(uri));
            } catch (URISyntaxException e) {
                throw new XBRLException(uri + " has invalid syntax.",e);
            }
        }
        Set<URI> storedURIs = this.getDocumentURIs();
        result.removeAll(storedURIs);
        return result;
    }

    /**
     * This property is used to co-ordinate the document
     * loading activities of loaders that are operating in
     * parallel on the one data store.  It is used to 
     * prevent the same document from being simultaneously
     * loaded by several of the loaders.
     */
    transient private HashMap<URI,Loader> loadingRights = new HashMap<URI,Loader>();
    
    /**
     * @see Store#requestLoadingRightsFor(Loader, URI)
     */
    public synchronized boolean requestLoadingRightsFor(Loader loader, URI document) throws XBRLException {
        if (document == null) throw new XBRLException("Cannot start loading a document with a null URI");
        if (! loadingRights.containsKey(document)) {
            loadingRights.put(document,loader);
            return true;
        }
        if (loadingRights.get(document).equals(loader)) {
            return true;
        }
        logger.debug("Two loaders have been seeking loading rights for " + document);
        return false;
    }

    /**
     * @see Store#recindLoadingRightsFor(Loader, URI)
     */
    public synchronized void recindLoadingRightsFor(Loader loader, URI document) {
        if (document == null) return;
        if (! loadingRights.containsKey(document)) return;
        if (loadingRights.get(document).equals(loader)) loadingRights.remove(document);
    }

    protected void finalize() throws Throwable {
        try {
            sync();
            close();
        } finally {
            super.finalize();
        }
    }

    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        try {
            matcher = (Matcher) in.readObject();
            
            if (in.readBoolean()) {
                analyser = new AnalyserImpl(this);
            } else {
                analyser = null;
            }
            
            if (in.readBoolean()) {
                int urisSize = in.readInt();
                if (urisSize > 0) {
                    uris = new Vector<URI>();
                    for (int i=0; i<urisSize; i++) {
                        uris.add((URI) in.readObject());
                    }
                }
            } else {
                uris = null;
            }
            
            int bindings = in.readInt();
            namespaceBindings = new HashMap<URI,String>();
            for (int i=0; i<bindings; i++) {
                namespaceBindings.put((URI) in.readObject(), (String) in.readObject());
            }
            
        } catch (XBRLException e) {
            throw new IOException("The data store could not be read.",e);
        }
        
        
        // Reload fragmentMap
        //fragmentMap = new HashMap<String,Element>();
        
        // Reload indexMap
        //indexMap = new HashMap<Element,String>();
        
    }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject( );
        out.writeObject(matcher);
        out.writeBoolean(this.analyser != null);
        out.writeBoolean(uris != null);
        if (uris != null) {
            out.writeInt(uris.size());
            for (URI uri: uris) {
                out.writeObject(uri);
            }
        }
        out.writeInt(namespaceBindings.keySet().size());
        for (URI uri: namespaceBindings.keySet()) {
            out.writeObject(uri);
            out.writeObject(namespaceBindings.get(uri));
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((analyser == null) ? 0 : analyser.hashCode());
        result = prime * result + ((matcher == null) ? 0 : matcher.hashCode());
        result = prime
                * result
                + ((namespaceBindings == null) ? 0 : namespaceBindings
                        .hashCode());
        result = prime * result + ((uris == null) ? 0 : uris.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseStoreImpl other = (BaseStoreImpl) obj;
        if (matcher == null) {
            if (other.matcher != null)
                return false;
        } else if (matcher instanceof InStoreMatcherImpl) {
            // Avoid using Matcher.equals() test to prevent infinite recursion.
            if (! (other.matcher instanceof InStoreMatcherImpl)) return false;
            if (((InStoreMatcherImpl) matcher).getStore() != this) return false;
            if (((InStoreMatcherImpl) other.matcher).getStore() != other) return false;
        }
        if (namespaceBindings == null) {
            if (other.namespaceBindings != null)
                return false;
        } else if (!namespaceBindings.equals(other.namespaceBindings))
            return false;
        if (uris == null) {
            if (other.uris != null)
                return false;
        } else if (!uris.equals(other.uris))
            return false;
        return true;
    }
    


}
