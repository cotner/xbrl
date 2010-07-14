package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public class TypedDimensionDomain extends DimensionDomainImpl implements Domain {

    /**
     * 
     */
    private static final long serialVersionUID = 7609975616855275437L;
    
    protected final static Logger logger = Logger.getLogger(TypedDimensionDomain.class);

    public TypedDimensionDomain(Store store, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(store,dimensionNamespace,dimensionLocalname);
    }
    

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(AspectValue candidate)
            throws XBRLException {
        if (! (candidate instanceof TypedDimensionAspectValue)) return false;
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
        if (! (first instanceof TypedDimensionAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return 1;
        }
        if (! (second instanceof TypedDimensionAspectValue)) {
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
