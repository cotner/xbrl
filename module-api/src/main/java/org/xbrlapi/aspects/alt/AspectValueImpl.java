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

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId().hashCode();
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AspectValueImpl other = (AspectValueImpl) obj;
        if (! getId().equals(other.getId()))
            return false;
        return true;
    }
    
    
}
