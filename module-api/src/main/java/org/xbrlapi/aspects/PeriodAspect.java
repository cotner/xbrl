package org.xbrlapi.aspects;

import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Period;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PeriodAspect extends BaseContextAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public PeriodAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.PERIOD;
    }

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.PeriodImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            if (hasTransform(value)) {
                return getTransform(value);
            }
            Period f = ((Period) value.getFragment());
            String result = "";
            if (f.isFiniteDurationPeriod()) {
                result += dateTime(f.getStart()) + "-" + dateTime(f.getEnd());
            } else if (f.isInstantPeriod()) {
                result += dateTime(f.getInstant());
            } else {
                result += "forever";
            }
            setTransform(value,result);
            return result;

        }

        private String dateTime(String value) {
            int tIndex = value.indexOf('T');
            if (tIndex == -1) {
                return value;
            }
            String[] dateTime = value.split("T");
            String dateValue = dateTime[0];
            String timeValue = dateTime[1];
            return dateValue + ":" + timeValue;
            
        }
    }    

    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public PeriodAspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new PeriodAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return null;
        }
    }

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        return ((Context) super.getFragment(fact)).getPeriod();
    }    
    
}
