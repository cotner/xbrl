package org.xbrlapi.data.resource;

import java.net.URI;

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
     * @param uri The URI of the resource to obtain the signature for.
     * @return the signature for the resource specified by the URI or null
     * if the resource could not be cached (generally because it does not exist).
     * @throws XBRLException if the signature cannot be constructed.
     */
    public String getSignature(URI uri) throws XBRLException;
    
    /**
     * @param uri The URI of the resource to be tested for a match.
     * @return the URI of the matching resource in the data store if one
     * exists and the original URI otherwise (because it becomes the
     * matching URI in the data store).
     * @throws XBRLException
     */
	public URI getMatch(URI uri) throws XBRLException;
	
}
