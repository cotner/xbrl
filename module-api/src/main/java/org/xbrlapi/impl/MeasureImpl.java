package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.xbrlapi.Measure;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MeasureImpl implements Measure {

    private URI namespace = null;
    private String localname = null;
    
    public MeasureImpl(String namespace, String localname) throws XBRLException {
        super();
        if (namespace == null) throw new XBRLException("The namespace must not be null.");
        if (localname == null) throw new XBRLException("The localname must not be null.");
        try {
            this.namespace = new URI(namespace);
        } catch (URISyntaxException e) {
            throw new XBRLException("The namespace syntax is malformed.",e);
        }
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
