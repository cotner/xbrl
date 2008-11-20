package org.xbrlapi.aspects;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class MissingAspectValue extends BaseAspectValue {

    public MissingAspectValue(Aspect aspect)
            throws XBRLException {
        super(aspect, null);
    }

}
