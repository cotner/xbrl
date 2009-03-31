package org.xbrlapi.data.resource;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * The default resource matcher implementation is used by stores by default.  
 * Note that this matcher is not persistent and identifies no matches.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class DefaultMatcherImpl implements Matcher {

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
    public URI getMatch(URI uri) throws XBRLException {
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

}
