package org.xbrlapi.data.dom;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.XML;
import org.xbrlapi.data.BaseStoreImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.utilities.XMLDOMBuilder;

public class StoreImpl extends BaseStoreImpl implements Store, Serializable {

	private final static Logger logger = Logger.getLogger(StoreImpl.class);
	
	/**
	 * The map of data fragments.
	 */
	transient private Map<String,Element> fragmentMap;
	transient private Map<Element,String> indexMap;
	
	/**
	 * XML DOM used to build the fragments in the store.
	 */
	private Document dom  = null;
	
	/**
	 * Name of the root element in the DOM
	 */
	private static final String ROOT_NAME = "store";

	/**
	 * Initialise the data store.
	 * @throws XBRLException if the loader state cannot be initialised
	 * or the XML DOM builder cannot be instantiated.
	 */
	public StoreImpl() throws XBRLException {
	    super();
	    initialize();
		dom = (new XMLDOMBuilder()).newDocument();
		Element store = dom.createElement(ROOT_NAME);
		dom.appendChild(store);
	}
	
	private void initialize() {
	    fragmentMap = new HashMap<String,Element>();
	    indexMap = new HashMap<Element,String>();
	}

	/**
	 * Handles object serialization
	 * @param out The input object stream used to store the serialization of the object.
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
	    out.defaultWriteObject( );
	    try {
	        String xml = serializeToString(dom.getDocumentElement());
	        out.writeObject(xml);
	    } catch (XBRLException e) {
	        throw new IOException("Could not convert the store content to a string representation of the XML.",e);
	    }
	}
	 
	/**
	 * Handles object inflation.
	 * @param in The input object stream used to access the object's serialization.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {

        in.defaultReadObject();
        initialize();
        try {
            XMLDOMBuilder builder = new XMLDOMBuilder();
            dom = builder.newDocument((String) in.readObject());

            NodeList nodes = dom.getDocumentElement().getChildNodes();
            for (int i=0; i<nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.hasAttribute("index")) {
                        fragmentMap.put(element.getAttribute("index"),element);
                        indexMap.put(element,element.getAttribute("index"));
                    }
                }
            }
            
        } catch (XBRLException e) {
            throw new IOException("The data store could not be read.",e);
        }
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
		if (hasXMLResource(index)) {
		    this.remove(index);
        }

		// TODO Eliminate this importNode call.
        Element element = (Element) dom.importNode(xml.getBuilder().getMetadata(),true);
        dom.getDocumentElement().appendChild(element);
        fragmentMap.put(index, element);
        indexMap.put(element, index);
        
        // Finalise the fragment, ready for use
        xml.setResource(element);
        xml.setStore(this);

	}

	/**
	 * @see org.xbrlapi.data.Store#hasXMLResource(String)
	 */
	public synchronized boolean hasXMLResource(String index) throws XBRLException {
		if (fragmentMap.containsKey(index)) return true;
		return false;
	}

