package org.xbrlapi.impl;

import java.net.URI;

import org.xbrlapi.Measure;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MeasureImpl implements Measure {

    private URI namespace = null;
    private String localname = null;
    
    public MeasureImpl(URI namespace, String localname) throws XBRLException {
        super();
        if (namespace == null) throw new XBRLException("The namespace must not be null.");
        if (localname == null) throw new XBRLException("The localname must not be null.");
        this.namespace = namespace;
        this.localname = localname;
        
    }

    /**
     * @see org.xbrlapi.Measure#getLocalname()
     */
    public String getLocalname() {
        return localname;
    }

    /**
     * @see org.xbrlapi.Measure#getNamespace()
     */
    public String getNamespace() {
        return namespace.toString();
    }

}
