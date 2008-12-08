package org.xbrlapi.xdt.aspects;

import org.xbrlapi.Fragment;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.BaseAspectValue;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TypedDimensionAspectValue extends BaseAspectValue {

    public TypedDimensionAspectValue(Aspect aspect, Fragment value) throws XBRLException {
        super(aspect,value);
    }
}
