package org.xbrlapi.aspects;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Segment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentAspect extends ContextAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public SegmentAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.SEGMENT;
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
            String id = f.getStore().serializeToString(f.getDataRootElement());
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
            NodeList children = f.getDataRootElement().getChildNodes();
            Element child = null;
            CHILDREN: for (int i=0; i<children.getLength(); i++) {
                if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    child = (Element) children.item(i);
                    break CHILDREN;
                }
            }
            String label = "";
            if (child == null) {
                f.getStore().serialize(f.getDataRootElement());
            } else {
                label = child.getLocalName();
                String text = child.getTextContent();
                if (! text.trim().equals("")) {
                    label += "=" + text;
                }
            }
            logger.info("Segment aspect value label is " + label);            
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
        return new SegmentAspectValue(this,fragment);
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
