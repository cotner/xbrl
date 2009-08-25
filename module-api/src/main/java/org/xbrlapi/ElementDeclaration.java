package org.xbrlapi;

import java.net.URI;

import org.w3c.dom.Element;
import org.xbrlapi.utilities.XBRLException;

/**
 * Used for top level element declarations (where the elements are
 * given a name and occur as children of XML Schema schema elements.
 * Other element declarations are not used as roots for fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ElementDeclaration extends SchemaContentDeclaration {

    /**
     * @return true if the element is abstract and false otherwise.
     * @throws XBRLException
     */
    public boolean isAbstract() throws XBRLException;
    


    
    /**
     * @return true iff the element is final for extension.
     * @throws XBRLException
     */
    public boolean isFinalForExtension() throws XBRLException;    
    
    /**
     * @return true iff the element is final for restriction.
     * @throws XBRLException
     */
    public boolean isFinalForRestriction() throws XBRLException;
    
    /**
     * @return true iff the element is blocking substitution.
     * @throws XBRLException
     */
    public boolean isBlockingSubstitution() throws XBRLException;
    
    /**
     * @return true iff the element is blocking extension.
     * @throws XBRLException
     */
    public boolean isBlockingExtension() throws XBRLException;    
    
    /**
     * @return true iff the element is blocking restriction.
     * @throws XBRLException
     */
    public boolean isBlockingRestriction() throws XBRLException;    
    
    
    /**
     * Gets the complex content fragment
     *
     * @throws XBRLException
     */
    public Element getComplexContent() throws XBRLException;    
    
    /**
     * Determine if a concept is nillable
     *
     * @return true if the concept is nillable and false otherwise.
     * @throws XBRLException
     */
    public boolean isNillable() throws XBRLException;

    /**
     * @return true if the content declaration has a named substitution group and false otherwise.
     * @throws XBRLException
     */
    public boolean hasSubstitutionGroup() throws XBRLException;
    
    /**
     * Retrieve the substitution group namespace.
     * @return the namespace for the element substitution group.
     * @throws XBRLException if the substitution group is not declared by a substitution group attribute.
     */
    public URI getSubstitutionGroupNamespace() throws XBRLException;
    
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
     * @return true if the element declaration is for an element 
     * in the substitution group for the XBRL item element and
     * false otherwise.
     * @throws XBRLException
     */
     public boolean isItem() throws XBRLException;
     
     /**
      * @return true if the element declaration is for an element 
      * in the substitution group for the XBRL tuple element and
      * false otherwise.
      * @throws XBRLException
      */
     public boolean isTuple() throws XBRLException;

     /**
      * @return true iff the element declaration has a single local complex type.
      * @throws XBRLException
      */
     public boolean hasLocalComplexType() throws XBRLException;
     
     /**
      * @return the local complex type definition.
      * @throws XBRLException if there is no local complex type declaration or
      * if there is more than one local complex type declaration (an XML Schema error).
      */
     public ComplexTypeDeclaration getLocalComplexType() throws XBRLException;     


     
     /**
      * @return the maximum number of times that the element can occur within a complex type definition.
      * @throws XBRLException if the element declaration is global.
      */
     public String getMaxOccurs() throws XBRLException;
     
     /**
      * @return the minimum number of times that the element can occur within a complex type definition.
      * @throws XBRLException if the element declaration is global.
      */
     public String getMinOccurs() throws XBRLException;     
}
