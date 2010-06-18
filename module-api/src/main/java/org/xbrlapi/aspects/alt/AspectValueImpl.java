package org.xbrlapi.aspects.alt;

import org.apache.log4j.Logger;

/**
 * This is a generic aspect value implementation. All concrete aspect value
 * implementations should extend this generic implementation.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class AspectValueImpl implements AspectValue {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5016688902526266720L;

    protected final static Logger logger = Logger
            .getLogger(AspectValueImpl.class);
    
}
