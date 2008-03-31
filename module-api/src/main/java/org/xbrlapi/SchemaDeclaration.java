package org.xbrlapi;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.LinkedList;

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
     * Determine if a concept is abstract
     *
     * @return true if the concept is abstract and false otherwise.
     * @throws XBRLException
     */
    public boolean isAbstract() throws XBRLException;
    


    /**
     * Get the block attribute value
     * One of #all or a list of extension, restriction, substitution
     *
     * @throws XBRLException
     */
    public String getBlock() throws XBRLException;
    


    /**
     * Get the final attribute value.
     * One of #all or list of extension and restriction
     *
     * @throws XBRLException
     */
    public String getFinal() throws XBRLException;
    


    /**
     * Get the id attribute value.
     *
     * @throws XBRLException
     */
    public String getSchemaDeclarationId() throws XBRLException;
    


    /**
     * Get the collection of other non-schema non-xbrl attributes
     *
     * @return a linked list of other attributes
     * @throws XBRLException
     */
    public LinkedList<Node> getOtherAttributes() throws XBRLException;
    




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
    




}
