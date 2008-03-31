package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * Used for top level element declarations (where the elements are
 * given a name and occur as children of XML Schema schema elements.
 * Other element declarations are not used as roots for fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ElementDeclaration extends SchemaDeclaration {

    /**
     * Determine if a concept is nillable
     *
     * @return true if the concept is nillable and false otherwise.
     * @throws XBRLException
     */
    public boolean isNillable() throws XBRLException;
    


    /**
     * Retrieve the type namespace.
     * @return the namespace for the data type.
     * @throws XBRLException if the datatype is not declared by a type attribute.
     */
    public String getTypeNamespace() throws XBRLException;
    
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
     * Retrieve the substitution group namespace.
     * @return the namespace for the element substitution group.
     * @throws XBRLException if the substitution group is not declared by a substitution group attribute.
     */
    public String getSubstitutionGroupNamespace() throws XBRLException;
    
    /**
     * Retrieve the substitution group namespace alias (also known as a namespace prefix).
     * @return the namespace alias for the element substitution group or the empty string if the default namespace prefix
     * is used.
     * @throws XBRLException if the substitution group is not declared by a substitution group attribute.
     */ 
    public String getSubstitutionGroupNamespaceAlias() throws XBRLException;

    /**
     * Retrieve the substitution group QName.
     * @return the QName used to specify the substitution group or null
     * if the element has no substitutionGroup attribute.
     * @throws XBRLException if the data root element is not available.
     */  
    public String getSubstitutionGroupQName() throws XBRLException;

    /**
     * Retrieve the substitution group local name.
     * @return the local name for the substitution group.
     * @throws XBRLException if the substitution group is not declared.
     */
    public String getSubstitutionGroupLocalname() throws XBRLException;



    /**
     * Get the default attribute value for the element
     * @return null if there is no default attribute, otherwise return its value.
     * @throws XBRLException
     */
    public String getDefault() throws XBRLException;
    


    /**
     * Get the fixed attribute value for the element
     * @return null if there is no fixed attribute, otherwise return its value.
     * @throws XBRLException
     */
    public String getFixed() throws XBRLException;
    


}
