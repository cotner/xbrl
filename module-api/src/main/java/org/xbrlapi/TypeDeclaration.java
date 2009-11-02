package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;


/**
 * base interface for all type declarations in XML Schemas.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface TypeDeclaration extends SchemaDeclaration {

    /**
     * @return the final attribute value or null if there is none.
     * One of #all or list of extension and restriction
     * @throws XBRLException
     */
    public String getFinal() throws XBRLException;
    
    /**
     * @return true iff the type declaration cannot be extended.
     * @throws XBRLException
     */
    public boolean isFinalForExtension() throws XBRLException;
    
    /**
     * @return true iff the type declaration cannot be restricted.
     * @throws XBRLException
     */
    public boolean isFinalForRestriction() throws XBRLException;

    /**
     * @return true iff the type declaration cannot be extended or restricted.
     * @throws XBRLException
     */
    public boolean isFinal() throws XBRLException;
    
    /**
     * @param namespace The namespace of the prospective ancestor type.
     * @param name The local name of the prospective ancestor type.
     * @return true if this type declaration is or is derived from the specified ancestor type declaration.
     * @throws XBRLException if there is no type declaration with the given namespace and name.
     */
    public boolean isDerivedFrom(URI namespace, String name) throws XBRLException;
    
    /**
     * 
     * @param candidate the type declaration that this type may be derived from.
     * @return true if this type declaration is or is derived from the specified 
     * ancestor type declaration.
     * @throws XBRLException
     */
    public boolean isDerivedFrom(TypeDeclaration candidate) throws XBRLException;
    
    /**
     * @return the type that this type extends or restricts or null if the type that
     * is extended or restricted is a type defined in the XML Schema specifications.
     * @throws XBRLException
     */
    public TypeDeclaration getParentType() throws XBRLException;

    
    /**
     * @return the namespace of the type that this type declaration extends or restricts.
     * @throws XBRLException
     */
    public URI getParentTypeNamespace() throws XBRLException;
    
    /**
     * @return the local name of the type that this type declaration extends or restricts.
     * @throws XBRLException
     */
    public String getParentTypeLocalname() throws XBRLException;        

    /**
     * @return true if the type is derived from or is an XBRL numeric item type.
     * @throws XBRLException
     */
    public boolean isNumericItemType() throws XBRLException; 
    
}
