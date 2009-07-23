package org.xbrlapi.xdt.aspects;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Item;
import org.xbrlapi.LabelResource;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspectValueTransformer;
import org.xbrlapi.aspects.MissingAspectValue;
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

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        /**
         * @see AspectValueTransformer#validate(AspectValue)
         */
        public void validate(AspectValue value) throws XBRLException {
            super.validate(value);
            if (value.getFragment() == null) return;
            if (! value.getFragment().isa("org.xbrlapi.impl.ConceptImpl")) {
                throw new XBRLException("The aspect value must have a concept fragment.");
            }
        }

        /**
         * For explicit XDT dimensions, the identifier is based
         * on the QName of the dimension member representing the
         * dimension value.
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {
            
            validate(value);
            
            if (hasMapId(value)) {
                return getMapId(value);
            }
            
            // TODO rework this to handle default explicit dimensions
            if (value.getFragment() == null) {
                setMapId(value,"");
                return "";
            }
            
            Concept concept = (Concept) value.getFragment();
            String id = concept.getTargetNamespace() + concept.getName();
            setMapId(value,id);
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            String id = getIdentifier(value);
            if (hasMapLabel(id)) {
                return getMapLabel(id);
            }
            Concept f = ((Concept) value.getFragment());
            if (f == null) {
                setMapLabel(id,"");
                return "";
            }            
            List<LabelResource> labels = f.getLabelsWithLanguageAndResourceRole(getLanguageCode(),getLabelRole());
            if (labels.isEmpty()) return id;
            String label = labels.get(0).getStringValue();
            logger.info("Explicit dimension aspect value label is " + label);
            setMapLabel(id,label);
            return label;
        }

    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public AspectValue getValue(Fact fact) throws XBRLException {
        Fragment fragment = getFragment(fact);
        if (fragment == null) {
            return new MissingAspectValue(this);
        }
        return new ExplicitDimensionAspectValue(this,fragment);
    }

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        DimensionValue value = getAccessor().getValue((Item) fact, getDimension());
        if (value == null) return null; 
        return (Concept) value.getValue();
    }
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        DimensionValue dimensionValue = getAccessor().getValue((Item) fact, getDimension());
        if (dimensionValue == null) return "";
        Concept concept = (Concept) dimensionValue.getValue();
        return concept.getNamespace() + concept.getLocalname();
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


