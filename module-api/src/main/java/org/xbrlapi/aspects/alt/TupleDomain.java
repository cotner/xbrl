package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public class TupleDomain extends DomainImpl implements Domain {

    /**
     * 
     */
    private static final long serialVersionUID = -7809445921191054988L;
    
    protected final static Logger logger = Logger.getLogger(TupleDomain.class);
    
    public TupleDomain(Store store) throws XBRLException {
        super(store);
    }

    /**
     * @see Domain#getAspectId()
     */
    public URI getAspectId() { return TupleAspect.ID; }
    
    /**
     * @see Domain#getAllAspectValues()
     */
    @Override
    public List<AspectValue> getAllAspectValues() throws XBRLException {
        
        List<AspectValue> values = new Vector<AspectValue>();
        values.add(new TupleAspectValue(true));
        values.add(new TupleAspectValue(false));
        return values;
    }

    /**
     * @see Domain#getSize()
     */
    @Override
    public long getSize() throws XBRLException {
        return 2L;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(AspectValue candidate)
            throws XBRLException {
        if (! (candidate instanceof TupleAspectValue)) return false;
        return true;
    }

    /**
     * @see Domain#isFinite()
     */
    @Override
    public boolean isFinite() {
        return true;
    }

    /**
     * @param first
     *            The first aspect value
     * @param second
     *            The second aspect value
     * @return -1 if the first aspect value is less than the second, 0 if they
     *         are equal and 1 if the first aspect value is greater than the
     *         second. Any aspect values that are not in this domain
     *         are placed last in the aspect value ordering.
     *         Otherwise, the comparison is based upon the natural ordering of
     *         the concept namespaces and then the concept local names.
     *         Missing values are ranked last among aspect values of the same type.
     */
    public int compare(AspectValue first, AspectValue second) {
        if (! (first instanceof TupleAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return 1;
        }
        if (! (second instanceof TupleAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return -1;
        }

        if (first.isMissing()) {
            if (second.isMissing()) return 0;
            return 1;
        }
        if (second.isMissing()) return -1;
        
        return first.getId().compareTo(second.getId());
    }

}
