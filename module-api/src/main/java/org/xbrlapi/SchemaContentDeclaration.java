package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;


/**
 * Base interface for XML Schema element and attribute declarations
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface SchemaContentDeclaration extends SchemaDeclaration {

    /**
     * @return true iff the element declaration is global rather than
     * part of a complex type declaration.
     * @throws XBRLException
     */
    public boolean isGlobal() throws XBRLException;    
    
    /**
     * @return the value of the form attribute if it is on the attribute declaration
     * and returns null otherwise.
     * @throws XBRLException
     */
    public String getForm() throws XBRLException;    
    
    /**
     * @return null if there is no default attribute, otherwise return its value.
     * @throws XBRLException
     */
    public String getDefault() throws XBRLException;

    /**
     * @return null if there is no fixed attribute, otherwise return its value.
     * @throws XBRLException
     */
    public String getFixed() throws XBRLException;
    
    /**
     * @return true iff the element or attribute is fixed and false otherwise.
     * @throws XBRLException
     */
    public boolean isFixed() throws XBRLException;    

    /**
     * @return true if the content declaration has a named data type and false otherwise.
     * @throws XBRLException if the datatype is not declared by a type attribute.
     */
    public boolean hasTypeReference() throws XBRLException;
    
    /**
     * Retrieve the type namespace.
     * @return the namespace for the data type.
     * @throws XBRLException if the datatype is not declared by a type attribute.
     */
    public URI getTypeNamespace() throws XBRLException;
    
    /**
     * Retrieve the type namespace alias.
     * @return the namespace alias (prefix) or the empty string if there 
     * is no namespace prefix used in the QName.
     * @throws XBRLException if the datatype is not declared.
     */
    public String getTypeNamespaceAlias() throws XBRLException;

    /**
     * Retrieve the type QName.
     * @return the QName used to specify the data type or null
     * if the element has no type attribute.
     * @throws XBRLException if the data root element is not available.
     */
    public String getTypeQName() throws XBRLException;

    /**
     * Retrieve the type local name.
     * @return the local name for the datatype.
     * @throws XBRLException if the datatype is not declared.
     */    
    public String getTypeLocalname() throws XBRLException;

    /**
     * @return The fragment that is the type declaration referred to by the 
     * schema content declaration or null if there is no type declaration.
     * @throws XBRLException if the fragment cannot be found in the data store.
     */
    public TypeDeclaration getTypeDeclaration() throws XBRLException;
    
    
    /**
     * @return true iff the content declaration has its own local simple or complex type declaration.
     * @throws XBRLException if the content declaration has more than one local type declaration.
     */
    public boolean hasLocalType() throws XBRLException;    
    
    /**
     * @return The local type declaration or null if the content 
     * declaration does not have its own local type declaration.
     */
    public TypeDeclaration getLocalType() throws XBRLException;
    
    
}
