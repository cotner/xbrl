package org.xbrlapi.aspects;

import org.xbrlapi.Unit;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class UnitAspectValue extends BaseAspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = 2102433667682868985L;

    public UnitAspectValue(Aspect aspect, Unit unit) throws XBRLException {
        super(aspect, unit);
    }

}
