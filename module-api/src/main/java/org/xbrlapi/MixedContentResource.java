package org.xbrlapi;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.utilities.XBRLException;

public interface MixedContentResource extends Resource {
	
    /**
     * Get the value of the resource as a NodeList containing all of the children 
     * elements and text nodes nested in the resource.
     * @return a list of child nodes of the resource including attributes, 
     * XHTML elements, and text nodes.
     * @throws XBRLException
     */
    public NodeList getContent() throws XBRLException;
	
    /**
     * Set the content of a resource using a vector
     * of DOM nodes as the input.
     * @param nodes The vector of child nodes to insert in
     * the same order as they appear in the supplied vector.
     * @throws XBRLException
     */
    public void setContent(Vector<Node> nodes) throws XBRLException;

    /**
     * Get the value of the xml:lang attribute on the resource.
     * @return the string value of the XML language attribute on 
     * the label resource or null if no such attribute is provided.
     * @throws XBRLException
     */
    public String getLanguage() throws XBRLException;
    
    /**
     * Set the value of xml:lang attribute on the resource
     * @param language the language code conforming to the requirements
     * set out in <a href="http://www.w3.org/TR/2000/REC-xml-20001006#sec-lang-tag">the XML
     * specification</a>.
     * @throws XBRLException
     */
    public void setLanguage(String language) throws XBRLException;

}
