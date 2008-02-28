package org.xbrlapi;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.LinkedList;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.utilities.XBRLException;

public interface SchemaDeclaration extends SchemaContent {

    /**
     * Get the name of structure being declared.
     * @return the name of the structure being declared.
     * @throws XBRLException
     */
    public String getName() throws XBRLException; 
    
    /**
     * Set the name of the structure being declared
     *
     * @param name The name of the concept
     * @throws XBRLException
     */
    public void setName(String name) throws XBRLException;

    /**
     * Determine if a concept is abstract
     *
     * @return true if the concept is abstract and false otherwise.
     * @throws XBRLException
     */
    public boolean isAbstract() throws XBRLException;
    
    /**
     * Sets the abstract attribute for the concept
     *
     * @param abstractProperty The abstract attribute value
     * @throws XBRLException
     */
    public void setAbstract(boolean abstractProperty) throws XBRLException;

    /**
     * Get the block attribute value
     * One of #all or a list of extension, restriction, substitution
     *
     * @throws XBRLException
     */
    public String getBlock() throws XBRLException;
    
    /**
     * Sets the block attribute for the concept
     * One of #all or a list of extension, restriction, substitution
     *
     * @param block The block attribute value
     * @throws XBRLException
     */
    public void setBlock(String block) throws XBRLException;

    /**
     * Get the final attribute value.
     * One of #all or list of extension and restriction
     *
     * @throws XBRLException
     */
    public String getFinal() throws XBRLException;
    
    /**
     * Sets the default attribute for the concept.
     * One of #all or list of extension and restriction
     *
     * @param value The final attribute value
     * @throws XBRLException
     */
    public void setFinal(String value) throws XBRLException;

    /**
     * Get the id attribute value.
     *
     * @throws XBRLException
     */
    public String getSchemaDeclarationId() throws XBRLException;
    
    /**
     * Sets the id attribute for the concept.
     *
     * @param id The id attribute value
     * @throws XBRLException
     */
    public void setSchemaDeclarationId(String id) throws XBRLException;

    /**
     * Get the collection of other non-schema non-xbrl attributes
     *
     * @return a linked list of other attributes
     * @throws XBRLException
     */
    public LinkedList<Node> getOtherAttributes() throws XBRLException;
    
    /**
     * Set a non-schema non-xbrl attribute value.
     * Mirrors the setAttributeNS method of the org.w3c.dom.Element class.
     *
     * @param namespaceURI
     * @param qualifiedName
     * @param value
     * @throws XBRLException
     */
    public void setOtherAttribute(String namespaceURI, String qualifiedName, String value) throws XBRLException;

    /**
     * Set a non-schema non-xbrl attribute value.
     * Mirrors the setAttributeNode method of the org.w3c.dom.Element class.
     *
     * @param attribute
     * @throws XBRLException
     */
    public void setOtherAttribute(Attr attribute) throws XBRLException;

    /**
     * Determines if concept has a non-schema non-xbrl attribute value.
     * Mirrors the hasAttributeNS method of the org.w3c.dom.Element class.
     *
     * @param namespaceURI The namespace of the attribute being tested for
     * @param localname The local name of the attribute being tested for
     * @throws XBRLException
     */
    public boolean hasOtherAttribute(String namespaceURI, String localname) throws XBRLException;

    /**
     * Removes a non-schema non-xbrl attribute value.
     * Mirrors the removeAttributeNS method of the org.w3c.dom.Element class.
     *
     * @param namespaceURI
     * @param localname
     * @throws XBRLException
     */
    public void removeOtherAttribute(String namespaceURI, String localname) throws XBRLException;

    /**
     * Removes a non-schema non-xbrl attribute value.
     * Mirrors the removeAttributeNode method of the org.w3c.dom.Element class.
     *
     * @param attribute
     * @throws XBRLException
     */
    public void removeOtherAttribute(Attr attribute) throws XBRLException;

    /**
     * Retrieves an array of annotation objects associated with the concept
     * The array is in document order for the XML Schema document containing
     * the concept definition.
     * @return list of SchemaAnnotation fragments or null if the annotations do not exist.
     * @throws XBRLException
     */
    public FragmentList<Fragment> getAnnotations() throws XBRLException;
    
    /**
     * Gets the complex content fragment
     *
     * @throws XBRLException
     */
    public Element getComplexContent() throws XBRLException;
    
    /**
     * Sets the complex content fragment.
     * @param complexContent The XML DOM element that is the complex content model.
     * @throws XBRLException
     */
    public void setComplexContent(Element complexContent) throws XBRLException;



}
