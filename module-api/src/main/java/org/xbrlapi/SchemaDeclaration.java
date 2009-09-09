package org.xbrlapi;

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
    
}
