package org.xbrlapi.aspects;

import org.xbrlapi.Period;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PeriodAspect extends BaseAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public PeriodAspect(AspectModel aspectModel) throws XBRLException {
        super();
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.PERIOD;
    }

    private class Transformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.PeriodImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            Period f = ((Period) value.getFragment());
            String v = "";
            if (f.isFiniteDurationPeriod()) {
                v += dateTime(f.getStart()) + "-" + dateTime(f.getEnd());
            } else if (f.isInstantPeriod()) {
                v += dateTime(f.getInstant());
            } else {
                v += "forever";
            }
            
            return v;
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
    
}
