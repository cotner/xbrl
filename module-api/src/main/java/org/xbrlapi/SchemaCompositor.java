package org.xbrlapi;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;


/**
 * base interface for xsd:choice and xsd:selection element fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface SchemaCompositor extends SchemaContent {

    /**
     * @return the list of all child element declaration fragments
     * One of #all or list of extension and restriction
     * @throws XBRLException
     */
    public List<ElementDeclaration> getMembers() throws XBRLException;    
    
}
