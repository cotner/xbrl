package org.xbrlapi.xdt.aspects;

import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.BaseAspectValue;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TypedDimensionAspectValue extends BaseAspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = -6203040308810854332L;

    public TypedDimensionAspectValue(Aspect aspect, OpenContextComponent occ) throws XBRLException {
        super(aspect,occ);
    }
}
