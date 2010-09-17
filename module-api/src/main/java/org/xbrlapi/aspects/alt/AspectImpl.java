package org.xbrlapi.aspects.alt;

import org.apache.log4j.Logger;
import org.xbrlapi.Context;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of common aspect methods.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
abstract public class AspectImpl implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = -255457317327893545L;

    private static final Logger logger = Logger.getLogger(AspectImpl.class);
    
    protected Domain domain;

    /**
     * @param domain
     *            The domain for this aspect.
     * @throws XBRLException
     *             if the domain is null or the domain aspect ID does not match
     *             the ID of the aspect it is being associated with.
     */
    public AspectImpl(Domain domain) throws XBRLException {
        if (domain == null) throw new XBRLException("The domain must not be null.");
        if (! this.isExtensible())
            if (! domain.getAspectId().equals(this.getId())) 
                throw new XBRLException("The domain " + domain.getAspectId() + " is not for aspect " + this.getId());
        this.domain = domain;
    }
    
    /**
     * @see Aspect#getDomain()
     */
    public Domain getDomain() {
        return domain;
    }
    
    /**
     * @see Aspect#isExtensible()
     */
    public boolean isExtensible() {
        return false;
    }
    
    /**
     * @see Aspect#getValue(Context)
     */
    public AspectValue getValue(Context context) throws XBRLException {
        return getMissingValue();
    }

    /**
     * @see Aspect#getValue(Unit)
     */
    public AspectValue getValue(Unit unit) throws XBRLException {
        return getMissingValue();
    }

}
