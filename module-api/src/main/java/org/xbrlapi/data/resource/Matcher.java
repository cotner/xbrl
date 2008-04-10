package org.xbrlapi.data.resource;

import java.net.URL;

import org.xbrlapi.utilities.XBRLException;

/**
 * The resource matcher interface, defining all methods that need to be 
 * implemented by a resource matcher to support XBRLAPI data stores.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Matcher {

    /**
     * Call this method if you need to use a different signature
     * generator for the resources you are working with.
     * @param signer The object used to generate signatures.
     */
    public void setSigner(Signer signer);
    
    /**
     * @param url The URL of the resource to obtain the signature for.
     * @return the signature for the resource specified by the URL.
     * @throws XBRLException if the signature cannot be constructed.
     */
    public String getSignature(URL url) throws XBRLException;
    
    /**
     * @param url The URL of the resource to be tested for a match.
     * @return the URL of the matching resource in the data store if one
     * exists and the original URL otherwise (because it becomes the
     * matching URL in the data store).
     * @throws XBRLException
     */
	public URL getMatch(URL url) throws XBRLException;
	
}
