package org.xbrlapi.aspects.alt;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * This is the same as the standard aspect model except that it 
 * also sets up the store-based caching aspect value labeller system
 * automatically as part of the constructor.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class StandardAspectModelWithCachingLabellers extends StandardAspectModel implements AspectModel {

    /**
     * 
     */
    private static final long serialVersionUID = 5288184252388411327L;
    
    private static final Logger logger = Logger.getLogger(StandardAspectModelWithCachingLabellers.class);
    
    /**
     * @param store The data store.
     * @param cache The aspect value label cache to use.
     * @throws XBRLException if a parameter is null.
     */
    public StandardAspectModelWithCachingLabellers(Store store, LabelCache cache) throws XBRLException {
        super(store);
        if (cache == null) throw new XBRLException("The label cache must not be null.");
        
        addAspect(new LocationAspect(new LocationDomain(store)));
        addAspect(new ConceptAspect(new ConceptDomain(store)));
        addAspect(new UnitAspect(new UnitDomain(store)));
        addAspect(new PeriodAspect(new PeriodDomain(store)));
        addAspect(new EntityAspect(new EntityDomain(store)));
        addAspect(new SegmentAspect(new SegmentDomain(store)));
        addAspect(new ScenarioAspect(new ScenarioDomain(store)));
        
        this.setLabeller(LocationAspect.ID, new CachingLabeller(cache, new LocationLabeller(this.getAspect(LocationAspect.ID))));
        this.setLabeller(ConceptAspect.ID, new CachingLabeller(cache, new ConceptLabeller(this.getAspect(ConceptAspect.ID))));
        this.setLabeller(UnitAspect.ID, new CachingLabeller(cache, new UnitLabeller(this.getAspect(UnitAspect.ID))));
        this.setLabeller(PeriodAspect.ID, new CachingLabeller(cache, new PeriodLabeller(this.getAspect(PeriodAspect.ID))));
        this.setLabeller(EntityAspect.ID, new CachingLabeller(cache, new EntityLabeller(this.getAspect(EntityAspect.ID))));
        this.setLabeller(SegmentAspect.ID, new CachingLabeller(cache, new SegmentLabeller(this.getAspect(SegmentAspect.ID))));
        this.setLabeller(ScenarioAspect.ID, new CachingLabeller(cache, new ScenarioLabeller(this.getAspect(ScenarioAspect.ID))));

    }

    
    

    

    

}
