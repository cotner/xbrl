package org.xbrlapi.xdt.aspects;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Item;
import org.xbrlapi.LabelResource;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspect;
import org.xbrlapi.aspects.BaseAspectValueTransformer;
import org.xbrlapi.aspects.MissingAspectValue;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.values.DimensionValue;
import org.xbrlapi.xdt.values.DimensionValueAccessor;
import org.xbrlapi.xdt.values.DimensionValueAccessorImpl;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionAspect extends BaseAspect implements Aspect {

    private ExplicitDimension dimension = null;
    private String type = null;
    private DimensionValueAccessor accessor = new DimensionValueAccessorImpl();
    /**
     * 
     * @param dimension The dimension defining this aspect.
     * @throws XBRLException if the dimension is null.
     */
    private void setDimension(ExplicitDimension dimension) throws XBRLException {
        if (dimension == null) throw new XBRLException("The dimension must not be null.");
        this.dimension = dimension;
        type = dimension.getTargetNamespace() + dimension.getName();
    }
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @param dimension The dimension defining this aspect.
     * @throws XBRLException.
     */
    public ExplicitDimensionAspect(AspectModel aspectModel, ExplicitDimension dimension) throws XBRLException {
        setAspectModel(aspectModel);
        setDimension(dimension);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return type;
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
        DimensionValue value = accessor.getValue((Item) fact, dimension);
        if (value == null) return null; 
        return (Concept) value.getValue();
    }
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        DimensionValue dimensionValue = accessor.getValue((Item) fact, this.dimension);
        if (dimensionValue == null) return "";
        Concept concept = (Concept) dimensionValue.getValue();
        return concept.getNamespace() + concept.getLocalname();
    }    
    
    
    /**
     * The label role is used in constructing the label for the
     * concept aspect values.
     */
    private URI role = Constants.StandardLabelRole;
    
    /**
     * @return the label resource role.
     */
    public URI getLabelRole() {
        return role;
    }
    
    /**
     * @param role The label resource role to use in
     * selecting labels for the concept.
     */
    public void setLabelRole(URI role) {
        this.role = role;
    }    

    /**
     * The language code is used in constructing the label for the
     * concept aspect values.
     */
    private String language = "en";    
    
    /**
     * @return the language code.
     */
    public String getLanguageCode() {
        return language;
    }
    
    /**
     * @param language The ISO language code
     */
    public void setLanguageCode(String language) throws XBRLException {
        if (language == null) throw new XBRLException("The language must not be null.");
        this.language = language;
    }
    
    
}


