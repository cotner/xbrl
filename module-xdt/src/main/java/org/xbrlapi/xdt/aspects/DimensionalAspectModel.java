package org.xbrlapi.xdt.aspects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Relationship;
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
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
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

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = 462180786946469106L;

    private static final Logger logger = Logger.getLogger(DimensionalAspectModel.class);
    
    transient DimensionValueAccessor accessor;
    
    /**
     * The XBRL data store.
     */
    private Store store;
    
    /**
     * XDT Aspects are only added as they are required.
     * @throws XBRLException if the store is null.
     */
    public DimensionalAspectModel(Store store) throws XBRLException {
        super();
        initialize();
        if (store == null) throw new XBRLException("The store is null.");
        this.store = store;
        this.setAspect(new LocationAspect(this));
        this.setAspect(new ConceptAspect(this));
        this.setAspect(new EntityIdentifierAspect(this));
        this.setAspect(new SegmentRemainderAspect(this));
        this.setAspect(new PeriodAspect(this));
        this.setAspect(new ScenarioRemainderAspect(this));
        this.setAspect(new UnitAspect(this));
        
        // Set up default explicit dimension aspects.
        Networks networks = store.getNetworks(XDTConstants.DefaultDimensionArcrole);
        for (Network network: networks) {
            List<Relationship> relationships = network.getAllRelationships();
            for (Relationship relationship: relationships) {
                ExplicitDimension dimension = (ExplicitDimension) relationship.getSource();
                if (! hasAspect(dimension.getTargetNamespace() + "#" + dimension.getName())) {
                    this.setAspect(new ExplicitDimensionAspect(this,dimension));
                }
            }
        }
        
        //XDT Aspects without defaults are only added as they are required.
        
    }

    protected void initialize() {
        this.accessor = new DimensionValueAccessorImpl();
    }
    
    /**
     * Adds in the ability to detect new dimensional aspects
     * inherent in the fact itself.
     * @see BaseAspectModel#addFact(Fact)
     */
    public void addFact(Fact fact) throws XBRLException {
        
        // Create any new XDT aspects
        if (! fact.isTuple()) {
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
    
    /**
     * @see AspectModel#addFacts(Collection<Fact>)
     */
    public <F extends Fact> void addFacts(Collection<F> facts) throws XBRLException {
        
        for (Fact fact: facts) this.addFact(fact);
        
        Set<Fact> allFacts = this.getAllFacts();
        
        for (ExplicitDimensionAspect aspect: this.getExplicitDimensionAspects()) {
            Set<Fact> missingFacts = new HashSet<Fact>();
            missingFacts.addAll(allFacts);
            missingFacts.removeAll(aspect.getAllFacts());
            aspect.addFacts(missingFacts);
        }
        for (TypedDimensionAspect aspect: this.getTypedDimensionAspects()) {
            Set<Fact> missingFacts = new HashSet<Fact>();
            missingFacts.addAll(allFacts);
            missingFacts.removeAll(aspect.getAllFacts());
            aspect.addFacts(missingFacts);
        }
    }    

    /**
     * This method needs to be improved to eliminate the potential for dimension aspect type values
     * to differ between how they are constructed in this method and how they are constructed
     * by dimensional aspects themselves.
     * @param occ The segment or scenario fragment to examine for new dimensional aspects.
     * @throws XBRLException
     */
    private void addNewAspects(org.xbrlapi.OpenContextComponent occ) throws XBRLException {

        Store store = occ.getStore();

        List<Element> children = occ.getChildElements();

        for (Element child: children) {
            if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString())) {
                if (child.hasAttribute("dimension")) {
                    String qname = child.getAttribute("dimension");
                    URI dimensionNamespace = occ.getNamespaceFromQName(qname,child);
                    String dimensionLocalname = occ.getLocalnameFromQName(qname);
                    if (! this.hasAspect(dimensionNamespace + "#" + dimensionLocalname)) {
                        Dimension dimension = store.<Dimension>getGlobalDeclaration(dimensionNamespace,dimensionLocalname);
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
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
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
           if (aspect.getClass().equals(ExplicitDimensionAspect.class) || aspect.getClass().equals(TypedDimensionAspect.class)) 
               result.add((DimensionAspect) aspect);
       }
       return result;
    }

    public List<ExplicitDimensionAspect> getExplicitDimensionAspects() {
        List<ExplicitDimensionAspect> result = new Vector<ExplicitDimensionAspect>();
        for (Aspect aspect: this.getAspects()) {
            if (aspect.getClass().equals(ExplicitDimensionAspect.class))
                result.add((ExplicitDimensionAspect) aspect);
        }
        return result;
    }
    
    public List<TypedDimensionAspect> getTypedDimensionAspects() {
        List<TypedDimensionAspect> result = new Vector<TypedDimensionAspect>();
        for (Aspect aspect: this.getAspects()) {
            if (aspect.getClass().equals(TypedDimensionAspect.class))
                result.add((TypedDimensionAspect) aspect);
        }
        return result;
    }
        
 
    /**
     * @see BaseAspectModel#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((store == null) ? 0 : store.hashCode());
        return result;
    }
    
    /**
     * @see BaseAspectModel#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        boolean superResult = super.equals(obj);
        if (superResult == false) return false;
        
        if (getClass() != obj.getClass())
            return false;
        DimensionalAspectModel other = (DimensionalAspectModel) obj;
        
        if (store == null) {
            if (other.store != null) {
                return false;
            }
        } else if (!store.equals(other.store)) {
            return false;
        }
        return true;
    }    
}
