package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * Provides an in-memory label caching system.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class MemoryLabelCache implements LabelCache {

    protected final static Logger logger = Logger.getLogger(MemoryLabelCache.class);
    
    private Map<String,String> map = new HashMap<String,String>();
    
    /**
     * @param store The data store to use.
     */
    public MemoryLabelCache() throws XBRLException {
        super();
    }

    /**
     * @see LabelCache#getLabel(URI, String, String, URI, URI)
     */
    public String getLabel(URI aspectId, String valueId, String locale, URI resourceRole, URI linkRole) throws XBRLException {
        String key = aspectId + valueId + locale + resourceRole + linkRole;
        if (map.containsKey(key)) return map.get(key);
        return null;
    }
    
    /**
     * @see LabelCache#cacheLabel(URI, String, String, URI, URI, String)
     */
    public void cacheLabel(URI aspectId, String valueId, String locale,
            URI resourceRole, URI linkRole, String label) throws XBRLException {
        String key = aspectId + valueId + locale + resourceRole + linkRole;
        map.put(key,label);
    }

}
