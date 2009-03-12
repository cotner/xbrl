package org.xbrlapi.xdt;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Hypercube extends XDTConcept {

    /**
     * @return a list of the hypercube's XDT dimensions.
     * @throws XBRLException
     */
    public List<Dimension> getDimensions() throws XBRLException;
    
}
