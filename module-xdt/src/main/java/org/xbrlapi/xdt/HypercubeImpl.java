package org.xbrlapi.xdt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class HypercubeImpl extends XDTConceptImpl implements Hypercube {

    /**
     * @see org.xbrlapi.xdt.Hypercube#getDimensions()
     */
    public List<Dimension> getDimensions() throws XBRLException {
        return new Vector<Dimension>();
    }    
}