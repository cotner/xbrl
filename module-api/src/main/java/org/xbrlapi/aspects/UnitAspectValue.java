package org.xbrlapi.aspects;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.UnitImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class UnitAspectValue extends BaseAspectValue {

    public UnitAspectValue(Aspect aspect, Fragment fragment)
            throws XBRLException {
        super(aspect, fragment);
        if (! fragment.isa(UnitImpl.class)) {
            throw new XBRLException("Fragment does not match the type of the aspect value.");
        }
    }

}
