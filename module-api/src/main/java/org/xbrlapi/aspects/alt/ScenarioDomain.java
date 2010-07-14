package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public class ScenarioDomain extends DomainImpl implements Domain {

    /**
     * 
     */
    private static final long serialVersionUID = 7377379604553778732L;
    
    protected final static Logger logger = Logger.getLogger(ScenarioDomain.class);

    public ScenarioDomain(Store store) throws XBRLException {
        super(store);
    }
    
    public URI getAspectId() { return ScenarioAspect.ID; }
    
    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(AspectValue candidate)
            throws XBRLException {
        if (! (candidate instanceof ScenarioAspectValue)) return false;
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
     *         the aspect value IDs.
     *         Missing values are ranked last among aspect values of the same type.
     */
    public int compare(AspectValue first, AspectValue second) {
        if (! (first instanceof ScenarioAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return 1;
        }
        if (! (second instanceof ScenarioAspectValue)) {
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
