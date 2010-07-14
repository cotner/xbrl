package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public class EntityDomain extends DomainImpl implements Domain {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1574457177945271318L;
    
    protected final static Logger logger = Logger.getLogger(EntityDomain.class);
    
    public EntityDomain(Store store) throws XBRLException {
        super(store);
    }
    
    /**
     * @see Domain#getAspectId()
     */
    public URI getAspectId() { return EntityAspect.ID; }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(AspectValue candidate)
            throws XBRLException {
        if (candidate instanceof EntityAspectValue) return true;
        return false;
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
     *         the aspect value IDs.
     *         Missing values are ranked last among aspect values of the same type.
     */
    public int compare(AspectValue first, AspectValue second) {
        if (! (first instanceof EntityAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return 1;
        }
        if (! (second instanceof EntityAspectValue)) {
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

    public List<AspectValue> getAllAspectValues() throws XBRLException {
        // TODO Auto-generated method stub
        return null;
    }

}
