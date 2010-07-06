package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Segment;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Segment aspect details</h2>
 * 
 * <p>
 * Scenario aspects capture information in the context segment.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentAspect extends AspectImpl implements Aspect {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6762933296532647581L;

    private static final Logger logger = Logger.getLogger(SegmentAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static URI ID = URI.create("http://xbrlapi.org/aspect/segment/1.0");
    
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
    public SegmentAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public SegmentAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return getMissingValue();
        if (fact.isNil()) return getMissingValue();
        Item item = (Item) fact;
        Segment segment = item.getContext().getEntity().getSegment();
        return getValue(segment);
    }
    
    /**
     * @param segment The context segment
     * @return the saspect value.
     * @throws XBRLException
     */
    public SegmentAspectValue getValue(Segment segment) throws XBRLException {
        if (segment == null) return getMissingValue();
        return new SegmentAspectValue(segment);
    }        

    /**
     * @see Aspect#getMissingValue()
     */
    public SegmentAspectValue getMissingValue() {
        return new SegmentAspectValue();
    }


}
