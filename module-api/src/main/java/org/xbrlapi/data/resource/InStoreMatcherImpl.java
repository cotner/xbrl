package org.xbrlapi.data.resource;

import java.net.URI;
import java.util.HashMap;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.MockImpl;
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
        
        String query = "/*[" + Constants.XBRLAPIPrefix + ":resource/@uri='" + uri +"']";
        FragmentList<Fragment> matches = getStore().query(query);
        if (matches.getLength() > 1) throw new XBRLException("The wrong number of match fragments was retrieved.  There must be just one.");
        
        if (matches.getLength() == 0) {

            String signature = null;
            try {
                signature = this.getSignature(uri);
            } catch (XBRLException e) {
                logger.warn("The URI matching process failed. " + e.getMessage());
                this.matchMap.put(uri,uri);
                return uri;
            }
            
            if (getStore().hasFragment(signature)) {
                Fragment match = getStore().getFragment(signature);
                HashMap<String,String> attr = new HashMap<String,String>();
                attr.put("uri",uri.toString());
                match = getStore().getFragment(signature);
                match.appendMetadataElement("resource",attr);
                URI matchURI = match.getURI();
                this.matchMap.put(uri,matchURI);
                return matchURI;
            } 

            Fragment match = new MockImpl(signature);
            HashMap<String,String> attr = new HashMap<String,String>();
            attr.put("uri",uri.toString());
            match.setMetaAttribute("uri",uri.toString());
            match.appendMetadataElement("resource",attr);
            getStore().storeFragment(match);
            this.matchMap.put(uri,uri);
            return uri;
            
        } 

        Fragment match = matches.get(0);
        URI matchURI = match.getURI();
        this.matchMap.put(uri,matchURI);
        return matchURI;
        
    }
    


}
