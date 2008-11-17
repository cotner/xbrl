package org.xbrlapi.aspects;

import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Item;
import org.xbrlapi.NumericItem;
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

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.UnitImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            Unit f = ((Unit) value.getFragment());
            String result = "";
            for (String measure: f.getResolvedNumeratorMeasures()) {
                String[] parts = measure.split("\\Q|:|:|\\E");
                result += parts[0] + parts[1];
            }
            setTransform(value,result);
            return result;
        }
    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public UnitAspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new UnitAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return null;
        }
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            throw new XBRLException("The fact must not be a tuple.");
        }
        Item item = (Item) fact;
        if (! item.isNumeric()) throw new XBRLException("The fact must be numeric.");
        return item.getUnit();
    }    
    
    /**
     * @see Aspect#getFragmentKey(Fact)
     */
    public String getFragmentKey(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            throw new XBRLException("The fact must not be a tuple.");
        }
        Item item = (Item) fact;
        if (! item.isNumeric()) throw new XBRLException("The fact must be numeric.");
        return item.getURL() + ((NumericItem) item).getUnitId();
    }
}
