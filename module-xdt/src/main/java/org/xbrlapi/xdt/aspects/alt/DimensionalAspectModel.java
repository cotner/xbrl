package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.AspectModelImpl;
import org.xbrlapi.aspects.alt.ConceptAspect;
import org.xbrlapi.aspects.alt.ConceptDomain;
import org.xbrlapi.aspects.alt.EntityAspect;
import org.xbrlapi.aspects.alt.EntityDomain;
import org.xbrlapi.aspects.alt.LocationAspect;
import org.xbrlapi.aspects.alt.LocationDomain;
import org.xbrlapi.aspects.alt.PeriodAspect;
import org.xbrlapi.aspects.alt.PeriodDomain;
import org.xbrlapi.aspects.alt.UnitAspect;
import org.xbrlapi.aspects.alt.UnitDomain;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.ExplicitDimensionImpl;
import org.xbrlapi.xdt.TypedDimension;
import org.xbrlapi.xdt.TypedDimensionImpl;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DimensionalAspectModel extends AspectModelImpl implements AspectModel {

    /**
     * 
     */
    private static final long serialVersionUID = -5012949679199322397L;
    
    private static final Logger logger = Logger.getLogger(DimensionalAspectModel.class);
    
    /**
     * @param store The data store.
     * @throws XBRLException if the data store is null.
     */
    public DimensionalAspectModel(Store store) throws XBRLException {
        super(store);
        
        addAspect(new LocationAspect(new LocationDomain(store)));
        addAspect(new ConceptAspect(new ConceptDomain(store)));
        addAspect(new UnitAspect(new UnitDomain()));
        addAspect(new PeriodAspect(new PeriodDomain()));
        addAspect(new EntityAspect(new EntityDomain()));
        addAspect(new SegmentRemainderAspect(new SegmentRemainderDomain()));
        addAspect(new ScenarioRemainderAspect(new ScenarioRemainderDomain()));
        
        for (ExplicitDimension dimension: store.<ExplicitDimension>getXMLResources(ExplicitDimensionImpl.class)) {
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
        
    }

    public List<Aspect> getExplicitDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
            if (aspect.getClass().equals(ExplicitDimensionAspect.class))
                result.add(aspect);
        }
        return result;
    }
    
    public List<Aspect> getTypedDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
            if (aspect.getClass().equals(TypedDimensionAspect.class))
                result.add(aspect);
        }
        return result;
    }
    
    public List<Aspect> getDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
           if (aspect.getClass().equals(ExplicitDimensionAspect.class) || aspect.getClass().equals(TypedDimensionAspect.class)) 
               result.add(aspect);
       }
       return result;
    }
}
