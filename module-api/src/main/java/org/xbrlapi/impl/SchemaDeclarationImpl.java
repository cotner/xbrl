package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.SchemaDeclaration;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaDeclarationImpl extends SchemaContentImpl implements SchemaDeclaration {
	
    /**
     * Get the name of structure being declared.
     * @return the name of the structure being declared.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getName()
     */
    public String getName() throws XBRLException {
        Element element = getDataRootElement();
        if (element.hasAttribute("name")) return element.getAttribute("name");
        return null;
    }
    



    



    

    



    


}