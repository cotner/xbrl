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
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentAspect extends AspectImpl<SegmentAspectValue> implements Aspect {
    
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
    public SegmentAspect(Domain<SegmentAspectValue> domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    @SuppressWarnings("unchecked")
    public SegmentAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return getMissingValue();
        if (fact.isNil()) return getMissingValue();
        Item item = (Item) fact;
        Segment segment = item.getContext().getEntity().getSegment();
        if (segment == null) return getMissingValue();
        return new SegmentAspectValue(segment);
    }

    /**
     * @see Aspect#getDomain()
     */
    @SuppressWarnings("unchecked")
    public Domain<SegmentAspectValue> getDomain() {
        return domain;
    }

    /**
     * Implies no missing values are defined.
     * @return null.
     */
    @SuppressWarnings("unchecked")
    public SegmentAspectValue getMissingValue() {
        return new SegmentAspectValue();
    }


}
