package org.xbrlapi.data.resource;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.utilities.XBRLException;

/**
 * The default resource matcher implementation is used by stores by default.  
 * Note that this matcher is not persistent and identifies no matches.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class DefaultMatcherImpl implements Matcher, Serializable {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -2009126809674227559L;

    public DefaultMatcherImpl() {
        ;
    }

    /**
     * @see Matcher#setSigner(Signer)
     */
    public void setSigner(Signer signer) {
        return;
    }
    
    /**
     * @see Matcher#getMatch(URI)
     */
    public synchronized URI getMatch(URI uri) throws XBRLException {
        return uri;
    }
    
    

    /**
     * @see Matcher#getSignature(URI)
     */
    public String getSignature(URI uri) throws XBRLException {
        return uri.toString();
    }
    
    /**
     * @see Matcher#delete(URI)
     */
    public URI delete(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The URI must not be null.");
        return null;
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#getAllMatchingURIs(java.net.URI)
     */
    public List<URI> getAllMatchingURIs(URI uri) throws XBRLException {
        List<URI> result = new Vector<URI>();
        result.add(uri);
        return result;
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#hasURI(java.net.URI)
     */
    public boolean hasURI(URI uri) throws XBRLException {
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 1;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
    
}
