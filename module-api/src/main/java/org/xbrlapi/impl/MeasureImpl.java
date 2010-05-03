package org.xbrlapi.impl;

import java.net.URI;

import org.xbrlapi.Measure;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MeasureImpl implements Measure {

    /**
     * 
     */
    private static final long serialVersionUID = -3990813740480933241L;
    
    private final URI namespace;
    transient private final String prefix;
    private final String localname;
    
    public MeasureImpl(URI namespace, String prefix, String localname) throws XBRLException {
        super();
        if (namespace == null) throw new XBRLException("The namespace must not be null.");
        if (prefix == null) throw new XBRLException("The prefix must not be null.");
        if (localname == null) throw new XBRLException("The localname must not be null.");
        this.namespace = namespace;
        this.prefix = prefix;
        this.localname = localname;        
    }

    /**
     * @see org.xbrlapi.Measure#getLocalname()
     */
    public String getLocalname() {
        return localname;
    }

    /**
     * @see org.xbrlapi.Measure#getPrefix()
     */
    public String getPrefix() {
        return prefix;
    }
    
    /**
     * @see org.xbrlapi.Measure#getNamespace()
     */
    public URI getNamespace() {
        return namespace;
    }

    /**
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Measure other) {

        if (other == null)
            throw new NullPointerException();

        if (this == other)
            return 0;

        if (getClass() != other.getClass())
            return 1;

        int result = namespace.compareTo(other.getNamespace());
        if (result != 0) return result;
        result = localname.compareTo(other.getLocalname());
        return result;
    }
    
}
