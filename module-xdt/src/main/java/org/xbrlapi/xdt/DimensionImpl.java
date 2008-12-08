package org.xbrlapi.xdt;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class DimensionImpl extends XDTConceptImpl implements XDTConcept {


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