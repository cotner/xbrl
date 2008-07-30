package org.xbrlapi.data.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public abstract class BaseMatcherImpl implements Matcher {

    Logger logger = Logger.getLogger(BaseMatcherImpl.class);    
    
    /**
     * The cache implementation to be used by the matcher when accessing
     * resources.  If this is null, then no cache is used.
     */
    private CacheImpl cache = null;
    protected CacheImpl getCache() {
        return cache;
    }
    protected void setCache(CacheImpl cache) {
        this.cache = cache;
    }
    
    Signer signer = null;
    protected Signer getSignature() {
        return signer;
    }
    public void setSigner(Signer signer) {
        this.signer = signer;
    }

    /**
     * @param cache The resource cache to be used by the matcher when accessing
     * resources to determine their signature.
     * @param signature The object used to generate resource signatures.
     * @throws XBRLException if either parameter is null.
     */
    public BaseMatcherImpl(CacheImpl cache,Signer signature) throws XBRLException {
        if (cache == null) {
            throw new XBRLException("Resource matchers must use a resource cache.");
        }
        setCache(cache);
        if (signature == null) {
            throw new XBRLException("Resource matchers must use a signature generator.");
        }
        setSigner(signature);
    }
    
    
    /**
     * @see org.xbrlapi.data.resource.Matcher#getSignature(URL)
     */
    public String getSignature(URL url) throws XBRLException {
        cache.getCacheURL(url); // Caches if not already cached.
        File cacheFile = cache.getCacheFile(url);
        if (cacheFile.exists()) {
            List<String> lines = getResourceContent(cacheFile);
            return getSignature().getSignature(lines);
        }
        return null;
    }

    protected List<String> getResourceContent(File file) throws XBRLException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> lines = new Vector<String>();
            String line = null;
            while ((line=reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            return lines;
        } catch (IOException e) {
            throw new XBRLException("There was a problem reading " + file + " from the cache.",e);
        }
    }

}
