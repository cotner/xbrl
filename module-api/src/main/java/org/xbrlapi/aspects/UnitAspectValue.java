package org.xbrlapi.aspects;

import org.xbrlapi.Unit;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class UnitAspectValue extends BaseAspectValue {

    public UnitAspectValue(Aspect aspect, Unit unit) throws XBRLException {
        super(aspect, unit);
    }

}
