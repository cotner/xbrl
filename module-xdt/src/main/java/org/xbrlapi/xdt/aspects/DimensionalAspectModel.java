package org.xbrlapi.xdt.aspects;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Scenario;
import org.xbrlapi.Segment;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.BaseAspectModel;
import org.xbrlapi.aspects.ConceptAspect;
import org.xbrlapi.aspects.EntityIdentifierAspect;
import org.xbrlapi.aspects.LocationAspect;
import org.xbrlapi.aspects.PeriodAspect;
import org.xbrlapi.aspects.UnitAspect;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.ItemImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.TypedDimension;
import org.xbrlapi.xdt.XDTConstants;
import org.xbrlapi.xdt.values.DimensionValueAccessor;
import org.xbrlapi.xdt.values.DimensionValueAccessorImpl;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DimensionalAspectModel extends BaseAspectModel implements AspectModel {

    transient DimensionValueAccessor accessor;
    
    /**
     * XDT Aspects are only added as they are required.
     * @throws XBRLException
     */
    public DimensionalAspectModel() throws XBRLException {
        super();
        initialize();
        this.setAspect(new LocationAspect(this));
        this.setAspect(new ConceptAspect(this));
        this.setAspect(new EntityIdentifierAspect(this));
        this.setAspect(new SegmentRemainderAspect(this));
        this.setAspect(new PeriodAspect(this));
        this.setAspect(new ScenarioRemainderAspect(this));
        this.setAspect(new UnitAspect(this));
        //XDT Aspects are only added as they are required.
        
    }

    protected void initialize() {
        this.accessor = new DimensionValueAccessorImpl();
    }
    
    /**
     * Adds in the ability to detect new dimensional aspects
     * inherent in the fact itself.
     * @see AspectModel#addFact(Fact)
     */
    @Override
    public void addFact(Fact fact) throws XBRLException {
        
        // Create any new XDT aspects
        if (fact.isa(ItemImpl.class)) {
            Item item = (Item) fact;
            Context context =  item.getContext();
            Entity entity = context.getEntity();
            Segment segment = entity.getSegment();
            if (segment != null) addNewAspects(segment); 
            Scenario scenario = context.getScenario();
            if (scenario != null) addNewAspects(scenario);
        }
        super.addFact(fact);

    }
    
    private void addNewAspects(org.xbrlapi.OpenContextComponent occ) throws XBRLException {

        Store store = occ.getStore();
        
        List<Element> children = occ.getChildElements();
        
        for (Element child: children) {
            if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace)) {
                if (child.hasAttribute("dimension")) {
                    String qname = child.getAttribute("dimension");
                    URI ns = occ.getNamespaceFromQName(qname,child);
                    String localname = occ.getLocalnameFromQName(qname);
                    if (! this.hasAspect(ns + localname)) {
                        Dimension dimension = (Dimension) store.getConcept(ns,localname); // Optimised
                        if (dimension.isExplicitDimension())
                            this.setAspect(new ExplicitDimensionAspect(this ,(ExplicitDimension) dimension));
                        else 
                            this.setAspect(new TypedDimensionAspect(this, (TypedDimension) dimension));
                    }
                }
            }
        }
    }    

    public String getType() {
        return "dimensional";
    }

    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        initialize();
   }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
   }
    
    public List<DimensionAspect> getDimensionAspects() {
        List<DimensionAspect> result = new Vector<DimensionAspect>();
        for (Aspect aspect: this.getAspects()) {
           String name = aspect.getClass().getName();
           if (name.equals("org.xbrlapi.xdt.aspects.ExplicitDimensionAspect") || name.equals("org.xbrlapi.xdt.aspects.TypedDimensionAspect")) 
               result.add((DimensionAspect) aspect);
       }
       return result;
    }

    public List<ExplicitDimensionAspect> getExplicitDimensionAspects() {
        List<ExplicitDimensionAspect> result = new Vector<ExplicitDimensionAspect>();
        for (Aspect aspect: this.getAspects()) {
            String name = aspect.getClass().getName();
            if (name.equals("org.xbrlapi.xdt.aspects.ExplicitDimensionAspect")) 
                result.add((ExplicitDimensionAspect) aspect);
        }
        return result;
    }
    
    public List<TypedDimensionAspect> getTypedDimensionAspects() {
        List<TypedDimensionAspect> result = new Vector<TypedDimensionAspect>();
        for (Aspect aspect: this.getAspects()) {
            String name = aspect.getClass().getName();
            if (name.equals("org.xbrlapi.xdt.aspects.TypedDimensionAspect")) 
                result.add((TypedDimensionAspect) aspect);
        }
        return result;
    }
        
    
}
