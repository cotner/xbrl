package org.xbrlapi.aspects;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.SegmentImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentAspectValue extends BaseAspectValue {

    public SegmentAspectValue(Aspect aspect, Fragment fragment)
            throws XBRLException {
        super(aspect, fragment);
        if (! fragment.isa(SegmentImpl.class)) {
            throw new XBRLException("Fragment does not match the type of the aspect value.");
        }
    }

}
