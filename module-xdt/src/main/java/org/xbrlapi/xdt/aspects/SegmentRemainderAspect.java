package org.xbrlapi.xdt.aspects;

import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Segment;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspectValueTransformer;
import org.xbrlapi.aspects.ContextAspect;
import org.xbrlapi.aspects.MissingAspectValue;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.XDTConstants;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentRemainderAspect extends ContextAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public SegmentRemainderAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return "segmentremainder";
    }

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        /**
         * @see AspectValueTransformer#validate(AspectValue)
         */
        public void validate(AspectValue value) throws XBRLException {
            super.validate(value);
            if (value.getFragment() == null) return;
            if (! value.getFragment().isa("org.xbrlapi.impl.SegmentImpl")) {
                throw new XBRLException("The aspect value must have a segment fragment.");
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
            if (value.getFragment() == null) {
                setMapId(value,"");
                return "";
            }
            Segment f = ((Segment) value.getFragment());
            String id = "";
            List<Element> children = f.getChildElements();
            CHILDREN: for (Element child: children) {
                if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace)) {
                    if (child.hasAttribute("dimension")) {
                        continue CHILDREN;
                    }
                }
                id += f.getStore().serializeToString(child);
            }
            setMapId(value,id);
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            validate(value);
            String id = getIdentifier(value);
            if (hasMapLabel(id)) {
                return getMapLabel(id);
            }
            if (value.getFragment() == null) {
                setMapLabel(id,"");
                return "";
            }
            Segment f = ((Segment) value.getFragment());
            String label = "";
            List<Element> children = f.getChildElements();
            if (children.size() == 0) {
                label ="";
            } else {
                CHILDREN: for (Element child: children) {
                    if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace)) {
                        if (child.hasAttribute("dimension")) {
                            continue CHILDREN;
                        }
                    }
                    label += child.getLocalName();
                    String text = child.getTextContent();
                    if (! text.trim().equals("")) {
                        label += "=" + text;
                    }
                    NamedNodeMap attrs = child.getAttributes();
                    if (attrs.getLength() > 0) {
                        label += "(";
                        for (int i=0; i<attrs.getLength(); i++) {
                            Attr attr = (Attr) attrs.item(i);
                            if (i!=0) label += ","; 
                            label += attr.getName() + "=" + attr.getValue();
                        }
                        label += ")";
                    }
                }
            }
            
            logger.info("Segment remainder aspect value label is " + label);            
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
        return new SegmentRemainderAspectValue(this,fragment);
    }

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        Entity entity = ((Context) super.getFragmentFromStore(fact)).getEntity();
        Segment segment = entity.getSegment();
        if (segment == null) return null;
        return segment;
    }
}
