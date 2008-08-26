package org.xbrlapi.xdt;

import org.xbrlapi.Concept;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ExplicitDimensionImpl extends DimensionImpl implements ExplicitDimension {

    /**
     * @see org.xbrlapi.xdt.ExplicitDimension#getDefaultDomainMember()
     */
    public Concept getDefaultDomainMember() throws XBRLException {
        throw new XBRLException("This method is not yet implemented.");
    }

    /**
     * @see org.xbrlapi.xdt.ExplicitDimension#hasDefaultDomainMember()
     */
    public boolean hasDefaultDomainMember() throws XBRLException {
        throw new XBRLException("This method is not yet implemented.");
    }
    
}