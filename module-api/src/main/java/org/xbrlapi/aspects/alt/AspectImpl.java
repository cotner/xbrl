package org.xbrlapi.aspects.alt;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of common aspect methods.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
abstract public class AspectImpl<V extends AspectValue> implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = -255457317327893545L;

    private static final Logger logger = Logger.getLogger(AspectImpl.class);
    
    protected Domain<V> domain;

    /**
     * @param domain The domain for this aspect.
     * @throws XBRLException if the domain is null.
     */
    public AspectImpl(Domain<V> domain) throws XBRLException {
        if (domain == null) throw new XBRLException("The domain must not be null.");
        this.domain = domain;
    }

}
