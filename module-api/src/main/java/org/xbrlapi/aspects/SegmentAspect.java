package org.xbrlapi.aspects;

import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Segment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentAspect extends BaseAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public SegmentAspect(AspectModel aspectModel) throws XBRLException {
        super();
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.SEGMENT;
    }

    private class Transformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.SegmentImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            Segment f = ((Segment) value.getFragment());
            return f.getStore().serializeToString(f.getDataRootElement());
        }
    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public ScenarioAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            return null;
        }
        Item item = (Item) fact;
        Segment segment = item.getContext().getEntity().getSegment();
        if (segment == null) {
            return null;
        }
        return new ScenarioAspectValue(this,segment);
    }      
}
