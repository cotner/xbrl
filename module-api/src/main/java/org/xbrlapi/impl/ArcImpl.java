package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Used for all extended link arcs.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ArcImpl extends ExtendedLinkContentImpl implements Arc {

    /**
     * @see org.xbrlapi.Arc#getAttribute(String,String)
     */
    public String getAttribute(String namespace, String name) throws XBRLException {
        Element root = getDataRootElement();
        if (! root.hasAttributeNS(namespace,name)) return null;
        return root.getAttributeNS(namespace,name);
    }
    
    /**
     * @see org.xbrlapi.Arc#hasAttribute(String,String)
     */    
    public boolean hasAttribute(String namespace, String name) throws XBRLException {
        return getDataRootElement().hasAttributeNS(namespace,name);
    }    
    
    /**
     * @see org.xbrlapi.Arc#getAttribute(String)
     */
    public String getAttribute(String name) throws XBRLException {
        Element root = getDataRootElement();
        if (! root.hasAttribute(name)) return null;
        return root.getAttribute(name);
    }
    
    /**
     * @see org.xbrlapi.Arc#hasAttribute(String)
     */
    public boolean hasAttribute(String name) throws XBRLException {
        return getDataRootElement().hasAttribute(name);
    }
    
    /**
     * @see org.xbrlapi.Arc#getShow() 
     */
    public String getShow() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace,"show")) return null;
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"show");
    }
	
    /**
     * @see org.xbrlapi.Arc#getActuate() 
     */
    public String getActuate() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace,"actuate")) return null;
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"actuate");
    }
	
    /**
     * @see org.xbrlapi.Arc#getFrom() 
     */
    public String getFrom() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace,"from")) return null;
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"from");
    }
    
    /**
     * @see org.xbrlapi.Arc#getTo() 
     */
    public String getTo() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace,"to")) return null;
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"to");
    }
    
    /**
     * @see org.xbrlapi.Arc#getArcrole() 
     */
    public URI getArcrole() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace,"arcrole")) return null;
    	String arcrole = getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"arcrole");
    	try {
    	    return new URI(arcrole);
    	} catch (URISyntaxException e) {
    	    throw new XBRLException("arcrole " + arcrole + "is not a valid URI.",e);
    	}
    	
    }
    
    /**
     * @see org.xbrlapi.Arc#getOrder() 
     */
    public Double getOrder() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttribute("order")) return new Double(1);
    	String value = getDataRootElement().getAttribute("order").trim();
    	try {
    	    return new Double(value);    
    	} catch (Exception e) {
    	    throw new XBRLException("The arc order '" + value + "' is not a valid decimal value.",e);
    	}
    }
    
    /**
     * @see org.xbrlapi.Arc#getPriority()
     */
    public Integer getPriority() throws XBRLException {
        Element root = getDataRootElement();
        if (! root.hasAttribute("priority")) return new Integer(1);
        return new Integer(getDataRootElement().getAttribute("priority"));    
    }
    
    
    /**
     * @see org.xbrlapi.Arc#getUse()
     */
    public String getUse() throws XBRLException {
        Element root = getDataRootElement();
        if (! root.hasAttribute("use")) return "optional";
        return getDataRootElement().getAttribute("use");    
    }
    
    /**
     * @see org.xbrlapi.Arc#isProhibited()
     */
    public boolean isProhibited() throws XBRLException {
        return (getUse().equals("prohibited"));
    }    
    
    /**
     * @see org.xbrlapi.Arc#getSourceFragments() 
     */
    public <E extends ArcEnd> List<E> getSourceFragments() throws XBRLException {
        String query = "/*[@parentIndex='" + this.getParentIndex() + "' and */*/@xlink:label='" + this.getFrom() + "']";
        return this.getStore().<E>query(query);
    }
    
    /**
     * @see org.xbrlapi.Arc#getTargetFragments() 
     */
    public <E extends ArcEnd> List<E> getTargetFragments() throws XBRLException {
        String query = "/*[@parentIndex='" + this.getParentIndex() + "' and */*/@xlink:label='" + this.getTo() + "']";
        return this.getStore().<E>query(query);
    }
    
    /**
     * @see org.xbrlapi.Arc#getSemanticAttributes()
     */
    public NamedNodeMap getSemanticAttributes() throws XBRLException {

    	// Clone the node to stop the attribute removal from impacting on the XML.
    	NamedNodeMap attributes = this.getDataRootElement().cloneNode(true).getAttributes();    	
    	
    	try {
    		attributes.removeNamedItemNS(Constants.XLinkNamespace,"arcrole");
    	} catch (DOMException e) {}
    	try {
    		attributes.removeNamedItemNS(Constants.XLinkNamespace,"type");
    	} catch (DOMException e) {}
    	try {
    		attributes.removeNamedItemNS(Constants.XLinkNamespace,"from");
    	} catch (DOMException e) {}
    	try {
			attributes.removeNamedItemNS(Constants.XLinkNamespace, "to");
		} catch (DOMException e) {
		}
		try {
			attributes.removeNamedItemNS(Constants.XLinkNamespace, "show");
		} catch (DOMException e) {
		}
		try {
			attributes.removeNamedItemNS(Constants.XLinkNamespace, "actuate");
		} catch (DOMException e) {
		}
		try {
			attributes.removeNamedItemNS(Constants.XLinkNamespace, "title");
		} catch (DOMException e) {
		}
		try {
			attributes.removeNamedItem("use");
		} catch (DOMException e) {
		}
		try {
			attributes.removeNamedItem("priority");
		} catch (DOMException e) {
		}
    	try {
    		attributes.removeNamedItemNS(Constants.XMLNamespace,"lang");
    	} catch (DOMException e) {}
    	try {
    		attributes.removeNamedItemNS(Constants.XMLNamespace,"base");
    	} catch (DOMException e) {}
		return attributes;
	}

    /**
     * @see org.xbrlapi.Arc#getSemanticKey() 
     */
    public String getSemanticKey() throws XBRLException {
    	NamedNodeMap attributes = getSemanticAttributes();
    	SortedMap<String,String> map = new TreeMap<String,String>();
    	
    	// Handle the order attribute which takes a default value of 1 when not specified.
    	String order = "1";
    	if (attributes.getNamedItem("order") != null) {
    		order = attributes.getNamedItem("order").getNodeValue();
    		attributes.removeNamedItem("order");
    	}
    	map.put("order",order);
    	for (int i=0; i<attributes.getLength(); i++) {
    		Node node = attributes.item(i);
			String namespace = node.getNamespaceURI();
	    	String key = null;
    		if (namespace == null) {
    			key = node.getNodeName();
    		} else {
    			key = namespace + ":" + node.getLocalName();
    		}
    		map.put(key,node.getNodeValue());
    	}
    	
    	String semanticKey = "";
    	while (! map.isEmpty()) {
    		String key = map.firstKey();
    		semanticKey += key + "=" + map.get(key) + "|";
    		map.remove(key);
    	}
    	
    	return semanticKey;

    }    
    
    /**
     * @param other The other arc to compare against.
     * @return true if and only if the maps have the 
     * same attributes and the same attribute values.
     * @throws XBRLException
     */
    private boolean semanticAttributesEqual(Arc other) throws XBRLException {
    	NamedNodeMap a = getSemanticAttributes();
    	NamedNodeMap b = other.getSemanticAttributes();

    	// Handle the order attribute which takes a default value of 1 when not specified.
    	String aOrder = "1";
    	String bOrder = "1";
    	if (a.getNamedItem("order") != null) {
    		aOrder = a.getNamedItem("order").getNodeValue();
    		a.removeNamedItem("order");
    	}
    	if (b.getNamedItem("order") != null) { 
    		bOrder = b.getNamedItem("order").getNodeValue();
    		b.removeNamedItem("order");
    	}
    	if (! aOrder.equals(bOrder)) {
    		return false;
    	}
    	
    	for (int i=0; i<a.getLength(); i++) {
    		Node aNode = a.item(i);
			String aNamespace = aNode.getNamespaceURI();
    		if (aNamespace == null) {
    			String aName = aNode.getNodeName();
        		Node bNode = b.getNamedItem(aName);
        		if (bNode == null) { 
        			return false;
        		}
    			b.removeNamedItem(aName);
    		} else {
    			String aName = aNode.getLocalName();
        		Node bNode = b.getNamedItemNS(aNamespace,aName);
        		if (bNode == null) {
        			return false;
        		}
    			b.removeNamedItemNS(aNamespace,aName);
    		}
    	}
		if (b.getLength() == 0)  return true;
		
		return false;
    	
    }    
    
    /**
     * @see org.xbrlapi.Arc#semanticEquals(Arc)
     */
    public boolean semanticEquals(Arc other) throws XBRLException {
    	
    	if (! this.getNamespace().equals(other.getNamespace())) return false;
    	if (! this.getLocalname().equals(other.getLocalname())) return false;

    	ExtendedLink aLink = this.getExtendedLink();
    	ExtendedLink bLink = other.getExtendedLink();
    	
    	if (! aLink.getNamespace().equals(bLink.getNamespace())) return false;
    	if (! aLink.getLocalname().equals(bLink.getLocalname())) return false;
    	if (! aLink.getLinkRole().equals(bLink.getLinkRole())) return false;
    	
    	if (! semanticAttributesEqual(other)) return false;
    	
    	return true;
    }

}
