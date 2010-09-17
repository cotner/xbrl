package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Segment;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectImpl;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Segment remainder aspect details</h2>
 * 
 * <p>
 * The value is based on the non-XDT content of 
 * the context Segment.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentRemainderAspect extends AspectImpl implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = 1204517671744178026L;

    private static final Logger logger = Logger.getLogger(SegmentRemainderAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static URI ID = URI.create("http://xbrlapi.org/aspect/segment-remainder/1.0");
    
    /**
     * @see Aspect#getId()
     */
    public URI getId() {
        return ID;
    }
    
    /**
     * @param domain The domain for this aspect.
     * @throws XBRLException
     */
    public SegmentRemainderAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public SegmentRemainderAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return getMissingValue();
        if (fact.isNil()) return getMissingValue();
        return getValue(((Item) fact).getContext());
    }

    /**
     * @see Aspect#getValue(Context)
     */
    public SegmentRemainderAspectValue getValue(Context context) throws XBRLException {
        return getValue(context.getEntity().getSegment());
    }

    /**
     * @param segment The context segment, which may be null.
     * @return the segment remainder aspect value.
     * @throws XBRLException
     */
    public SegmentRemainderAspectValue getValue(Segment segment) throws XBRLException {
        if (segment == null) return getMissingValue();
        return new SegmentRemainderAspectValue(segment);
    }
    
    /**
     * @see Aspect#getMissingValue()
     */
    public SegmentRemainderAspectValue getMissingValue() {
        return new SegmentRemainderAspectValue();
    }

}
