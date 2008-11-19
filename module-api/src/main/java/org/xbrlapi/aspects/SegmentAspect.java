package org.xbrlapi.aspects;

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
            Segment f = ((Segment) value.getFragment());
            String id = f.getStore().serializeToString(f.getDataRootElement());
            setMapId(value,id);
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            return getIdentifier(value);
        } 
    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public SegmentAspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new SegmentAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return null;
        }
    }

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        Entity entity = ((Context) super.getFragmentFromStore(fact)).getEntity();
        Segment segment = entity.getSegment();
        if (segment == null) throw new XBRLException("The segment fragment is not available.");
        return segment;
    }
}
