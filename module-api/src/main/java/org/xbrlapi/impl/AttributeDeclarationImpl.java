package org.xbrlapi.impl;


import org.xbrlapi.AttributeDeclaration;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class AttributeDeclarationImpl extends SchemaContentDeclarationImpl implements AttributeDeclaration {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = 7060377316448918942L;

    /**
     * @see org.xbrlapi.AttributeDeclaration#getUse()
     */
    public String getUse() throws XBRLException {
        if (this.isGlobal()) return null;
        if (! getDataRootElement().hasAttribute("use")) return "optional";
        return getDataRootElement().getAttribute("use");
    }    
    
}