package org.xbrlapi.xdt;

import org.xbrlapi.FragmentList;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class HypercubeImpl extends XDTConceptImpl implements Hypercube {

    /**
     * @see org.xbrlapi.xdt.Hypercube#getDimensions()
     */
    public FragmentList<Dimension> getDimensions() throws XBRLException {
        return new FragmentListImpl<Dimension>();
    }    
}