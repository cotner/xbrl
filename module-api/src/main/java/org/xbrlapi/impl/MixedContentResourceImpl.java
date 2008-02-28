package org.xbrlapi.impl;

import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.MixedContentResource;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MixedContentResourceImpl extends ResourceImpl implements MixedContentResource {
	
    /**
     * Get the value of the resource as a NodeList containing all of the children 
     * elements and text nodes nested in the resource.
     * @return a list of child nodes of the resource including attributes, 
     * XHTML elements, and text nodes.
     * @throws XBRLException
     * @see org.xbrlapi.MixedContentResource#getContent()
     */
    public NodeList getContent() throws XBRLException {
    	return getDataRootElement().getChildNodes();
    }
    
    /**
     * Set the content of a resource using a vector
     * of DOM nodes as the input.
     * @param nodes The vector of child nodes to insert in
     * the same order as they appear in the supplied vector.
     * @throws XBRLException
     * @see org.xbrlapi.MixedContentResource#setContent(Vector)
     */
    public void setContent(Vector<Node> nodes) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

    /**
     * Get the value of the xml:lang attribute on the resource.
     * @return the string value of the XML language attribute on 
     * the label resource or null if no such attribute is provided.
     * @throws XBRLException
     * @see org.xbrlapi.MixedContentResource#getLanguage()
     */
    public String getLanguage() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttribute("xml:lang")) return null;
    	return root.getAttribute("xml:lang");
    }
    
    /**
     * Set the value of xml:lang attribute on the resource
     * @param language the language code conforming to the requirements
     * set out in <a href="http://www.w3.org/TR/2000/REC-xml-20001006#sec-lang-tag">the XML
     * specification</a>.
     * @throws XBRLException
     * @see org.xbrlapi.MixedContentResource#setLanguage(String)
     */
    public void setLanguage(String language) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }
    
}
