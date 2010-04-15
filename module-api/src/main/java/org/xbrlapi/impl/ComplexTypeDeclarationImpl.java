package org.xbrlapi.impl;

import org.xbrlapi.ComplexTypeDeclaration;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ComplexTypeDeclarationImpl extends TypeDeclarationImpl implements ComplexTypeDeclaration {	

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = 4929601557073607819L;

    /**
     * @see org.xbrlapi.ComplexTypeDeclaration#isAbstract()
     */
    public boolean isAbstract() throws XBRLException {
        if (getDataRootElement().getAttribute("abstract").equals("true")) {
            return true;
        }
        return false;
    }
    
    /**
     * @see org.xbrlapi.ComplexTypeDeclaration#isMixed()
     */
    public boolean isMixed() throws XBRLException {
        return getDataRootElement().getAttribute("mixed").equals("true");
    }

    /**
     * @see org.xbrlapi.ComplexTypeDeclaration#isFinalForRestriction()
     */
    public boolean isFinalForRestriction() throws XBRLException {
        String value = getDataRootElement().getAttribute("final");
        if (value.matches("restriction")) return true;
        return value.equals("#all");
    }
    
    /**
     * @see org.xbrlapi.ComplexTypeDeclaration#isFinalForRestriction()
     */
    public boolean isFinalForExtension() throws XBRLException {
        String value = getDataRootElement().getAttribute("final");
        if (value.matches("extension")) return true;
        return value.equals("#all");
    }            
    
    /**
     * @see org.xbrlapi.ComplexTypeDeclaration#isBlockingRestriction()
     */
    public boolean isBlockingRestriction() throws XBRLException {
        String value = getDataRootElement().getAttribute("block");
        if (value.matches("restriction")) return true;
        return value.equals("#all");
    }
    
    /**
     * @see org.xbrlapi.ComplexTypeDeclaration#isBlockingRestriction()
     */
    public boolean isBlockingExtension() throws XBRLException {
        String value = getDataRootElement().getAttribute("block");
        if (value.matches("extension")) return true;
        return value.equals("#all");
    }               
    
}