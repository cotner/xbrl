package org.xbrlapi.data.dom;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathResult;
import org.xbrlapi.XML;
import org.xbrlapi.data.BaseStoreImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.utilities.XMLDOMBuilder;

public class StoreImpl extends BaseStoreImpl implements Store {

	protected static Logger logger = Logger.getLogger(StoreImpl.class);
	
	/**
	 * The map of data fragments.
	 */
	private Map<String,Element> fragmentMap = new HashMap<String,Element>();
	private Map<Element,String> indexMap = new HashMap<Element,String>();
	
	/**
	 * XML DOM used to build the fragments in the store.
	 */
	private Document dom  = null;
	
	/**
	 * XML DOM Element containing all fragments.
	 */
	private Element store = null;
	
	/**
	 * Name of the root element in the DOM
	 */
	private final String ROOT_NAME = "store";
	
	/**
	 * The next fragmentId.
	 */
	String nextId = "1";
	
	/**
	 * Initialise the data store.
	 * @throws XBRLException if the loader state cannot be initialised
	 * or the XML DOM builder cannot be instantiated.
	 */
	public StoreImpl() throws XBRLException {
	    super();
		dom = (new XMLDOMBuilder()).newDocument();
		store = dom.createElement(ROOT_NAME);
		dom.appendChild(store);
	}

	/**
	 * @see org.xbrlapi.data.Store#close()
	 */
	public synchronized void close() throws XBRLException {
		super.close();
	}
	
	/**
	 * The store is in memory so no actions are required to remove
	 * the data store from persistent storage.
	 * @see org.xbrlapi.data.Store#delete()
	 */
	public synchronized void delete() throws XBRLException {
		; 
	}	
	
    /**
     * @see org.xbrlapi.data.Store#persist(XML)
     */
    public synchronized int getSize() throws XBRLException {
        return fragmentMap.size();
    }
	
	/**
	 * @see org.xbrlapi.data.Store#persist(XML)
	 */
	public synchronized void persist(XML xml) throws XBRLException {
		
	    logger.debug("Storing " + xml.getType() + " " + xml.getIndex());
	    
		// If the fragment is already stored we are done.
		if (xml.getStore() != null) {			
			return;
		}
		
		// Get the fragment index to delete existing fragments with the same index.
		String index = xml.getIndex();
		if (hasXML(index)) {
		    this.remove(index);
        }

		// TODO Eliminate this importNode call.
        Element element = (Element) dom.importNode(xml.getBuilder().getMetadata(),true);
        store.appendChild(element);
        fragmentMap.put(index, element);
        indexMap.put(element, index);
        
        // Finalise the fragment, ready for use
        xml.setResource(element);
        xml.setStore(this);

	}

	/**
	 * @see org.xbrlapi.data.Store#hasXML(String)
	 */
	public synchronized boolean hasXML(String index) throws XBRLException {
		if (fragmentMap.containsKey(index)) return true;
		return false;
	}

	/**
	 * @see org.xbrlapi.data.Store#getFragment(String)
	 */
	public synchronized <F extends XML> F getFragment(String index) throws XBRLException {

		Element root = fragmentMap.get(index);
		if (root == null) {
			throw new XBRLException("Index " + index + " does not map to a fragment in the store.");
		}
		return  FragmentFactory.newFragment(this, root);
	}



	/**
	 * Remove a fragment from the store.
	 * 
	 * @param index
	 *            The index of the fragment to be removed from the store.
	 * @throws XBRLException
	 *             if the fragment exists but cannot be removed from the store.
	 */
	public synchronized void remove(String index) throws XBRLException {
    	
        if (! hasXML(index)) return;

        Element d = fragmentMap.get(index);
        fragmentMap.remove(index);
        indexMap.remove(d);
        d.getParentNode().removeChild(d);
	}

