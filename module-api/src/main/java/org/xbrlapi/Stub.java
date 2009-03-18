package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

public interface Stub extends XML {

    /**
     * @return the reason that the stub was stored.
     * @throws XBRLException
     */
    public String getReason() throws XBRLException;
    
    /**
     * @return the URI of the affected document.
     * @throws XBRLException if the URI syntax is incorrect.
     */
    public URI getURI() throws XBRLException;
}
