package org.xbrlapi.builder;

import java.net.URI;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;

/**
 * Methods exposed by the fragment builder.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface Builder {
	
	/**
	 * Get the Document object used to build the fragment.
	 * @return the data XML structure.
	 * @throws XBRLException if the data has not been built yet.
	 */
	public Element getData() throws XBRLException;
	
	/**
	 * Get the metadata DOM document.
	 * @return the metadata XML structure.
	 */
	public Element getMetadata();

    /**
     * Get the insertion point for new content.
     * @return The XML DOM Element to contain new content.
     */
    public Element getInsertionPoint();
    
	/**
	 * Returns true iff the builder has not yet added an element to the fragment.
	 * @return true iff the builder has not yet added an element to the fragment.
	 */
	public boolean isNewFragment();
    
	/**
	 * Append a text node.
	 * @param text The node to be appended.
	 * @throws XBRLException if the node cannot be appended.
	 */
	public void appendText(String text) throws XBRLException;
	
	/**
	 * Append a processing instruction node
	 * @param target The processing target application identifier.
	 * @param data The data defining what is to be done.
	 * @throws XBRLException if the node cannot be appended.
	 */
	public void appendProcessingInstruction(String target, String data) throws XBRLException;
	
	/**
	 * Append a comment node.
	 * @param text The data constituting the content of the comment.
	 * @throws XBRLException if the node cannot be appended.
	 */
	public void appendComment(String text) throws XBRLException;

	/**
	 * Append an element node.
	 * @param namespaceURI The namespace of the element found by the SAX parser.
	 * @param lName The local name of the element found by the SAX parser.
	 * @param qName The QName of the element found by the SAX parser.
	 * @param attrs The set of attributes found by the SAX parser.
	 * @throws XBRLException if the node cannot be appended.
	 */
	public void appendElement(
			URI namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs) throws XBRLException;
	
	/**
	 * Insert a new element without attributes.
	 * @param namespaceURI The namespace of the element found by the SAX parser.
	 * @param lName The local name of the element found by the SAX parser.
	 * @param qName The QName of the element found by the SAX parser.
	 * @throws XBRLException if the node cannot be appended.
	 */
	public void appendElement(
			URI namespaceURI, 
			String lName, 
			String qName) throws XBRLException;
	
	/**
	 * Update the insertion point for new content when reaching 
	 * the end of an element.
	 * TODO try to make endElement this a private method.
	 * @param namespaceURI The namespace URI of the element that is ending.
	 * @param lName The local name of the element that is ending.
	 * @param qName The QName of the element that is ending.
	 * @throws XBRLException if the current insertion point is not an 
	 * element node or if the new (parent) insertion point is not an element node.
	 */
	public void endElement(
			String namespaceURI,
			String lName,
			String qName
			) throws XBRLException;
	
	/**
	 * Append a notation declaration.
	 */
	public void appendNotationDecl(
			String name, 
			String publicId, 
			String systemId
			) throws XBRLException;
	
	/**
	 * Append an unparsed entity declaration.
	 */
	public void appendUnparsedEntityDecl(
			String name, 
			String publicId,
			String systemId, 
			String notationName) 
	throws XBRLException;

	/**
	 * Append an element DTD declaration.
	 */
	public void appendElementDecl(
			String name, 
			String model
			) throws XBRLException;
	
	/**
	 * Append an internal entity DTD declaration.
	 */
	public void appendInternalEntityDecl(
			String name, 
			String value
			) throws XBRLException;

	/**
	 * Append an external entity DTD declarations
	 */
	public void appendExternalEntityDecl(
			String name, 
			String publicId, 
			String systemId
			) throws XBRLException;
	/**
	 * Append an attribute DTD declaration
	 */
	public void appendAttributeDecl(
			String eName, 
			String aName, 
			String type,
			String valueDefault, 
			String value
			) throws XBRLException;

    //===========================================================
    // Metadata construction methods
    //===========================================================	
	
	/**
	 * Set a metadata attribute.
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 **/
	public void setMetaAttribute(String name, String value);
	
	/**
	 * Get a metadata attribute.
	 * @param name The name of the attribute.
	 * @return the string value of the metadata attribute or 
	 * null if it is not specified.
	 **/
	public String getMetaAttribute(String name);
	/**
	 * Remove a metadata attribute.
	 * @param name The name of the attribute.
	 **/
	public void removeMetaAttribute(String name);
	
    /**
     * Appends a child element to the root metadata element.
     * @param eName Name of the element to be added (no namespaces are used).
     * @param attributes A hashmap from attribute name keys to attribute values.
     * @throws XBRLException if the metadata element cannot be appended.
     */
    public void appendMetadataElement(String eName, HashMap<String,String> attributes) throws XBRLException;
    
    /**
     * Removes a child element from the metadata root element by 
     * specifying the name of the child and the value of the element's 
     * text content and/or the value of a named attribute.  All specified 
     * information must match for the deletion to succeed.
     * @param eName Name of the element to be added (no namespaces are used).
     * @param attributes A hashmap from attribute name keys to attribute values.
     * @throws XBRLException if the metadata element cannot be removed.
     */
    public void removeMetadataElement(String eName, HashMap<String,String> attributes) throws XBRLException;
}
