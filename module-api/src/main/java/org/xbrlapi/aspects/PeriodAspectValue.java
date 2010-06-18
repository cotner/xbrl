package org.xbrlapi.aspects;

import org.xbrlapi.Period;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PeriodAspectValue extends BaseAspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = -6461739642078278523L;

    public PeriodAspectValue(Aspect aspect, Period period)
            throws XBRLException {
        super(aspect, period);
    }

}
