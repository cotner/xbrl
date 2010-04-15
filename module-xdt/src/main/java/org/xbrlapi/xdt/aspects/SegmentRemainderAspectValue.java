package org.xbrlapi.xdt.aspects;

import org.xbrlapi.Segment;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.BaseAspectValue;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentRemainderAspectValue extends BaseAspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = -6737544738244612885L;

    public SegmentRemainderAspectValue(Aspect aspect, Segment segment) throws XBRLException {
        super(aspect, segment);
    }

}
