package org.xbrlapi.impl;


import org.xbrlapi.AttributeDeclaration;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class AttributeDeclarationImpl extends SchemaContentDeclarationImpl implements AttributeDeclaration {



    /**
     * @see org.xbrlapi.AttributeDeclaration#getUse()
     */
    public String getUse() throws XBRLException {
        if (this.isGlobal()) return null;
        if (! getDataRootElement().hasAttribute("use")) return "optional";
        return getDataRootElement().getAttribute("use");
    }    
    
}