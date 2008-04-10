package org.xbrlapi.data.resource;

import java.net.URL;

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
     * @see org.xbrlapi.data.resource.Matcher#getMatch(URL)
     */
    public URL getMatch(URL url) throws XBRLException {
        return url;
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#getSignature(URL)
     */
    public String getSignature(URL url) throws XBRLException {
        return url.toString();
    }

}
