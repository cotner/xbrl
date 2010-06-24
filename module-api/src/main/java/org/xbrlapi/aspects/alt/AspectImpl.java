package org.xbrlapi.aspects.alt;

import org.apache.log4j.Logger;
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
     * @param domain The domain for this aspect.
     * @throws XBRLException if the domain is null.
     */
    public AspectImpl(Domain domain) throws XBRLException {
        if (domain == null) throw new XBRLException("The domain must not be null.");
        if (domain.getAspectId().equals(this.getId())) throw new XBRLException("The domain " + domain.getAspectId() + " is not for aspect " + this.getId());
        this.domain = domain;
    }
    
    /**
     * @see Aspect#getDomain()
     */
    public Domain getDomain() {
        return domain;
    }

}
