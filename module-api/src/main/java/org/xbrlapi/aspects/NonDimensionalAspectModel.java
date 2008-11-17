package org.xbrlapi.aspects;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class NonDimensionalAspectModel extends BaseAspectModel implements AspectModel {

    public NonDimensionalAspectModel() throws XBRLException {
        super();
        this.setAspect(new ConceptAspect(this));
        this.setAspect(new EntityIdentifierAspect(this));
        this.setAspect(new SegmentAspect(this));
        this.setAspect(new PeriodAspect(this));
        this.setAspect(new ScenarioAspect(this));
        this.setAspect(new UnitAspect(this));
    }

    public String getType() {
        return AspectModel.NONDIMENSIONAL;
    }
    

        

}
