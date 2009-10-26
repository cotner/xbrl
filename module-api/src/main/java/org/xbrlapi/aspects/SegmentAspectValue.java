package org.xbrlapi.aspects;

import org.xbrlapi.Segment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentAspectValue extends BaseAspectValue {

    public SegmentAspectValue(Aspect aspect, Segment segment) throws XBRLException {
        super(aspect, segment);
    }

}
