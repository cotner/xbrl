package org.xbrlapi.xdt.aspects;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Item;
import org.xbrlapi.LabelResource;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspectValueTransformer;
import org.xbrlapi.aspects.MissingAspectValue;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.TypedDimension;
import org.xbrlapi.xdt.values.DimensionValue;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TypedDimensionAspect extends DimensionAspect implements Aspect {

    private final static Logger logger = Logger.getLogger(TypedDimensionAspect.class);
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @param dimension The dimension defining this aspect.
     * @throws XBRLException.
     */
    public TypedDimensionAspect(AspectModel aspectModel, TypedDimension dimension) throws XBRLException {
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
            if (! value.getFragment().isa("org.xbrlapi.impl.OpenContextComponentImpl")) {
                throw new XBRLException("The aspect value must have an OCC fragment.");
            }
        }

        /**
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {
            
            validate(value);
            
            if (hasMapId(value)) {
                return getMapId(value);
            }
            
            TypedDimensionAspect aspect = (TypedDimensionAspect) value.getAspect();
            Element content = aspect.getAccessor().getTypedDimensionContentFromOpenContextComponent((OpenContextComponent) value.getFragment(),aspect.getDimension());
            NodeList nodes = content.getChildNodes();
            Element child = content;
            GETELEMENT: for (int i=0; i<nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    child = (Element) nodes.item(i);
                    break GETELEMENT;
                }
            }
            
            String id = getLabelFromElement(child);
            
            setMapId(value,id);
            return id;
        }
        
        String dimensionLabel = null;
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            String id = getIdentifier(value);
            if (hasMapLabel(id)) {
                return getMapLabel(id);
            }

            OpenContextComponent f = (OpenContextComponent) value.getFragment();
            if (f == null) {
                setMapLabel(id,"");
                return "";
            }

            if (dimensionLabel == null) {
                TypedDimensionAspect aspect = (TypedDimensionAspect) value.getAspect();
                Dimension dimension = aspect.getDimension();
                Concept concept = dimension;
                List<LabelResource> labels = concept.getLabelsWithLanguageAndResourceRole(getLanguageCode(),getLabelRole());
                if (labels.isEmpty()) dimensionLabel = dimension.getName();
                else dimensionLabel = labels.get(0).getStringValue();
            }

            String label = dimensionLabel + ": " + id;
            logger.info("Typed dimension aspect value label is " + label);
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
        return new TypedDimensionAspectValue(this,fragment);
    }

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        DimensionValue value = getAccessor().getValue((Item) fact, getDimension());
        if (value == null) return null; 
        return value.getOpenContextComponent();
    }
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        return fact.getIndex();
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


