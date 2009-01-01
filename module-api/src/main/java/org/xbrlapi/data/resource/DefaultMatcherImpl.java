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
    }

    public void setSigner(Signer signer) {
        return;
    }
    
    /**
     * @see org.xbrlapi.data.resource.Matcher#getMatch(URI)
     */
    public URI getMatch(URI uri) throws XBRLException {
        return uri;
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#getSignature(URI)
     */
    public String getSignature(URI uri) throws XBRLException {
        return uri.toString();
    }

}
