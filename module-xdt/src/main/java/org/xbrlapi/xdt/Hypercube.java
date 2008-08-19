package org.xbrlapi.xdt;

import org.xbrlapi.FragmentList;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Hypercube extends XDTConcept {

    /**
     * @return a list of the hypercube's XDT dimensions.
     * @throws XBRLException
     */
    public FragmentList<Dimension> getDimensions() throws XBRLException;
    
}
