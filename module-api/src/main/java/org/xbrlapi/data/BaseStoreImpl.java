package org.xbrlapi.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.NetworksImpl;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.networks.RelationshipImpl;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Instance;
import org.xbrlapi.Item;
import org.xbrlapi.Language;
import org.xbrlapi.Locator;
import org.xbrlapi.Tuple;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.resource.DefaultMatcherImpl;
import org.xbrlapi.data.resource.Matcher;
import org.xbrlapi.impl.FragmentComparator;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.impl.MockFragmentImpl;
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
    public void setMatcher(Matcher matcher) throws XBRLException {
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
    public void setNamespaceBinding(String namespace, String prefix) throws XBRLException {
        this.namespaceBindings.put(namespace,prefix);
    }

    /**
     * List of URLs to use when filtering query results to only get matches
     * to a specific set of documents.
     */
    private List<String> urls = null;

    /**
     * @see org.xbrlapi.data.Store#setFilteringURLs(List)
     */
    public void setFilteringURLs(List<String> urls) {
        this.urls = urls;
    }
    
    /**
     * @see org.xbrlapi.data.Store#getFilteringURLs()
     */
    public List<String> getFilteringURLs() {
        return this.urls;
    }    
    
    /**
     * @see org.xbrlapi.data.Store#clearFilteringURLs()
     */
    public void clearFilteringURLs() {
        this.urls = null;
    }

    /**
     * @see org.xbrlapi.data.Store#isFilteringByURLs()
     */
    public boolean isFilteringByURLs() {
        if (this.urls == null) return false;
        return true;
    }
    
    /**
     * @return an X Query clause that restricts the set of fragments returned by 
     * a query to those from a specific set of URLs.
     */
    protected String getURLFilteringQueryClause() {

        if (isFilteringByURLs()) {
            String urlFilter = "0";
            for (String url: this.getFilteringURLs()) {
                urlFilter = urlFilter + " or @url='" + url + "'";
            }
            urlFilter = "[" + urlFilter + "]";
            logger.debug(urlFilter);
            return urlFilter;
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
     * @see org.xbrlapi.data.Store#storeLoaderState(List)
     */
    public void storeLoaderState(List<String> documents) throws XBRLException {
        try {
            for (String document: documents) {
                storeStub(document);
            }
        } catch (XBRLException e) {
            throw new XBRLException("The loader state could not be stored.",e);
        }
    }

    /**
     * @see org.xbrlapi.data.Store#storeStub(String)
     */
    public void storeStub(String document) throws XBRLException {

        if (this.hasDocument(document)) return;
        if (this.getStub(document) != null) return;
        
        String documentId = getDocumentId(document);
        Fragment stub = new MockFragmentImpl(documentId);
        stub.setFragmentIndex(documentId);
        stub.setURL(document);
        stub.setMetaAttribute("stub","");
        this.storeFragment(stub);
    }

    /**
     * @see org.xbrlapi.data.Store#getDocumentId(String)
     */    
    public String getDocumentId(String document) throws XBRLException {

        Fragment stub = getStub(document);
        if (stub != null) {
            return stub.getFragmentIndex();
        }
        int i = 1;
        String iString = (new Integer(i)).toString();
        String randomString = random();
        while (this.hasFragment(randomString + "_" + iString + "_1")) {
            i++;
            iString = (new Integer(i)).toString();
        }
        return (randomString + "_" + iString);

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
    public void serialize(Fragment fragment) throws XBRLException {
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
	  * @see org.xbrlapi.data.Store#deleteDocument(String)
     */
    public void deleteDocument(String url) throws XBRLException {
    	try {
    	    URL matchURL = this.getMatcher().getMatch(new URL(url));
    	    String query = "/*[@url='"+ matchURL + "']";
            FragmentList<Fragment> fragments = this.<Fragment>query(query);      
            for (Fragment fragment: fragments) {
                this.removeFragment(fragment.getFragmentIndex());
            }
    	} catch (MalformedURLException e) {
    	    throw new XBRLException("Malformed URL.",e);
    	}
    }
    

    /**
	  * @see org.xbrlapi.data.Store#deleteRelatedDocuments(String)
	  */
	private static HashMap<String,Boolean> documentsToDelete = new HashMap<String,Boolean>(); 
    public void deleteRelatedDocuments(String url) throws XBRLException {
    	deleteDocument(url);
    	FragmentList<Fragment> fragments = this.<Fragment>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@targetDocumentURL='"+ url + "']");
    	for (Fragment fragment: fragments) {
            if (! documentsToDelete.containsKey(fragment.getURL())) {
                documentsToDelete.put(fragment.getURL(),new Boolean(true));
            }
    		Iterator<String> iterator = documentsToDelete.keySet().iterator();
    		while (iterator.hasNext()) {
    			String myURL = iterator.next();
    			if (documentsToDelete.get(myURL)) {
        			deleteRelatedDocuments(myURL);
        			documentsToDelete.put(myURL,new Boolean(false));
    			}
    		}
    	}
    }
    
    /**
     * @see org.xbrlapi.data.Store.getReferencingDocuments(String)
     */
    public List<String> getReferencingDocuments(String url) throws XBRLException {
        String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@targetDocumentURL='"+ url + "']";
        FragmentList<Fragment> fragments = this.<Fragment>query(query);

        List<String> urls = new Vector<String>();
        HashMap<String,String> map = new HashMap<String,String>(); 

        for (Fragment fragment: fragments) {
            String doc = fragment.getURL();
            if (!map.containsKey(doc)) {
                map.put(doc,"");
                urls.add(doc);
            }
        }
        
        return urls;
    }
    
    /**
     * @see org.xbrlapi.data.Store.getReferencedDocuments(String)
     */
    public List<String> getReferencedDocuments(String url) throws XBRLException {
        String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + url + "' and @targetDocumentURL]";
        FragmentList<Fragment> fragments = this.<Fragment>query(query);

        List<String> urls = new Vector<String>();
        HashMap<String,String> map = new HashMap<String,String>(); 

        for (Fragment fragment: fragments) {
            String target = fragment.getMetaAttribute("targetDocumentURL");
            if (!map.containsKey(target)) {
                map.put(target,"");
                urls.add(target);
            }
        }
        
        return urls;
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
     * Get a list of the URLs that have been stored.
     * @return a list of the URLs in the data store.
     * @throws XBRLException if the list cannot be constructed.
     */
    public List<String> getStoredURLs() throws XBRLException {
    	LinkedList<String> urls = new LinkedList<String>();
    	FragmentList<Fragment> rootFragments = this.query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='none']");
    	for (int i=0; i<rootFragments.getLength(); i++) {
    		urls.add(rootFragments.getFragment(i).getURL());
    	}
    	return urls;
    }

    /**
     * @see org.xbrlapi.data.Store#hasDocument(String)
     */
    public boolean hasDocument(String url) throws XBRLException {
        try {
            URL matchURL = getMatcher().getMatch(new URL(url));
            FragmentList<Fragment> rootFragments = this.<Fragment>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + matchURL + "' and @parentIndex='none']");
            return (rootFragments.getLength() > 0) ? true : false;
        } catch (MalformedURLException e) {
            throw new XBRLException("Malformed URL.",e);
        }
    }

    
    /**
     * Get a single document in the store as a DOM.  Note that this will
     * not reflect the original document in some ways.  Importantly, 
     * entities will be resolved and document type declarations will be missing.
     * Document encodings may also differ.  If the original document is required,
     * simply use the supplied URL to get a copy of the original document.
     * @param url The string representation of the URL of the 
     * document to be retrieved.
     * @return a DOM Document containing the XML representation of the
     * file at the specified URL.  Returns null if the store does not
     * contain a document with the given URL.
     * @throws XBRLException if the document cannot be constructed as a DOM.
     */
    public Element getDocumentAsDOM(String url) throws XBRLException {
    	return getSubtree(this.getRootFragmentForDocument(url));
    }
	
	/**
     * Get a single document in the store as a DOM including annotations.
     * @param url The string representation of the URL of the 
     * document to be retrieved.
     * @return an annotated DOM Document containing the XML representation of the
     * file at the specified URL.  Returns null if the store does not
     * contain a document with the given URL.
     * @throws XBRLException if more or less than one document is found in the store matching 
     * the supplied URL.
     */
    private Element getAnnotatedDocumentAsDOM(String url) throws XBRLException {
        try {
            URL matchURL = getMatcher().getMatch(new URL(url));
            FragmentList<Fragment> fragments = query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + matchURL + "' and @parentIndex='none']");
            if (fragments.getLength() > 1) throw new XBRLException("More than one document was found in the data store.");
            if (fragments.getLength() == 0) throw new XBRLException("No documents were found in the data store.");
            Fragment fragment = fragments.getFragment(0);
            Element document = this.getAnnotatedSubtree(fragment);
            document.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":index",fragment.getFragmentIndex());
            return document;
        } catch (MalformedURLException e) {
            throw new XBRLException("Malformed URL.",e);
        }
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
			storeDOM = XMLDOMBuilder.newDocument();
		}

		// Get the DOM representation of the fragment
		Element d = null;
		try {
		    d = (Element) storeDOM.importNode(f.getDataRootElement(), true);
		} catch (Exception e) {
		    f.getStore().serialize(f.getMetadataRootElement());
		    throw new XBRLException("The data could not be plugged into the DOM for fragment " + f.getFragmentIndex(),e);
		}
		    
		// Get the child fragment IDs
		FragmentList<Fragment> fs = this.query("/"+ Constants.XBRLAPIPrefix + ":" + "fragment[@parentIndex='" + f.getFragmentIndex() + "']");
		
		// With no children, just return the fragment
		if (fs.getLength() == 0) {
			return d;

		}

		// Sort the child fragments into insertion order
		Comparator<Fragment> comparator = new FragmentComparator();
		TreeSet<Fragment> fragments = new TreeSet<Fragment>(comparator);
    	for (int i=0; i<fs.getLength(); i++) {
    		fragments.add(fs.getFragment(i));
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
		
    	//logger.debug((new Date()) + ":Getting fragment " + f.getFragmentIndex());
    	
		// Make sure that the DOM is initialised.
		if (storeDOM == null) {
			storeDOM = XMLDOMBuilder.newDocument();
		}

		// Get the DOM representation of the fragment
		Element d = (Element) storeDOM.importNode(f.getDataRootElement(), true);
		
		// Get the child fragment IDs
		FragmentList<Fragment> fs = this.query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + f.getFragmentIndex() + "']");
		
		// With no children, just return the fragment
		if (fs.getLength() == 0) {
			return d;

		}

		// Sort the child fragments into insertion order
		TreeSet<Fragment> fragments = new TreeSet<Fragment>(new FragmentComparator());
		for (int i=0; i<fs.getLength(); i++) {
    		fragments.add(fs.getFragment(i));
    	}
    	
    	// Iterate child fragments in insertion order, inserting them
    	Iterator<Fragment> iterator = fragments.iterator();
    	while (iterator.hasNext()) {
    		
    		// Get child fragment
    		Fragment childFragment = iterator.next();

    		// Get XML DOM for child fragment using recursion
    		Element child = getAnnotatedSubtree(childFragment);
	    	child.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":index",childFragment.getFragmentIndex());

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
     * simply use the URLs captured in the data store to get a copy of the original 
     * document.  @see org.xbrlapi.data.Store#getStoredURLs() for more details.
     * Get all data in the store as a single XML DOM object.
     * @return the XML DOM representation of the XBRL information in the 
     * data store.
     * @throws XBRLException if the DOM cannot be constructed.
     */
    public Document getStoreAsDOM() throws XBRLException {

		if (storeDOM == null) {
			storeDOM = XMLDOMBuilder.newDocument();
		}
		
    	Element root = storeDOM.createElementNS(Constants.XBRLAPINamespace,Constants.XBRLAPIPrefix + ":dts");
		
		List<String> urls = getStoredURLs();
		Iterator<String> iterator = urls.iterator();
		while (iterator.hasNext()) {
			String url = iterator.next();
			Element e = getDocumentAsDOM(url);
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
			storeDOM = XMLDOMBuilder.newDocument();
		}
		
    	Element root = storeDOM.createElementNS(Constants.CompNamespace,Constants.CompPrefix + ":dts");
		
		List<String> urls = getStoredURLs();
		Iterator<String> iterator = urls.iterator();
		while (iterator.hasNext()) {
			String url = iterator.next();
	    	Element file = storeDOM.createElementNS(Constants.CompNamespace,Constants.CompPrefix + ":file");
	    	file.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":uri", url);
	    	file.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":index",this.getNextFragmentId());
			root.appendChild(file);
			file.appendChild(getAnnotatedDocumentAsDOM(url));
		}
    	root.setAttributeNS(Constants.CompNamespace,Constants.CompPrefix + ":index",this.getNextFragmentId());
    	storeDOM.appendChild(root);
    	return storeDOM;
    	
    }
    
    /**
     * @see org.xbrlapi.data.Store#getNextFragmentId()
     */
    public String getNextFragmentId() throws XBRLException {
        return "1";
/*        Fragment summary = this.getFragment("summary");
        String maxId = summary.getMetaAttribute("maximumFragmentId");
        if (maxId == null) {
            return "1";
        } else {
            int value = (new Integer(maxId)).intValue();
            return (new Integer(value+1)).toString();
        }
*/    }
    

    
    /**
     * @see org.xbrlapi.data.Store#getStubs()
     */
    public FragmentList<Fragment> getStubs() throws XBRLException {
        return this.<Fragment>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@stub]");
    }
    
    /**
     * @see org.xbrlapi.data.Store#getStub(String url)
     */
    public Fragment getStub(String url) throws XBRLException {
        try {
            URL matchURL = getMatcher().getMatch(new URL(url));
            FragmentList<Fragment> stubs = this.<Fragment>query("/"+ Constants.XBRLAPIPrefix + ":" + "fragment[@stub and @url='" + matchURL + "']");        
            if (stubs.getLength() == 0) return null;
            if (stubs.getLength() > 1) throw new XBRLException("There are " + stubs.getLength() + " stubs for " + url);
            return stubs.get(0);
        } catch (MalformedURLException e) {
            throw new XBRLException("Malformed URL.",e);
        }
    }
    
    /**
     * @see org.xbrlapi.data.Store#getStub(String stubId)
     */
    public void removeStub(String stubId) throws XBRLException {
        if (hasFragment(stubId)) removeFragment(stubId);
    }            
    
    
    /**
     * @see org.xbrlapi.data.Store#getDocumentsToDiscover()
     */
    public List<URL> getDocumentsToDiscover() throws XBRLException {
        FragmentList<Fragment> stubs = getStubs();
        LinkedList<URL> list = new LinkedList<URL>();
        for (Fragment stub: stubs) {
            String url = stub.getMetaAttribute("url");
            try {
                list.add(new URL(url));
            } catch (MalformedURLException e) {
                throw new XBRLException("URL " + url + " is malformed", e);
            }
        }
        return list;
    }
    
	/**
	 * Saves the individual documents in the data store 
	 * into a directory structure that is placed into
	 * the specified directory.  The directory structure that is 
	 * created mirrors the structure of the URLs of the documents. 
	 * Note that the URLs of the documents that are written out
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
	 * Serializes those documents in the data store with a URL that
	 * begins with the specified URL prefix. They are saved to the local
	 * file system in the same manner as is applied for the saveDocuments
	 * method that operates on all documents in the data store.
	 * @param destination The folder in which the directory structure and
	 * the documents in the data store are to be saved.
	 * @param urlPrefix All documents in the data store with a URL that begins 
	 * with the string specified by urlPrefix will be saved to the local
	 * file system.
	 * @throws XBRLException If the root folder does not exist or 
	 * is not a directory or if the documents in the store cannot 
	 * be saved to the local file system.
	 */
	public void saveDocuments(File destination, String urlPrefix) throws XBRLException {
		
		try {
			if (! destination.exists()) throw new XBRLException("The specified directory does not exist.");
			
			if (! destination.isDirectory()) throw new XBRLException("A directory rather than a file must be specified.");
			
			List<String> urls = getStoredURLs();
			Iterator<String> iterator = urls.iterator();
			while (iterator.hasNext()) {			
				String url = iterator.next();
				if (url.startsWith(urlPrefix)) {
					CacheImpl cache = new CacheImpl(destination);
					File file = cache.getCacheFile(new URL(url));
					Element e = getDocumentAsDOM(url);
					serialize(e,file);
				}
			}
		} catch (MalformedURLException e) {
			throw new XBRLException("Document could not be saved because the URL could not be formed.", e);
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
     * @see org.xbrlapi.data.Store#getFragments(String)
     */
    public <F extends Fragment> FragmentList<F> getFragments(String interfaceName) throws XBRLException {
        String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl." + interfaceName + "Impl']";
        if (interfaceName.indexOf(".") > -1) {
            query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='" + interfaceName + "']";
        }
    	return this.<F>query(query);
    }
    
    /**
     * @see org.xbrlapi.data.Store#getChildFragments(String, String)
     */
    public <F extends Fragment> FragmentList<F> getChildFragments(String interfaceName, String parentIndex) throws XBRLException {
    	return this.<F>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl." + interfaceName + "Impl' and @parentIndex='" + parentIndex + "']");
    }    
    
    /**
     * @see org.xbrlapi.data.Store#getNetworks(String)
     */
    public Networks getNetworks(String arcRole) throws XBRLException {

    	Networks networks = new NetworksImpl();
    	
    	// First get the set of arcs using the arc role
		FragmentList<Arc> arcs = getArcs(arcRole);
		logger.info(arcs.getLength());
    	for (Arc arc: arcs) {
    		FragmentList<ArcEnd> sources = arc.getSourceFragments();
    		FragmentList<ArcEnd> targets = arc.getTargetFragments();
    		for (ArcEnd source: sources) {
    			for (ArcEnd target: targets) {
    				Fragment s = null;
    				Fragment t = null;
            		if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
            			s = ((Locator) source).getTargetFragment();
            		else s = source;
            		if (target.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
            			t = ((Locator) target).getTargetFragment();
            		else t = target;
        			Relationship relationship = new RelationshipImpl(arc,s,t);
        			networks.addRelationship(relationship);
    			}
    		}
    	}
    	return networks;    	
    }
    
    /**
     * @see org.xbrlapi.data.Store#getNetworks(String,String)
     */
    public Networks getNetworks(String linkrole, String arcrole) throws XBRLException {

        Networks networks = new NetworksImpl();

        Map<String,ExtendedLink> links = new HashMap<String,ExtendedLink>();
        
        // First get the set of arcs using the arc role
        FragmentList<Arc> arcs = getArcs(arcrole);
        for (Arc arc: arcs) {
            ExtendedLink link = null;
            String linkIndex = arc.getParentIndex();
            if (links.containsKey(linkIndex)) {
                link = links.get(linkIndex);
            } else {
                link = (ExtendedLink) arc.getParent();
                links.put(link.getFragmentIndex(),link);
            }
            String myLinkrole = link.getLinkRole();
            if (myLinkrole.equals(linkrole)) {
                FragmentList<ArcEnd> sources = arc.getSourceFragments();
                FragmentList<ArcEnd> targets = arc.getTargetFragments();
                for (ArcEnd source: sources) {
                    for (ArcEnd target: targets) {
                        Fragment s = null;
                        Fragment t = null;
                        if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
                            s = ((Locator) source).getTargetFragment();
                        else s = source;
                        if (target.getType().equals("org.xbrlapi.impl.LocatorImpl")) 
                            t = ((Locator) target).getTargetFragment();
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
    private FragmentList<Arc> getArcs(String arcrole) throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/*[@xlink:arcrole='" + arcrole + "' and @xlink:type='arc']]";
    	return this.<Arc>query(query);
    }
    
    
    
    /**
     * Utility method to return a list of fragments in a data store
     * that have a type corresponding to the specified fragment interface name and
     * that are in the document with the specified URL.
     * @param url The URL of the document to get the fragments from.
     * @param interfaceName The name of the interface.  EG: If a list of
     *  org.xbrlapi.impl.ReferenceArcImpl fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an Arc would not return all ReferenceArcs as well as other
     *  types of arcs.
     * @return a list of fragments with the given fragment type and in the given document.
     * @throws XBRLException
     */
    public <F extends Fragment> FragmentList<F> getFragmentsFromDocument(URL url, String interfaceName) throws XBRLException {
        URL matchURL = getMatcher().getMatch(url);
        return this.<F>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='"+ matchURL + "' and @type='org.xbrlapi.impl." + interfaceName + "Impl']");
    }    
    
    /**
     * @return a list of all of the root-level facts in the data store (those facts
     * that are children of the root element of an XBRL instance).  Returns an empty list 
     * if no facts are found.
     * @throws XBRLException
     */
    public FragmentList<Fact> getFacts() throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragments("Instance");
    	return getFactsFromInstances(instances);
    }
    
    /**
     * This method is provided as a helper method for the getFact methods.
     * @param instances The list of instance fragments to extract facts from.
     * @return The list of facts in the instances.
     * @throws XBRLException
     */
    private FragmentList<Fact> getFactsFromInstances(FragmentList<Instance> instances) throws XBRLException {
    	FragmentList<Fact> facts = new FragmentListImpl<Fact>();
    	for (int i=0; i<instances.getLength(); i++) {
    		Instance instance = instances.getFragment(i);
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
    private FragmentList<Item> getItemsFromInstances(FragmentList<Instance> instances) throws XBRLException {
    	FragmentList<Fact> facts = getFactsFromInstances(instances);
    	FragmentList<Item> items = new FragmentListImpl<Item>();
    	for (Fact fact: facts) {
    		if (! fact.getType().equals("org.xbrlapi.org.impl.TupleImpl"))
    			items.addFragment((Item) fact);
    	}
    	return items;
    }
    
    /**
     * Helper method for common code in the getTuple methods.
     * @param instances The instances to retrieve tuples for.
     * @return a list of root tuples in the instances.
     * @throws XBRLException
     */
    private FragmentList<Tuple> getTuplesFromInstances(FragmentList<Instance> instances) throws XBRLException {
    	FragmentList<Fact> facts = getFactsFromInstances(instances);
    	FragmentList<Tuple> tuples = new FragmentListImpl<Tuple>();
    	for (Fact fact: facts) {
    		if (fact.getType().equals("org.xbrlapi.org.impl.TupleImpl"))
    			tuples.addFragment((Tuple) fact);
    	}
    	return tuples;
    }    
    
    /**
     * @return a list of all of the root-level items in the data store(those items
     * that are children of the root element of an XBRL instance).
     * TODO eliminate the redundant retrieval of tuples from the getItems methods.
     * @throws XBRLException
     */
    public FragmentList<Item> getItems() throws XBRLException {
    	FragmentList<Instance> instances = this.getFragments("Instance");
    	return getItemsFromInstances(instances);
    }
    


    /**
     * @param url The URL of the document to get the facts from.
     * @return a list of all of the root-level facts in the specified document.
     * @throws XBRLException
     */
    public FragmentList<Fact> getFacts(URL url) throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragmentsFromDocument(url,"Instance");
    	return this.getFactsFromInstances(instances);
    }
    
    /**
     * @param url The URL of the document to get the items from.
     * @return a list of all of the root-level items in the data store.
     * @throws XBRLException
     */
    public FragmentList<Item> getItems(URL url) throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragmentsFromDocument(url,"Instance");
    	return this.getItemsFromInstances(instances);
    }
    


    /**
     * @see org.xbrlapi.data.Store#getRootFragmentForDocument(String)
     */
    public <F extends Fragment> F getRootFragmentForDocument(String url) throws XBRLException {
    	FragmentList<F> fragments = this.<F>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + url + "' and @parentIndex='none']");
    	if (fragments.getLength() == 0) return null;
    	if (fragments.getLength() > 1) throw new XBRLException("Two fragments identify themselves as roots of the one document.");
    	return fragments.getFragment(0);
    }

    
    /**
     * @see org.xbrlapi.data.Store#getRootFragments()
     */
    public <F extends Fragment> FragmentList<F> getRootFragments() throws XBRLException {
    	return this.<F>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='none']");
    }

    /**
     * @see org.xbrlapi.data.Store#getLanguage(String, String)
     */
    public Language getLanguage(String encoding, String code) throws XBRLException {
        if (encoding == null) return null;
        if (code == null) return null;
        String query = "/"+ Constants.XBRLAPIPrefix + ":" + "fragment[@type='org.xbrlapi.impl.LanguageImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/lang:language/lang:encoding='" + encoding.toUpperCase() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/lang:language/lang:code='" + code.toUpperCase() + "']";
        FragmentList<Language> languages = this.<Language>query(query);
        if (languages.getLength() == 0) return null;
        return languages.get(0);
    }    
    

    
}
