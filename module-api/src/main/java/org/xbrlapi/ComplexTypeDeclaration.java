package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;


/**
 * Used for complex type declarations in XML Schemas.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ComplexTypeDeclaration extends TypeDeclaration {

    /**
     * @return true if the complex type is abstract and false otherwise.
     * @throws XBRLException
     */
    public boolean isAbstract() throws XBRLException;
    
    /**
     * @return true if the complex type allows mixed content.
     * @throws XBRLException
     */
    public boolean isMixed() throws XBRLException;    
    
    /**
     * @return true iff the complex type is final for extension.
     * @throws XBRLException
     */
    public boolean isFinalForExtension() throws XBRLException;    
    
    /**
     * @return true iff the complex type is final for restriction.
     * @throws XBRLException
     */
    public boolean isFinalForRestriction() throws XBRLException;
    
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
}
