package org.xbrlapi.xdt.aspects;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.LabelResource;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspectValueTransformer;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.values.DimensionValue;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionAspect extends DimensionAspect implements Aspect {

    private final static Logger logger = Logger.getLogger(ExplicitDimensionAspect.class); 
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @param dimension The dimension defining this aspect.
     * @throws XBRLException.
     */
    public ExplicitDimensionAspect(AspectModel aspectModel, ExplicitDimension dimension) throws XBRLException {
        super(aspectModel, dimension);
        initialize();
    }
    
    protected void initialize() {
        setTransformer(new Transformer());        
    }

    public class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {


        /**
         * For explicit XDT dimensions, the identifier is based
         * on the QName of the dimension member representing the
         * dimension value.
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(ExplicitDimensionAspectValue value) throws XBRLException {
            
            if (hasMapId(value)) {
                return getMapId(value);
            }

            String id = "";
            Concept concept = value.<Concept>getFragment();
            if (concept != null) id = concept.getTargetNamespace() + ":" + concept.getName();

            setMapId(value,id);
            return id;
            
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(ExplicitDimensionAspectValue value) throws XBRLException {
            
            Concept concept = value.<Concept>getFragment();
            if (concept == null) return null;

            String id = getIdentifier(value);
            if (hasMapLabel(id)) {
                return getMapLabel(id);
            }
            
            String label = id;
            if (concept != null) {
                List<LabelResource> labels = concept.getLabelsWithLanguageAndResourceRole(getLanguageCode(),getLabelRole());
                if (! labels.isEmpty()) {
                    label = labels.get(0).getStringValue();
                }
            }
            
            setMapLabel(id,label);
            return label;
        }

    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public AspectValue getValue(Fact fact) throws XBRLException {
        Concept concept = this.<Concept>getFragment(fact);
        return new ExplicitDimensionAspectValue(this,concept);
    }

    /**
     * When you are confused by the return type, recall that explicit dimension
     * values are all requried to be XBRL concepts - wierd but true!
     * @see Aspect#getFragmentFromStore(Fact)
     */
    @SuppressWarnings("unchecked")
    public Concept getFragmentFromStore(Fact fact) throws XBRLException {
        if (fact.isTuple()) return null;
        ExplicitDimension dimension = this.<ExplicitDimension>getDimension();
        DimensionValue value = getAccessor().getValue((Item) fact, dimension);
        if (value == null) return null;
        return (Concept) value.getValue();
    }
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        if (fact.isTuple()) return "";
        DimensionValue dimensionValue = getAccessor().getValue((Item) fact, getDimension());
        if (dimensionValue == null) return "";
        Concept concept = (Concept) dimensionValue.getValue();
        return concept.getNamespace() + "#" + concept.getLocalname();
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

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
       return true;
    }

}


