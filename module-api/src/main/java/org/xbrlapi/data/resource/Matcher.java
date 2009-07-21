package org.xbrlapi.data.resource;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * The resource matcher interface, defining all methods that need to be 
 * implemented by a resource matcher to support XBRLAPI data stores.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Matcher extends Serializable {

    /**
     * Call this method if you need to use a different signature
     * generator for the resources you are working with.
     * @param signer The object used to generate signatures.
     */
    public void setSigner(Signer signer);
    
    /**
     * @param uri The URI of the resource to obtain the signature for.
     * @return the signature for the resource specified by the URI or null
     * if the resource could not be accessed.
     * @throws XBRLException if the signature cannot be constructed.
     */
    public String getSignature(URI uri) throws XBRLException;
    
    /**
     * As a side effect, this method adds the specified
     * URI to the matcher.
     * @param uri The URI of the resource to be tested for a match.
     * @return the URI of the matching resource in the data store if one
     * exists and the original URI otherwise (because it becomes the
     * matching URI in the data store).
     * @throws XBRLException
     */
	public URI getMatch(URI uri) throws XBRLException;
	
	/**
	 * @param uri The URI to test.
	 * @return true if the matcher has a URI that is a match for the 
	 * given URI (this match may be the URI itself).
	 * @throws XBRLException
	 */
	public boolean hasURI(URI uri) throws XBRLException;
	
	
	/**
     * @param uri The URI of the resource to be tested for a match.
     * @return a list of all URIs that match the given URI, including the given URI.
     * @throws XBRLException
	 */
	public List<URI> getAllMatchingURIs(URI uri) throws XBRLException;
	
	/**
	 * Deletes the given URI from the matcher.
	 * @param uri The URI to delete.
	 * @return the new match URI for documents that used to 
	 * match the given URI or null if there is none.
	 * @throws XBRLException if the URI is null.
	 */
	public URI delete(URI uri) throws XBRLException;
	
}
