package org.xbrlapi.xdt.aspects;

import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.BaseAspectModel;
import org.xbrlapi.aspects.ConceptAspect;
import org.xbrlapi.aspects.EntityIdentifierAspect;
import org.xbrlapi.aspects.PeriodAspect;
import org.xbrlapi.aspects.UnitAspect;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DimensionalAspectModel extends BaseAspectModel implements AspectModel {

    public DimensionalAspectModel() throws XBRLException {
        super();
        this.setAspect(new ConceptAspect(this));
        this.setAspect(new EntityIdentifierAspect(this));
        this.setAspect(new SegmentRemainderAspect(this));
        this.setAspect(new PeriodAspect(this));
        this.setAspect(new ScenarioRemainderAspect(this));
        this.setAspect(new UnitAspect(this));
        //XDT Aspects are only added as they are required.
    }

    public String getType() {
        return "dimensional";
    }

}
