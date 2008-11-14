package org.xbrlapi.aspects;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class NonDimensionalAspectModel extends BaseAspectModel implements AspectModel {

    public NonDimensionalAspectModel() throws XBRLException {
        super();
        this.setOrphanAspect(new ConceptAspect(this));
        this.setOrphanAspect(new EntityIdentifierAspect(this));
        this.setOrphanAspect(new SegmentAspect(this));
        this.setOrphanAspect(new PeriodAspect(this));
        this.setOrphanAspect(new ScenarioAspect(this));
        this.setOrphanAspect(new UnitAspect(this));
    }

    public String getType() {
        return AspectModel.NONDIMENSIONAL;
    }

}
