package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Period;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Period aspect details</h2>
 * 
 * <p>
 * Period aspects capture the period information in XBRL contexts.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PeriodAspect extends AspectImpl implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = -8787755650749959353L;

    private static final Logger logger = Logger.getLogger(PeriodAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static final URI ID = URI.create("http://xbrlapi.org/aspect/period/1.0");
    
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
    public PeriodAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public PeriodAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return getMissingValue();
        if (fact.isNil()) return getMissingValue();
        Item item = (Item) fact;
        Period period = item.getContext().getPeriod();
        return new PeriodAspectValue(period);
    }
    
    /**
     * @see Aspect#getMissingValue()
     */
    public PeriodAspectValue getMissingValue() {
        return new PeriodAspectValue();
    }

}
