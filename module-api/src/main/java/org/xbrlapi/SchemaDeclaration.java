package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * Base interface for all kinds of XML Schema declarations
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface SchemaDeclaration extends SchemaContent {

    /**
     * @return the name of the structure being declared or null of the 
     * structure has no name.
     * @throws XBRLException
     */
    public String getName() throws XBRLException; 
    
    /**
     * @return true if the content declaration uses a reference to another XML Schema declaration
     * and false otherwise.
     * @throws XBRLException
     */
    public boolean hasReference() throws XBRLException;
    
    /**
     * @return the namespace for the referenced XML Schema declaration.
     * @throws XBRLException if the datatype is not declared by a type attribute.
     */
    public URI getReferenceNamespace() throws XBRLException;
    
    /**
     * @return the namespace alias (prefix)  for the referenced XML Schema declaration 
     * or the empty string if there is no namespace prefix used in the QName.
     */
    public String getReferenceNamespaceAlias() throws XBRLException;

    /**
     * @return the QName used to specify the identity of the referenced XML Schema
     * declaration.
     * @throws XBRLException if the content declaration does not reference another 
     * XML Schema content declaration using a ref attribute.
     */
    public String getReferenceQName() throws XBRLException;

    /**
     * Retrieve the type local name.
     * @return the local name for the datatype.
     * @throws XBRLException if the datatype is not declared.
     */    
    public String getReferenceLocalname() throws XBRLException;

    /**
     * @return The fragment that is the XML Schema declaration referred to by the 
     * schema content declaration.
     * @throws XBRLException if the fragment cannot be found in the data store.
     */
    public <F extends SchemaDeclaration> F getReferencedSchemaDeclaration() throws XBRLException;
     
}
