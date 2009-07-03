package org.xbrlapi.data.resource;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.cache.Cache;
import org.xbrlapi.utilities.XBRLException;

/**
 * The in-memory resource matcher implementation, for use with the
 * DOM data store implementation.  Note that this matcher is not persistent.
 * Sub-class if you want to use an alternative signature.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InMemoryMatcherImpl extends BaseMatcherImpl implements Matcher {

    /**
     * Map from signature strings to lists of URIs with the same signature.
     * The first URI in the list is the URI of the resource in the data
     * store.
     */
    private HashMap<String,List<URI>> map = new HashMap<String,List<URI>>();
    private HashMap<String,List<URI>>getMap() {
        return map;
    }

    /**
     * @param cache The resource cache to be used by the matcher when accessing
     * resources to determine their signature.
     * @throws XBRLException if the cache parameter is null.
     */
    public InMemoryMatcherImpl(Cache cache) throws XBRLException {
        super(cache,new MD5SignerImpl());
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#getMatch(URI)
     */
    public synchronized URI getMatch(URI uri) throws XBRLException {
        String signature = this.getSignature(uri);
        if (signature == null) throw new XBRLException("The signature could not be generated.");
        if (getMap().containsKey(signature)) {
            List<URI> matches = getMap().get(signature);
            if (! matches.contains(uri)) matches.add(uri);
        } else {
            List<URI> list = new Vector<URI>();
            list.add(uri);
            getMap().put(signature,list);
        }
        return getMap().get(signature).get(0);
    }
    
    /**
     * @see org.xbrlapi.data.resource.Matcher#getAllMatchingURIs(java.net.URI)
     */
    public List<URI> getAllMatchingURIs(URI uri) throws XBRLException {

        List<URI> result = new Vector<URI>();

        String signature = this.getSignature(uri);

        if (getMap().containsKey(signature)) {
            List<URI> matches = getMap().get(signature);
            return matches;
        }

        result.add(uri);
        return result;

    }    
    
    /**
     * @see Matcher#delete(URI)
     */
    public URI delete(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The URI must not be null.");
        String signature = this.getSignature(uri);
        
        List<URI> uris = map.get(signature);
        
        for (URI item: uris) {
            if (item.equals(uri)) uris.remove(item);
        }

        if (uris.isEmpty()) {
            map.remove(signature);
            return null;
        }
        return uris.get(0);
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#hasURI(java.net.URI)
     */
    public boolean hasURI(URI uri) throws XBRLException {
        return getMap().containsKey(getSignature(uri));
    }    

}
