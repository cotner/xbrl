package org.xbrlapi.aspects.alt;

import java.net.URI;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public class PeriodDomain extends DomainImpl implements Domain {

    /**
     * 
     */
    private static final long serialVersionUID = -4367394263354299714L;
    
    protected final static Logger logger = Logger.getLogger(PeriodAspectValue.class);
    
    public PeriodDomain(Store store) throws XBRLException {
        super(store);
    }
    
    /**
     * @see Domain#getAspectId()
     */
    public URI getAspectId() { return PeriodAspect.ID; }
    
    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(AspectValue candidate)
            throws XBRLException {
        if (! (candidate instanceof PeriodAspectValue)) return false;
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
     *         Otherwise, the comparison is based upon date/time ordering
     *         of the end-date/instants and if that ordering is equal or indeterminate,
     *         on the date/time ordering of the start-date/instants.
     *         If the ordering remains indeterminate, then the periods are 
     *         treated as equal.
     *         Missing values are ranked last among aspect values of the same type.
     */
    public int compare(AspectValue first, AspectValue second) {
        if (! (first instanceof PeriodAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return 1;
        }
        if (! (second instanceof PeriodAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return -1;
        }

        if (first.isMissing()) {
            if (second.isMissing()) return 0;
            return 1;
        }
        if (second.isMissing()) return -1;

        PeriodAspectValue f = (PeriodAspectValue) first;
        PeriodAspectValue s = (PeriodAspectValue) second;
     
        if (f.isForever() && s.isForever()) return 0;
        if (f.isForever()) return 1;
        if (s.isForever()) return -1;
        
        XMLGregorianCalendar fC, sC;
        if (f.isDuration()) fC = f.getEnd();
        else fC = f.getInstant();
        if (s.isDuration()) sC = s.getEnd();
        else sC = s.getInstant();
        
        int result = fC.compare(sC);
        if (result == DatatypeConstants.LESSER) return -1;
        if (result == DatatypeConstants.GREATER) return 1;

        if (f.isDuration()) fC = f.getStart();
        if (s.isDuration()) sC = s.getStart();
        
        result = fC.compare(sC);
        if (result == DatatypeConstants.LESSER) return -1;
        if (result == DatatypeConstants.GREATER) return 1;
        
        return 0;
    }

}
