package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.impl.InstanceImpl;
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
public class LocationAspect extends AspectImpl implements Aspect {
    
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
    public LocationAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public LocationAspectValue getValue(Fact fact) throws XBRLException {
        Fragment parent = fact.getParent();
        if (parent.isa(InstanceImpl.class)) return new LocationAspectValue(fact.getIndex());
        return new LocationAspectValue(parent.getIndex(),fact.getIndex());
    }

    /**
     * @see Aspect#getMissingValue()
     */
    public LocationAspectValue getMissingValue() {
        return new LocationAspectValue();
    }

}
