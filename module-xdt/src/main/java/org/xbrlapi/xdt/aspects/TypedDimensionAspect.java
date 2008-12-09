package org.xbrlapi.xdt.aspects;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.LabelResource;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspect;
import org.xbrlapi.aspects.BaseAspectValueTransformer;
import org.xbrlapi.aspects.MissingAspectValue;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.TypedDimension;
import org.xbrlapi.xdt.values.DimensionValue;
import org.xbrlapi.xdt.values.DimensionValueAccessor;
import org.xbrlapi.xdt.values.DimensionValueAccessorImpl;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TypedDimensionAspect extends BaseAspect implements Aspect {

    private TypedDimension dimension = null;
    private String type = null;
    private DimensionValueAccessor accessor = new DimensionValueAccessorImpl();
    /**
     * 
     * @param dimension The dimension defining this aspect.
     * @throws XBRLException if the dimension is null.
     */
    private void setDimension(TypedDimension dimension) throws XBRLException {
        if (dimension == null) throw new XBRLException("The dimension must not be null.");
        this.dimension = dimension;
        type = dimension.getTargetNamespaceURI() + dimension.getName();
    }
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @param dimension The dimension defining this aspect.
     * @throws XBRLException.
     */
    public TypedDimensionAspect(AspectModel aspectModel, TypedDimension dimension) throws XBRLException {
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
            Element content = aspect.accessor.getTypedDimensionContentFromOpenContextComponent((OpenContextComponent) value.getFragment(),aspect.dimension);
            NodeList nodes = content.getChildNodes();
            Element child = content;
            GETELEMENT: for (int i=0; i<nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    child = (Element) nodes.item(i);
                    break GETELEMENT;
                }
            }
            
            String id = child.getLocalName();
            String text = child.getTextContent();
            if (! text.trim().equals("")) {
                id += "=" + text;
            }
            NamedNodeMap attrs = child.getAttributes();
            if (attrs.getLength() > 0) {
                id += "(";
                for (int i=0; i<attrs.getLength(); i++) {
                    Attr attr = (Attr) attrs.item(i);
                    if (i!=0) id += ","; 
                    id += attr.getName() + "=" + attr.getValue();
                }
                id += ")";
            }
            
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
                Dimension dimension = aspect.dimension;
                Concept concept = dimension;
                FragmentList<LabelResource> labels = concept.getLabelsWithLanguageAndRole(getLanguageCode(),getLabelRole());
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
        DimensionValue value = accessor.getValue((Item) fact, dimension);
        if (value == null) return null; 
        return value.getOpenContextComponent();
    }
    
    /**
     * @see Aspect#getFragmentKey(Fact)
     */
    public String getFragmentKey(Fact fact) throws XBRLException {
        return fact.getFragmentIndex();
    }    
    
    
    /**
     * The label role is used in constructing the label for the
     * concept aspect values.
     */
    private String role = Constants.StandardLabelRole;
    
    /**
     * @return the label resource role.
     */
    public String getLabelRole() {
        return role;
    }
    
    /**
     * @param role The label resource role to use in
     * selecting labels for the concept.
     */
    public void setLabelRole(String role) {
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


