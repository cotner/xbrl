package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.impl.InstanceImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Tuple aspect details</h2>
 * 
 * <p>
 * The tuple aspect keeps track of whether facts are tuples or children of tuples
 * or whether they are children of XBRL instances.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TupleAspect extends AspectImpl implements Aspect {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7795484614533602772L;

    private static final Logger logger = Logger.getLogger(TupleAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static final URI ID = URI.create("http://xbrlapi.org/aspect/tuple/1.0");
    
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
    public TupleAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public TupleAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return new TupleAspectValue(true);
        Fragment parent = fact.getParent();
        if (parent.isa(InstanceImpl.class)) return new TupleAspectValue(false);
        return new TupleAspectValue(true);
    }

    /**
     * @see Aspect#getMissingValue()
     */
    public TupleAspectValue getMissingValue() {
        return new TupleAspectValue();
    }

}
