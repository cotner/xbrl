package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Location aspect details</h2>
 * 
 * <p>
 * All facts have a value for the location aspect.  The location aspect reflects 
 * the containers of the fact.  This includes the containing XBRL instance and
 * any tuple ancestors of the fact.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LocationAspect extends AspectImpl<LocationAspectValue> implements Aspect {
    
    /**
     * 
     */
    private static final long serialVersionUID = -7841916801129980666L;

    private static final Logger logger = Logger.getLogger(LocationAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static URI ID = URI.create("http://xbrlapi.org/aspect/location/1.0");

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
    public LocationAspect(Domain<LocationAspectValue> domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    @SuppressWarnings("unchecked")
    public LocationAspectValue getValue(Fact fact) throws XBRLException {
        return new LocationAspectValue(fact.getIndex());
    }

    /**
     * @see Aspect#getDomain()
     */
    @SuppressWarnings("unchecked")
    public Domain<LocationAspectValue> getDomain() {
        return domain;
    }

    /**
     * Implies no missing values are defined.
     * @return null.
     */
    @SuppressWarnings("unchecked")
    public LocationAspectValue getMissingValue() {
        return null;
    }


}
