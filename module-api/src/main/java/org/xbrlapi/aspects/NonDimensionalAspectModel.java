package org.xbrlapi.aspects;

import org.xbrlapi.Fact;
import org.xbrlapi.Item;
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
    
    /**
     * @see AspectModel#addFact(Fact)
     */
    public void addFact(Fact fact) throws XBRLException {
        Aspect aspect = this.getAspect(Aspect.CONCEPT);
        aspect.addFact(fact);
        if (fact.isTuple()) return;
        Item item = (Item) fact;
        
        
        
    }
        

}
