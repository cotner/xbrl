package org.xbrlapi.data.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.Match;
import org.xbrlapi.cache.Cache;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.MatchImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * The in-store resource matcher implementation, for use with the
 * persistent store implementations.  This matcher MUST use the
 * store that it is doing the matching for.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InStoreMatcherImpl extends BaseMatcherImpl implements Matcher {

    /**
     * 
     */
    private static final long serialVersionUID = -2815835104865947253L;

    /**
     * The store in which the information about matched URIs is
     * to be retained.
     */
    private Store store = null;
    
    /**
     * @return the store used by this matcher.
     */
    public Store getStore() {
        return store;
    }
    private void setStore(Store store) {
        this.store = store;
    }
    
    /**
     * @param store The data store to use for persisting resource matches
     * @param cache The resource cache to be used by the matcher when accessing
     * resources to determine their signature.
     * @throws XBRLException if the cache parameter is null.
     */
    public InStoreMatcherImpl(Store store, Cache cache) throws XBRLException {
        super(cache,new MD5SignerImpl());
        if (store == null) {
            throw new XBRLException("The store must not be null.");
        }
        setStore(store);
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#getMatch(URI)
     */
    public synchronized URI getMatch(URI uri) throws XBRLException {
        
        String query = "for $match in #roots#[@type='org.xbrlapi.impl.MatchImpl']/" + matchElement + " where $match/@value='"+ uri + "' return string($match/../" + matchElement + "[1]/@value)";
        Set<String> matches = getStore().queryForStrings(query);
        
        if (matches.size() == 1) { // We have already captured this URI in the matcher.
            String matchURI = matches.iterator().next();
            try {
                return new URI(matchURI);
            } catch (URISyntaxException e) {
                throw new XBRLException("The matching URI syntax is invalid: " + matchURI);
            }
        }

        if (matches.size() == 0) { 
            return addURI(uri); // This URI remains to be captured.
        }

        throw new XBRLException("There is more than one matching URI for " + uri);

    }
    
    /**
     * @param uri The URI to add to the matcher system.
     * @return the URI that matches the given URI, after the addition.
     * @throws XBRLException
     */
    private URI addURI(URI uri) throws XBRLException {

        String signature = this.getSignature(uri);

        Store store = getStore();
        
        if (store.hasXMLResource(signature)) {
            Match match = store.<Match>getXMLResource(signature);
            match.addMatchedURI(uri);
            store.persist(match);
            return match.getMatch();
        } 
        
        Match match = new MatchImpl(signature);
        match.setStore(store);
        match.addMatchedURI(uri);
        store.persist(match);
        return uri;
        
    }
    
    /**
     * @see org.xbrlapi.data.resource.Matcher#getAllMatchingURIs(java.net.URI)
     */
    public List<URI> getAllMatchingURIs(URI uri) throws XBRLException {

        Match match = this.getMatchXMLResource(uri);
        if (match == null) return new Vector<URI>();
        return match.getURIs();

    }    
    
    private final static String matchElement = Constants.XBRLAPIPrefix + ":match";
    
    /**
     * @see Matcher#delete(URI)
     */
    public synchronized URI delete(URI uri) throws XBRLException {
        
        if (uri == null) throw new XBRLException("The URI must not be null.");

        Match match = this.getMatchXMLResource(uri);
        if (match == null) return null;
        match.deleteURI(uri);
        return match.getMatch();

    }    
    
    private Match getMatchXMLResource(URI uri) throws XBRLException {
        String query = "for $match in #roots#[@type='org.xbrlapi.impl.MatchImpl'] where $match/" + matchElement + "/@value='"+ uri + "' return $match";
        List<Match> matches = getStore().<Match>queryForXMLResources(query);
        if (matches.size() > 1) throw new XBRLException("The wrong number of match fragments was retrieved.  There must be just one.");
        if (matches.size() == 0) return null;
        return matches.get(0);
    }
    /**
     * @see org.xbrlapi.data.resource.Matcher#hasURI(java.net.URI)
     */
    public boolean hasURI(URI uri) throws XBRLException {
        return (this.getMatchXMLResource(uri) != null);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        InStoreMatcherImpl other = (InStoreMatcherImpl) obj;
        if (store == null) {
            if (other.store != null)
                return false;
        } else if (!store.equals(other.store))
            return false;
        return true;
    }


    

    
    
}
