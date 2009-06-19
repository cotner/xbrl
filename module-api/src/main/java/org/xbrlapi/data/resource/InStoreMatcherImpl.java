package org.xbrlapi.data.resource;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.xbrlapi.Match;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.MatchImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * The in-store resource matcher implementation, for use with the
 * persistent store implementations.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InStoreMatcherImpl extends BaseMatcherImpl implements Matcher {

    /**
     * The store in which the information about matched URIs is
     * to be retained.
     */
    private Store store = null;
    private Store getStore() {
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
    public InStoreMatcherImpl(Store store, CacheImpl cache) throws XBRLException {
        super(cache,new MD5SignerImpl());
        if (store == null) {
            throw new XBRLException("The store must not be null.");
        }
        setStore(store);
    }


    /**
     * The matches hashmap gets populated over time to 
     * ensure that a match only has to be sought in the database
     * once for each URI that is looked up.  This in-memory
     * duplication should significantly improve search performance 
     * over time as the matcher operates.
     */
    private HashMap<URI,URI> matchMap = new HashMap<URI,URI>();
    
    /**
     * @see org.xbrlapi.data.resource.Matcher#getMatch(URI)
     */
    public URI getMatch(URI uri) throws XBRLException {
        
        if (matchMap.containsKey(uri)) return matchMap.get(uri);
        
        Match match = this.getMatchXML(uri);
        
        if (match != null) {
            logger.debug("The match is not null.");
        }
        
        if (match == null) {

            String signature = null;
            try {
                signature = this.getSignature(uri);
            } catch (XBRLException e) {
                logger.warn("The URI matching process failed. " + e.getMessage());
                this.matchMap.put(uri,uri);
                return uri;
            }
            
            if (getStore().hasXML(signature)) {
                match = getStore().<Match>getFragment(signature);
                match.setResourceURI(uri);
                getStore().persist(match);
                URI result = match.getMatch();
                logger.debug(result);
                return result;
            } 

            match = new MatchImpl(signature);
            match.setResourceURI(uri);
            getStore().persist(match);
            this.matchMap.put(uri,uri);
            logger.debug(uri);
            return uri;
        } 

        URI matchURI = match.getMatch();
        this.matchMap.put(uri,matchURI);
        logger.debug(matchURI);
        return matchURI;
        
    }
    
    /**
     * @see Matcher#delete(URI)
     */
    public URI delete(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The URI must not be null.");

        Match match = this.getMatchXML(uri);
        if (match == null) return null;
        match.deleteURI(uri);
        
        // Update the memory map
        URI matchURI = match.getMatch();
        if (matchURI != null) {
            matchMap.remove(uri);
            if (uri.equals(this.getMatch(uri))) {
                for (URI key: matchMap.keySet()) {
                    if (matchMap.get(key).equals(uri)) matchMap.put(key,matchURI);
                }
            }
        }
        
        return matchURI;
    }    
    
    private Match getMatchXML(URI uri) throws XBRLException {
        String query = "#roots#[@type='org.xbrlapi.impl.MatchImpl' and "+ Constants.XBRLAPIPrefix + ":match/@value='" + uri +"']";
        List<Match> matches = getStore().<Match>query(query);
        if (matches.size() > 1) throw new XBRLException("The wrong number of match fragments was retrieved.  There must be just one.");
        if (matches.size() == 0) return null;
        return matches.get(0);
    }

}
