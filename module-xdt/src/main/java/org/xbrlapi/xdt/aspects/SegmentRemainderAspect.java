package org.xbrlapi.xdt.aspects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Segment;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspectValueTransformer;
import org.xbrlapi.aspects.ContextAspect;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.XDTConstants;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentRemainderAspect extends ContextAspect implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = -6911165634379417856L;
    public final static String TYPE = "non-dimensional segment content";
    
    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return TYPE;
    }

    private static final Logger logger = Logger.getLogger(SegmentRemainderAspect.class);

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public SegmentRemainderAspect(AspectModel aspectModel) throws XBRLException {
        super(aspectModel);
        initialize();
    }
    
    protected void initialize() {
        this.setTransformer(new Transformer(this));
    }

        

    public class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {

        public Transformer(Aspect aspect) {
            super(aspect);
        }
        
        /**
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {

            if (hasMapId(value)) {
                return getMapId(value);
            }

            String id = "";
            Segment segment = value.<Segment>getFragment();
            if (segment != null) {
                List<Element> children = segment.getChildElements();
                List<Element> remainder = new Vector<Element>();
                CHILDREN: for (Element child: children) {
                    if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString())) {
                        if (child.hasAttribute("dimension")) {
                            continue CHILDREN;
                        }
                    }
                    remainder.add(child);
                }                
                id = getLabelFromElements(remainder);
            }
            
            setMapId(value,id);
            return id;
            
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            if (value.getFragment() == null) return null;
            return getIdentifier(value);
        } 
    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public SegmentRemainderAspectValue getValue(Fact fact) throws XBRLException {
        Segment segment = this.<Segment>getFragment(fact);
        return new SegmentRemainderAspectValue(this,segment);
    }

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    @SuppressWarnings("unchecked")
    public Segment getFragmentFromStore(Fact fact) throws XBRLException {
        Context context = getContextFromStore(fact);
        if (context == null) return null;
        Entity entity = context.getEntity();
        Segment segment = entity.getSegment();
        return segment;
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
