package org.xbrlapi.data.dom;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathResult;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.data.XBRLStoreImpl;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.utilities.XMLDOMBuilder;

public class StoreImpl extends XBRLStoreImpl implements XBRLStore {

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
	 * @throws XBRLException if the loader state cannot be initialised.
	 */
	public StoreImpl() {
	    super();
		dom = XMLDOMBuilder.newDocument();
		store = dom.createElement(ROOT_NAME);
		dom.appendChild(store);
	}

	/**
	 * @see org.xbrlapi.data.Store#close()
	 */
	public void close() throws XBRLException {
		super.close();
	}
	
	/**
	 * The store is in memory so no actions are required to remove
	 * the data store from persistent storage.
	 * @see org.xbrlapi.data.Store#delete()
	 */
	public void delete() throws XBRLException {
		; 
	}	
	
	/**
	 * Add a fragment to the data store.
	 * 
	 * @param fragment
	 *            The fragment to be added to the DTS store.
	 * @throws XBRLException
	 *             if the fragment cannot be added to the store (eg: because one
	 *             with the same index is already in the store).
	 */
	public void storeFragment(Fragment fragment) throws XBRLException {
		
	    logger.debug("Storing " + fragment.getFragmentIndex());
	    
		// If the fragment is already stored we are done.
		if (fragment.getStore() != null) {			
			return;
		}
		
		// Get the fragment index to delete existing fragments with the same index.
		String index = fragment.getFragmentIndex();
		if (hasFragment(index)) {
		    this.removeFragment(index);
        }

		// TODO Eliminate this importNode call.
        Element element = (Element) dom.importNode(fragment.getBuilder().getMetadata(),true);
        store.appendChild(element);
        fragmentMap.put(index, element);
        indexMap.put(element, index);
        
        // Finalise the fragment, ready for use
        fragment.setResource(element);
        fragment.setStore(this);

	}

	/**
	 * Test if a store contains a specific fragment, as identified by its index.
	 * 
	 * @param index
	 *            The index of the fragment to test for.
	 * @return true iff the store contains a fragment with the specified
	 *         fragment index.
	 * @throws XBRLException
	 *             If the test cannot be conducted.
	 */
	public boolean hasFragment(String index) throws XBRLException {
		if (fragmentMap.containsKey(index)) return true;
		return false;
	}

	/**
	 * Retrieves a fragment from the store. The fragment will be created as a
	 * fragment of the original fragment type but will be returned as a straight
	 * fragment.
	 * 
	 * @param index The index of the fragment.
	 * @return The fragment corresponding to the specified index or null
	 * if the fragment is not in the store.
	 * @throws XBRLException if the fragment cannot be retrieved.
	 */
	public Fragment getFragment(String index) throws XBRLException {

		Element root = fragmentMap.get(index);
		if (root == null) {
			throw new XBRLException("Index " + index + " does not map to a fragment in the store.");
		}
		return  FragmentFactory.newFragment(this, root);
	}

	/**
	 * Remove a fragment from the store.
	 * 
	 * @param fragment
	 *            The fragment to be removed from the store.
	 * @throws XBRLException
	 *             if the fragment exists but cannot be removed from the store.
	 */
	public void removeFragment(Fragment fragment) throws XBRLException {
		removeFragment(fragment.getFragmentIndex());
	}

	/**
	 * Remove a fragment from the store.
	 * 
	 * @param index
	 *            The index of the fragment to be removed from the store.
	 * @throws XBRLException
	 *             if the fragment exists but cannot be removed from the store.
	 */
	public void removeFragment(String index) throws XBRLException {
    	
        if (! hasFragment(index)) return;

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
    public Document getStoreAsDOM() {
    	return this.dom;
    }



	/**
	 * Run a query against the collection of all fragments in the store.
	 * @param query
	 *            The XPath query to run.
	 * @return a resource set that contains data for each matching fragment.
	 * @throws XBRLException
	 *             if the query cannot be executed.
	 *             
	 */
    @SuppressWarnings(value = "unchecked")
	public <F extends Fragment> FragmentList<F> query(String query) throws XBRLException {

        query = query + this.getURLFilteringQueryClause();
        
		if (query.charAt(0) == '/')
			query = "/store" + query;
		else
			query = "/store/" + query;
		
		// Create an XPath evaluator and pass in the document.
		XPathEvaluator evaluator = new XPathEvaluatorImpl(dom);
		XPathNSResolverImpl resolver = new XPathNSResolverImpl();
        for (String namespace: this.namespaceBindings.keySet()) {
            resolver.setNamespaceBinding(this.namespaceBindings.get(namespace),namespace);
        }
		XPathResult result = (XPathResult) evaluator.evaluate(query, dom, resolver, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);
		FragmentList<F> fragments = new FragmentListImpl<F>();
		Node n;
	    while ((n = result.iterateNext()) != null) {
	    	String index = getIndex(n);
	    	fragments.addFragment((F) getFragment(index));
	    }
	    return fragments;
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