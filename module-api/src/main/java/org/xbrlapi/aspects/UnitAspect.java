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
        return Aspect.UNIT;
    }

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        
        public Transformer() {
            super();
        }
        

        /**
         * @see AspectValueTransformer#validate(AspectValue)
         */
        public void validate(AspectValue value) throws XBRLException {
            super.validate(value);
            if (value.getFragment() == null) return;
            if (! value.getFragment().isa("org.xbrlapi.impl.UnitImpl")) {
                throw new XBRLException("The aspect value must have a unit fragment.");
            }
        }

        /**
         * TODO Handle transformations and labelling of units with denominators.
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
            Unit f = ((Unit) value.getFragment());
            String result = "";
            for (String measure: f.getResolvedNumeratorMeasures()) {
                String[] parts = measure.split("\\Q|:|:|\\E");
                result += parts[0] + parts[1];
            }
            setMapId(value,result);
            return result;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            String id = getIdentifier(value);
            if (hasMapLabel(id)) {
                return getMapLabel(id);
            }
            if (value.getFragment() == null) {
                setMapLabel("","");
                return "";
            }
            Unit f = ((Unit) value.getFragment());
            String result = "";
            for (String measure: f.getResolvedNumeratorMeasures()) {
                String[] parts = measure.split("\\Q|:|:|\\E");
                result += parts[1];
            }
            setMapLabel(id,result);
            return result;
        } 
        
    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public AspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new UnitAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return new MissingAspectValue(this);
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
