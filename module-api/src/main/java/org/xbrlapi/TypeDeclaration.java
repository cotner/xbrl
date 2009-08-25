package org.xbrlapi;

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
    
}
