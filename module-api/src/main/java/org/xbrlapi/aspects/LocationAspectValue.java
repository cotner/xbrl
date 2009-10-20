package org.xbrlapi.aspects;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.FactImpl;
import org.xbrlapi.impl.InstanceImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LocationAspectValue extends BaseAspectValue {

    public LocationAspectValue(Aspect aspect, Fragment fragment)
            throws XBRLException {
        super(aspect, fragment);
        if (! fragment.isa(FactImpl.class) && ! fragment.isa(InstanceImpl.class)) {
            throw new XBRLException("Fragments for location aspects must be XBRL facts or XBRL instances.  In this case is it a " + fragment.getClass().getName() + ".");
        }
    }

}
