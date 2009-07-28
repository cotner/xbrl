package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

public interface Stub extends NonFragmentXML {

    /**
     * @return the reason that the stub was stored.
     * @throws XBRLException
     */
    public String getReason() throws XBRLException;
    
    /**
     * @return the URI of the affected document.
     * @throws XBRLException if the URI syntax is incorrect.
     */
    public URI getResourceURI() throws XBRLException;
    
    /**
     * @param uri the URI of the document described by the stub.
     * @throws XBRLException if the URI is null
     */
    public void setResourceURI(URI uri) throws XBRLException;    
    
    /**
     * @param reason The reason for the stub to exist.
     * @throws XBRLException if the reason is null
     */
    public void setReason(String reason) throws XBRLException;        
}
