package org.xbrlapi.data.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.MockFragmentImpl;
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
     * @see org.xbrlapi.data.resource.Matcher#getMatch(URI)
     */
    public URI getMatch(URI uri) throws XBRLException {
        logger.debug("Getting match for " + uri);
        String signature = this.getSignature(uri);
        if (signature == null) return uri;
        Fragment match = null;
        if (getStore().hasFragment(signature)) {
            logger.debug(uri + " has a match already");
            String query = "/*[*/@uri='" + uri + "']";
            FragmentList<Fragment> matches = getStore().query(query);
            if (matches.getLength() == 0) {
                HashMap<String,String> attr = new HashMap<String,String>();
                attr.put("uri",uri.toString());
                match = getStore().getFragment(signature);
                match.appendMetadataElement("resource",attr);
            } else {
                match = matches.get(0);
            }
        } else {
            logger.debug(uri + " has no existing matches");
            match = new MockFragmentImpl(signature);
            HashMap<String,String> attr = new HashMap<String,String>();
            attr.put("uri",uri.toString());
            match.appendMetadataElement("match",attr);
            match.appendMetadataElement("resource",attr);
            store.storeFragment(match);
        }
        NodeList nodes = match.getMetadataRootElement().getElementsByTagNameNS(Constants.XBRLAPINamespace,"match");
        Element element = (Element) nodes.item(0);
        try {
            return new URI(element.getAttribute("uri"));
        } catch (URISyntaxException e) {
            throw new XBRLException("Unexpected malformed URI.",e);
        }
        
    }

}
