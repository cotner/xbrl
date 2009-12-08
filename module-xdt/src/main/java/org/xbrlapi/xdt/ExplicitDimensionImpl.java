package org.xbrlapi.xdt;

import java.util.List;

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
        List<Concept> defaults = getStore().<Concept>getTargets(this.getIndex(),null,XDTConstants.DefaultDimensionArcrole);
        if (defaults.size() == 0) throw new XBRLException("There are no defaults for explicit dimension " + this.getTargetNamespace() + ":" + this.getName());
        if (defaults.size() > 1) throw new XBRLException("There are multiple defaults for explicit dimension " + this.getTargetNamespace() + ":" + this.getName());
        return defaults.get(0);
    }

    /**
     * @see org.xbrlapi.xdt.ExplicitDimension#hasDefaultDomainMember()
     */
    public boolean hasDefaultDomainMember() throws XBRLException {
        List<Concept> defaults = getStore().<Concept>getTargets(this.getIndex(),null,XDTConstants.DefaultDimensionArcrole);
        return (defaults.size() == 1);
    }
    
}