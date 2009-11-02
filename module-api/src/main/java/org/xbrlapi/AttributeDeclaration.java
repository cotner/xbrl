package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;


/**
 * Used for attribute declaration fragments
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface AttributeDeclaration extends SchemaContentDeclaration {

    /**
     * @return the use attribute value (defaults to optional if missing on a non-global declaration) 
     * for the attribute declaration.  Null if the attribute declaration is global.
     * @throws XBRLException
     */
    public String getUse() throws XBRLException;
    
}
