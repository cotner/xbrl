package org.xbrlapi.xdt.aspects;

import org.xbrlapi.Concept;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.BaseAspectValue;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionAspectValue extends BaseAspectValue {


    public ExplicitDimensionAspectValue(Aspect aspect, Concept concept) throws XBRLException {
        super(aspect,concept);
    }
    


}
