package org.xbrlapi.xdt;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class DimensionImpl extends XDTConceptImpl implements XDTConcept {


    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -1980428745071603911L;

    /**
     * @see Dimension#isTypedDimension()
     */
    public boolean isTypedDimension() {     
        if (getType().equals("org.xbrlapi.xdt.TypedDimensionImpl")) return true;
        return false;
    }
    /**
     * @see Dimension#isExplicitDimension()
     */
    public boolean isExplicitDimension() {
        if (getType().equals("org.xbrlapi.xdt.ExplicitDimensionImpl")) return true;
        return false;
    }
    
}