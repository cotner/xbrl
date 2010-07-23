package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.CachingLabeller;
import org.xbrlapi.aspects.alt.ConceptAspect;
import org.xbrlapi.aspects.alt.ConceptDomain;
import org.xbrlapi.aspects.alt.ConceptLabeller;
import org.xbrlapi.aspects.alt.EntityAspect;
import org.xbrlapi.aspects.alt.EntityDomain;
import org.xbrlapi.aspects.alt.EntityLabeller;
import org.xbrlapi.aspects.alt.LabelCache;
import org.xbrlapi.aspects.alt.LocationAspect;
import org.xbrlapi.aspects.alt.LocationDomain;
import org.xbrlapi.aspects.alt.LocationLabeller;
import org.xbrlapi.aspects.alt.PeriodAspect;
import org.xbrlapi.aspects.alt.PeriodDomain;
import org.xbrlapi.aspects.alt.PeriodLabeller;
import org.xbrlapi.aspects.alt.UnitAspect;
import org.xbrlapi.aspects.alt.UnitDomain;
import org.xbrlapi.aspects.alt.UnitLabeller;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.ExplicitDimensionImpl;
import org.xbrlapi.xdt.TypedDimension;
import org.xbrlapi.xdt.TypedDimensionImpl;

/**
 * This is the same as the dimensional aspect model except that it 
 * also sets up the store-based caching aspect value labeller system
 * automatically as part of the constructor.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DimensionalAspectModelWithCachingLabellers extends DimensionalAspectModel implements AspectModel {

    /**
     * 
     */
    private static final long serialVersionUID = -1267021539832407713L;
    
    private static final Logger logger = Logger.getLogger(DimensionalAspectModelWithCachingLabellers.class);
    
    /**
     * @param store The data store.
     * @param cache The aspect value label cache to use.
     * @throws XBRLException if a parameter is null.
     */
    public DimensionalAspectModelWithCachingLabellers(Store store, LabelCache cache) throws XBRLException {
        super(store);
        if (cache == null) throw new XBRLException("The label cache must not be null.");
        
        addAspect(new LocationAspect(new LocationDomain(store)));
        addAspect(new ConceptAspect(new ConceptDomain(store)));
        addAspect(new UnitAspect(new UnitDomain(store)));
        addAspect(new PeriodAspect(new PeriodDomain(store)));
        addAspect(new EntityAspect(new EntityDomain(store)));
        addAspect(new SegmentRemainderAspect(new SegmentRemainderDomain(store)));
        addAspect(new ScenarioRemainderAspect(new ScenarioRemainderDomain(store)));
        
        for (ExplicitDimension dimension: getStore().<ExplicitDimension>getXMLResources(ExplicitDimensionImpl.class)) {
            URI dimensionNamespace = dimension.getTargetNamespace();
            String dimensionLocalname = dimension.getName();
            ExplicitDimensionDomain domain = new ExplicitDimensionDomain(store,dimensionNamespace,dimensionLocalname);
            Aspect aspect = new ExplicitDimensionAspect(domain,dimensionNamespace,dimensionLocalname);
            if (! hasAspect(aspect.getId())) addAspect(aspect);
        }
        
        for (TypedDimension dimension: store.<TypedDimension>getXMLResources(TypedDimensionImpl.class)) {
            URI dimensionNamespace = dimension.getTargetNamespace();
            String dimensionLocalname = dimension.getName();
            TypedDimensionDomain domain = new TypedDimensionDomain(store,dimensionNamespace,dimensionLocalname);
            Aspect aspect = new TypedDimensionAspect(domain,dimensionNamespace,dimensionLocalname);
            if (! hasAspect(aspect.getId())) addAspect(aspect);
        }
        
        this.setLabeller(LocationAspect.ID, new CachingLabeller(cache, new LocationLabeller(this.getAspect(LocationAspect.ID))));
        this.setLabeller(ConceptAspect.ID, new CachingLabeller(cache, new ConceptLabeller(this.getAspect(ConceptAspect.ID))));
        this.setLabeller(UnitAspect.ID, new CachingLabeller(cache, new UnitLabeller(this.getAspect(UnitAspect.ID))));
        this.setLabeller(PeriodAspect.ID, new CachingLabeller(cache, new PeriodLabeller(this.getAspect(PeriodAspect.ID))));
        this.setLabeller(EntityAspect.ID, new CachingLabeller(cache, new EntityLabeller(this.getAspect(EntityAspect.ID))));
        this.setLabeller(SegmentRemainderAspect.ID, new CachingLabeller(cache, new SegmentRemainderLabeller(this.getAspect(SegmentRemainderAspect.ID))));
        this.setLabeller(ScenarioRemainderAspect.ID, new CachingLabeller(cache, new ScenarioRemainderLabeller(this.getAspect(ScenarioRemainderAspect.ID))));

        for (Aspect aspect: this.getTypedDimensionAspects()) {
            this.setLabeller(aspect.getId(), new CachingLabeller(cache, new TypedDimensionLabeller(this.getAspect(aspect.getId()))));
        }
        
        for (Aspect aspect: this.getExplicitDimensionAspects()) {
            this.setLabeller(aspect.getId(), new CachingLabeller(cache, new ExplicitDimensionLabeller(this.getAspect(aspect.getId()))));
        }
    }

}
