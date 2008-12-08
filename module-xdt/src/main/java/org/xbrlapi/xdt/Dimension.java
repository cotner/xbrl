package org.xbrlapi.xdt;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Dimension extends XDTConcept {

    /**
     * @return true if the dimension is an explicit dimension and
     * false otherwise.
     */
    public boolean isExplicitDimension();    

    /**
     * @return true if the dimension is a typed dimension and
     * false otherwise.
     */
    public boolean isTypedDimension();    
    
}
