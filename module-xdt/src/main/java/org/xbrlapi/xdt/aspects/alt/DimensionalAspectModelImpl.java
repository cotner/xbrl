package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Item;
import org.xbrlapi.NumericItem;
import org.xbrlapi.Period;
import org.xbrlapi.Scenario;
import org.xbrlapi.Segment;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.AspectModelImpl;
import org.xbrlapi.aspects.alt.AspectValue;
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
import org.xbrlapi.xdt.XDTConstants;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DimensionalAspectModelImpl extends AspectModelImpl implements DimensionalAspectModel {


    /**
     * 
     */
    private static final long serialVersionUID = -525899452312079180L;
    private static final Logger logger = Logger.getLogger(DimensionalAspectModelImpl.class);
    
    /**
     * @param store The data store.
     * @throws XBRLException if the data store is null.
     */
    public DimensionalAspectModelImpl(Store store) throws XBRLException {
        super(store);
        
    }

    /**
     * @see AspectModel#initialise()
     */
    public void initialise() throws XBRLException {
        super.initialise();
        addAspect(new LocationAspect(new LocationDomain(getStore())));
        addAspect(new ConceptAspect(new ConceptDomain(getStore())));
        addAspect(new UnitAspect(new UnitDomain(getStore())));
        addAspect(new PeriodAspect(new PeriodDomain(getStore())));
        addAspect(new EntityAspect(new EntityDomain(getStore())));
        addAspect(new SegmentRemainderAspect(new SegmentRemainderDomain(getStore())));
        addAspect(new ScenarioRemainderAspect(new ScenarioRemainderDomain(getStore())));
        
        for (ExplicitDimension dimension: getStore().<ExplicitDimension>getXMLResources(ExplicitDimensionImpl.class)) {
            URI dimensionNamespace = dimension.getTargetNamespace();
            String dimensionLocalname = dimension.getName();
            ExplicitDimensionDomain domain = new ExplicitDimensionDomain(getStore(),dimensionNamespace,dimensionLocalname);
            Aspect aspect = new ExplicitDimensionAspect(domain,dimensionNamespace,dimensionLocalname);
            if (! hasAspect(aspect.getId())) addAspect(aspect);
        }
        
        for (TypedDimension dimension: getStore().<TypedDimension>getXMLResources(TypedDimensionImpl.class)) {
            URI dimensionNamespace = dimension.getTargetNamespace();
            String dimensionLocalname = dimension.getName();
            TypedDimensionDomain domain = new TypedDimensionDomain(getStore(),dimensionNamespace,dimensionLocalname);
            Aspect aspect = new TypedDimensionAspect(domain,dimensionNamespace,dimensionLocalname);
            if (! hasAspect(aspect.getId())) addAspect(aspect);
        }
        
    }    
    
    /**
     * Makes the discovery of aspect values for a fact much more efficient than the default
     * by exploiting knowledge of the sources of information about aspect values given the fact.
     * @see AspectModel#getAspectValues(Fact)
     */
    @Override
    public Map<URI,AspectValue> getAspectValues(Fact fact) throws XBRLException {

        Map<URI,AspectValue> result = new HashMap<URI,AspectValue>();

        result.put(ConceptAspect.ID, ((ConceptAspect)getAspect(ConceptAspect.ID)).getValue(fact) );
        
        Fragment parent = fact.getParent();
        result.put(LocationAspect.ID, ((LocationAspect)getAspect(LocationAspect.ID)).getValue(fact,parent) );

        if (fact.isNil() || fact.isTuple()) {
            result.put(EntityAspect.ID, getAspect(EntityAspect.ID).getMissingValue() );
            result.put(PeriodAspect.ID, getAspect(PeriodAspect.ID).getMissingValue() );
            result.put(SegmentRemainderAspect.ID, getAspect(SegmentRemainderAspect.ID).getMissingValue() );
            result.put(ScenarioRemainderAspect.ID, getAspect(ScenarioRemainderAspect.ID).getMissingValue() );
            result.put(UnitAspect.ID, getAspect(UnitAspect.ID).getMissingValue() );
        } else {
        
            Item item = (Item) fact;
            
            Context context = item.getContext();
            Entity entity = context.getEntity();
            Period period = context.getPeriod();
            Scenario scenario = context.getScenario();
            Segment segment = entity.getSegment();
            
            result.put(EntityAspect.ID, ((EntityAspect)getAspect(EntityAspect.ID)).getValue(entity) );
            result.put(PeriodAspect.ID, ((PeriodAspect)getAspect(PeriodAspect.ID)).getValue(period) );
            result.put(SegmentRemainderAspect.ID, ((SegmentRemainderAspect)getAspect(SegmentRemainderAspect.ID)).getValue(segment) );
            result.put(ScenarioRemainderAspect.ID, ((ScenarioRemainderAspect)getAspect(ScenarioRemainderAspect.ID)).getValue(scenario) );

            if (segment != null) {
                List<Element> children = segment.getChildElements();
                for (Element child: children) {
                    if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString())) {
                        String dimensionQName = child.getAttribute("dimension");
                        if (! dimensionQName.equals("")) {
                            URI candidateNamespace = segment.getNamespaceFromQName(dimensionQName,child);
                            String candidateLocalname = segment.getLocalnameFromQName(dimensionQName);
                            URI id = URI.create(candidateNamespace + "#" + candidateLocalname);
                            if (this.hasAspect(id)) {
                                Aspect aspect = this.getAspect(id);
                                if (aspect instanceof ExplicitDimensionAspect) {
                                    result.put( id, ((ExplicitDimensionAspect) aspect).getValue(segment, child) );
                                } else {
                                    result.put( id, ((TypedDimensionAspect) aspect).getValue(child) );
                                }
                            }
                        }
                    }
                }
            }
            if (scenario != null) {
                List<Element> children = segment.getChildElements();
                for (Element child: children) {
                    if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString())) {
                        String dimensionQName = child.getAttribute("dimension");
                        if (! dimensionQName.equals("")) {
                            URI candidateNamespace = segment.getNamespaceFromQName(dimensionQName,child);
                            String candidateLocalname = segment.getLocalnameFromQName(dimensionQName);
                            URI id = URI.create(candidateNamespace + "#" + candidateLocalname);
                            if (this.hasAspect(id)) {
                                Aspect aspect = this.getAspect(id);
                                if (aspect instanceof ExplicitDimensionAspect) {
                                    result.put( id, ((ExplicitDimensionAspect) aspect).getValue(segment, child) );
                                } else {
                                    result.put( id, ((TypedDimensionAspect) aspect).getValue(child) );
                                }
                            }
                        }
                    }
                }
            }

            for (Aspect aspect: this.getExplicitDimensionAspects()) {
                if (! result.containsKey(aspect.getId())) result.put(aspect.getId(), ((ExplicitDimensionAspect) aspect).getDefaultValue(getStore()));
            }

            for (Aspect aspect: this.getTypedDimensionAspects()) {
                if (! result.containsKey(aspect.getId())) result.put(aspect.getId(), aspect.getMissingValue());
            }

            if (item.isNumeric()) 
                result.put(UnitAspect.ID, ((UnitAspect)getAspect(UnitAspect.ID)).getValue(((NumericItem) item).getUnit()) );
            else 
                result.put(UnitAspect.ID, getAspect(UnitAspect.ID).getMissingValue());
        }   
        // Return the map of aspect values, filling in gaps for any aspects other than those dealt with above.
        return this.getAspectValues(fact,result);
        
    }    
    
    public List<Aspect> getExplicitDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
            if (aspect instanceof ExplicitDimensionAspect)
                result.add(aspect);
        }
        return result;
    }
    
    public List<Aspect> getTypedDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
            if (aspect instanceof TypedDimensionAspect)
                result.add(aspect);
        }
        return result;
    }
    
    public List<Aspect> getDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
           if (aspect instanceof ExplicitDimensionAspect || aspect instanceof TypedDimensionAspect) 
               result.add(aspect);
       }
       return result;
    }
}
