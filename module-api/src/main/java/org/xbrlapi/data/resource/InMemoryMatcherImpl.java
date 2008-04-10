package org.xbrlapi.data.resource;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * The in-memory resource matcher implementation, for use with the
 * DOM data store implementation.  Note that this matcher is not persistent.
 * Sub-class if you want to use an alternative signature.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InMemoryMatcherImpl extends BaseMatcherImpl implements Matcher {

    /**
     * Map from signature strings to lists of URLs with the same signature.
     * The first URL in the list is the URL of the resource in the data
     * store.
     */
    private HashMap<String,List<URL>> map = new HashMap<String,List<URL>>();
    private HashMap<String,List<URL>>getMap() {
        return map;
    }

    /**
     * @param cache The resource cache to be used by the matcher when accessing
     * resources to determine their signature.
     * @throws XBRLException if the cache parameter is null.
     */
    public InMemoryMatcherImpl(CacheImpl cache) throws XBRLException {
        super(cache,new MD5SignerImpl());
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#getMatch(URL)
     */
    public URL getMatch(URL url) throws XBRLException {
        String signature = this.getSignature(url);
        if (getMap().containsKey(signature)) {
            List<URL> matches = getMap().get(signature);
            if (! matches.contains(url))
                matches.add(url);
        } else {
            List<URL> list = new Vector<URL>();
            list.add(url);
            getMap().put(signature,list);
        }
        return getMap().get(signature).get(0);
    }

}