    /**
     * Get the actual DOM that is used to hold the data store.
     * @return the XML DOM that is the data store.  Note that this
     * uses a different format for the DOM than that returned by the
     * getStoreAsDOM method.
     * Contributed by Howard Ungar 13 February, 2007.
     */
    public synchronized Document getStoreAsDOM() {
    	return this.dom;
    }



    /**
     * Contains the logic common to queries that return fragments and
     * queries that return fragment indices.
     * @param query The query to run.
     * @return The XPath query result.
     * @throws XBRLException
     */
    private XPathResult runQuery(String query) throws XBRLException {

        String roots = "/store/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
        // Create an XPath evaluator and pass in the document.
        XPathEvaluator evaluator = new XPathEvaluatorImpl(dom);
        XPathNSResolverImpl resolver = new XPathNSResolverImpl();
        for (URI namespace: this.namespaceBindings.keySet()) {
            resolver.setNamespaceBinding(this.namespaceBindings.get(namespace),namespace.toString());
        }
        return (XPathResult) evaluator.evaluate(query, dom, resolver, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);
    }
    

    
    /**
	 * Run a query against the collection of all fragments in the store.
	 * @param query The XPath query to run.
	 * @return a resource set that contains data for each matching fragment.
	 * @throws XBRLException if the query cannot be executed.
	 */
    @SuppressWarnings(value = "unchecked")
	public synchronized <F extends XML> List<F> query(String query) throws XBRLException {
        XPathResult result = runQuery(query);
		List<F> fragments = new Vector<F>();
		Node n;
	    while ((n = result.iterateNext()) != null) {
	    	String index = getIndex(n);
	    	fragments.add((F) getFragment(index));
	    }
	    return fragments;
	}
    
    /**
     * @see Store#queryCount(String)
     */
    public synchronized long queryCount(String query) throws XBRLException {
        XPathResult result = runQuery(query);
        @SuppressWarnings("unused")
        Node n;
        long count = 0;
        while ((n = result.iterateNext()) != null) {
            count++;
        }
        return count;
    }    
    
    /**
     * @see org.xbrlapi.data.Store#queryForIndices(String)
     */
    public synchronized Set<String> queryForIndices(String query) throws XBRLException {
        
        XPathResult xpr = runQuery(query);
        Node n;
        HashMap<String,String> indices = new HashMap<String,String>();
        while ((n = xpr.iterateNext()) != null) {
            String index = getIndex(n);
            indices.put(index,null);
        }
        return indices.keySet();
    }
    
    /**
     * @see org.xbrlapi.data.Store#queryForStrings(String)
     */
    public synchronized Set<String> queryForStrings(String query) throws XBRLException {
                
        XPathResult result = runQuery(query);
        Set<String> strings = new TreeSet<String>();
        Node n = null;
        while ((n = result.iterateNext()) != null) {
            strings.add(n.getNodeValue());
        }
        return strings;

    }    
	
	/**
	 * Get the index of the fragment containing the node matched by the query.
	 * @param n The matching node returned by the query.
	 * @return The index of the fragment that contains the matching node.
	 * @throws XBRLException if the matching node is null or the fragment index could not be
	 * identified.
	 */
	private String getIndex(Node n) throws XBRLException {
		if (n == null)  throw new XBRLException("The matching node from a query cannot be null.");
		if (n.getNodeType() == Element.DOCUMENT_NODE) throw new XBRLException("The fragment for the matching node could not be found.");
		Node parentNode =  n.getParentNode();
		if (n.getNodeType() == Node.ATTRIBUTE_NODE) {
			Attr a = (Attr) n;
			parentNode = a.getOwnerElement();
		}
		if (parentNode == null) throw new XBRLException("The parent node for the matched node should not be null.");
		if (parentNode.getNodeType() != Element.ELEMENT_NODE) throw new XBRLException("The fragment for the matching node could not be found.");
		Element parent = (Element) parentNode;
    	if (parent.getTagName().equals(ROOT_NAME)) {
    		return indexMap.get(n);
		}
		return getIndex(parent);
	}

}