	/**
	 * @see org.xbrlapi.data.Store#getXMLResource(String)
	 */
	public synchronized <F extends XML> F getXMLResource(String index) throws XBRLException {

		Element root = fragmentMap.get(index);
		if (root == null) {
			throw new XBRLException("Index " + index + " does not map to a fragment in the store.");
		}
		return FragmentFactory.<F>newFragment(this, root);
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
    	
        if (! hasXMLResource(index)) return;

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





    transient private XQueryCompiler compiler;

    transient private Processor processor;

    /**
     * Contains the logic common to queries that return fragments and
     * queries that return fragment indices.
     * @param query The query to run.
     * @return The query results as an iterable sequence.
     * @throws XBRLException
     */
    private XdmValue runQuery(String query) throws XBRLException {

        if (processor == null) {
            processor = new Processor(false);
        }
        if (compiler == null) {
            compiler = processor.newXQueryCompiler();
            compiler.declareNamespace(Constants.XBRL21LinkPrefix,Constants.XBRL21LinkNamespace.toString());
            compiler.declareNamespace(Constants.XBRL21Prefix,Constants.XBRL21Namespace.toString());
            compiler.declareNamespace(Constants.XBRLAPIPrefix,Constants.XBRLAPINamespace.toString());
            compiler.declareNamespace(Constants.XBRLAPILanguagesPrefix,Constants.XBRLAPILanguagesNamespace.toString());
            compiler.declareNamespace(Constants.XLinkPrefix,Constants.XLinkNamespace.toString());
            compiler.declareNamespace(Constants.XMLPrefix,Constants.XMLNamespace.toString());
            compiler.declareNamespace(Constants.XMLSchemaPrefix,Constants.XMLSchemaNamespace.toString());
        }
        
        String roots = "/" + StoreImpl.ROOT_NAME + "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);

        try {
            for (URI namespace: this.namespaceBindings.keySet()) {
                compiler.declareNamespace(this.namespaceBindings.get(namespace),namespace.toString());
            }
            XQueryExecutable executable = compiler.compile(query);
            XQueryEvaluator evaluator = executable.load();
            XdmNode xdmNode = processor.newDocumentBuilder().wrap(dom);
            evaluator.setContextItem(xdmNode);
            return evaluator.evaluate();
        } catch (SaxonApiException e) {
            throw new XBRLException("Saxon failed to execute " + query,e);
        }
    }
    

    
    /**
	 * Run a query against the collection of all fragments in the store.
	 * @param query The XPath query to run.
	 * @return a resource set that contains data for each matching fragment.
	 * @throws XBRLException if the query cannot be executed.
	 */
    @SuppressWarnings(value = "unchecked")
	public synchronized <F extends XML> List<F> queryForXMLResources(String query) throws XBRLException {
        
        query = "for $attr in "+ query + "/@index return string($attr)";
        XdmValue result = runQuery(query);
        List<F> fragments = new Vector<F>();
        for (XdmItem item: result) {
            String index = "";
            if (item.isAtomicValue()) {
                index = ((XdmAtomicValue)item).getStringValue();
            } else {
                index = ((XdmNode)item).getStringValue();
            }
            if (this.fragmentMap.containsKey(index)) {
                fragments.add((F) getXMLResource(index));
            }
        }
	    return fragments;
	}
    
    /**
     * @see Store#queryCount(String)
     */
    @SuppressWarnings("unused")
    public synchronized long queryCount(String query) throws XBRLException {
        XdmValue result = runQuery(query);
        long count = 0;
        for (XdmItem item: result) {
            count++;
        }
        return count;
    }    
    
    /**
     * @see org.xbrlapi.data.Store#queryForIndices(String)
     */
    public synchronized Set<String> queryForIndices(String query) throws XBRLException {
        
        query = query + "/@index";
        XdmValue result = runQuery(query);
        Set<String> indices = new HashSet<String>();
        for (XdmItem item: result) {
            indices.add(item.getStringValue());
        }

        return indices;
    }
    
    /**
     * @see org.xbrlapi.data.Store#queryForStrings(String)
     */
    public synchronized Set<String> queryForStrings(String query) throws XBRLException {
                
        XdmValue result = runQuery(query);
        Set<String> strings = new TreeSet<String>();
        for (XdmItem item: result) {
            strings.add(item.getStringValue());
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
		if (parentNode == null) throw new XBRLException("The parent node for the matched node must not be null.");
		if (parentNode.getNodeType() != Element.ELEMENT_NODE) throw new XBRLException("The fragment for the matching node could not be found.");
		Element parent = (Element) parentNode;
    	if (parent.getTagName().equals(ROOT_NAME)) {
    		return indexMap.get(n);
		}
		return getIndex(parent);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        NodeList nodes = dom.getDocumentElement().getChildNodes();
        for (int i=0; i<nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.hasAttribute("index")) {
                    String index = element.getAttribute("index");
                    result = prime * result + ((index == null) ? 0 : index.hashCode());
                }
            }
        }
        return result;
    }

    /**
     * The two stores are equal if they have equal
     * caches and matchers and if they have the same
     * number of fragments with the same indices.  This
     * is an incomplete way of assessing equality but it
     * does get us there for most purposes and dodges the
     * issue of XML DOM equality assessment.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        StoreImpl other = (StoreImpl) obj;
        if (dom == null) {
            if (other.dom != null)
                return false;
        } else {
            NodeList nodes = dom.getDocumentElement().getChildNodes();
            Set<String> indicesInThisStore = new HashSet<String>();
            for (int i=0; i<nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.hasAttribute("index")) {
                        indicesInThisStore.add(element.getAttribute("index"));
                    }
                }
            }            
            nodes = dom.getDocumentElement().getChildNodes();
            Set<String> indicesInOtherStore = new HashSet<String>();
            for (int i=0; i<nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.hasAttribute("index")) {
                        indicesInOtherStore.add(element.getAttribute("index"));
                    }
                }
            }
            if (! indicesInThisStore.equals(indicesInOtherStore)) return false;
        }
        return true;
    }

}