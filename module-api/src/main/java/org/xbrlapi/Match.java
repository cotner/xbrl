package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;


/**
 * Used to store URI mapping information 
 * about identical documents.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Match extends XML {

    /**
     * @param uri The URI to add to the match XML resource.
     * @throws XBRLException if the URI is null.
     */
    public void setResourceURI(URI uri) throws XBRLException;
    
    /**
     * @return the matching URI defined by this match resource or null
     * if none is defined.
     * @throws XBRLException if the matching URI has invalid syntax.
     */
    public URI getMatch() throws XBRLException;
    
    /**
     * Call this method, for example, when a document is
     * deleted from the data store so matching information needs
     * to be updated.
     * @param uri The URI to eliminate from the match resource.
     * @throws XBRLException
     */
    public void deleteURI(URI uri) throws XBRLException;    
}