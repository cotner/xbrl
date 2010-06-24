package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
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
        Item item = (Item) fact;
        Segment Segment = item.getContext().getEntity().getSegment();
        if (Segment == null) return getMissingValue();
        return new SegmentRemainderAspectValue(Segment);
    }

    /**
     * Implies no missing values are defined.
     * @return null.
     */
    public SegmentRemainderAspectValue getMissingValue() {
        return new SegmentRemainderAspectValue();
    }

}
