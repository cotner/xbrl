package org.xbrlapi.aspects;

import org.xbrlapi.Period;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PeriodAspectValue extends BaseAspectValue {

    public PeriodAspectValue(Aspect aspect, Period period)
            throws XBRLException {
        super(aspect, period);
    }

}
