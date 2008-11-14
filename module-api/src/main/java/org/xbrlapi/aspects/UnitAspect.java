package org.xbrlapi.aspects;

import org.xbrlapi.Unit;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class UnitAspect extends BaseAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public UnitAspect(AspectModel aspectModel) throws XBRLException {
        super();
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.CONCEPT;
    }

    private class Transformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.UnitImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            Unit f = ((Unit) value.getFragment());
            String v = "";
            for (String measure: f.getResolvedNumeratorMeasures()) {
                String[] parts = measure.split("\\Q|:|:|\\E");
                v += parts[0] + parts[1];
            }
            return null;
        }
    }    
}
