package org.xbrlapi.aspects;

import org.xbrlapi.Segment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentAspectValue extends BaseAspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = 2781998986018034839L;

    public SegmentAspectValue(Aspect aspect, Segment segment) throws XBRLException {
        super(aspect, segment);
    }

}
