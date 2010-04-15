package org.xbrlapi.data.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.cache.Cache;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public abstract class BaseMatcherImpl implements Matcher {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -3402075120037095009L;

    private static final Logger logger = Logger.getLogger(BaseMatcherImpl.class);    
    
    /**
     * The cache implementation to be used by the matcher when accessing
     * resources.  If this is null, then no cache is used.
     */
    private Cache cache = null;
    protected Cache getCache() {
        return cache;
    }
    protected void setCache(Cache cache) {
        this.cache = cache;
    }
    
    private Signer signer = null;
    protected Signer getSigner() {
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
    public BaseMatcherImpl(Cache cache,Signer signature) throws XBRLException {
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
     * @see org.xbrlapi.data.resource.Matcher#getSignature(URI)
     */
    public String getSignature(URI uri) throws XBRLException {
        
        cache.getCacheURI(uri); // Caches the URI if it is not already cached.
        File cacheFile = cache.getCacheFile(uri);
        
        if (cacheFile.exists()) {
            return getSigner().getSignature(getResourceContent(cacheFile));
        }

        List<String> content = new Vector<String>();
        content.add(uri.toString());
        return getSigner().getSignature(content);
        
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
    
    protected List<String> getResourceContent(URI uri) throws XBRLException {
        try {
            InputStream stream = uri.toURL().openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            List<String> lines = new Vector<String>();
            String line = null;
            while ((line=reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            return lines;
        } catch (IOException e) {
            throw new XBRLException("There was a problem reading lines in from " + uri,e);
        }
    }    


    

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cache == null) ? 0 : cache.hashCode());
        result = prime * result + ((signer == null) ? 0 : signer.hashCode());
        return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseMatcherImpl other = (BaseMatcherImpl) obj;
        if (cache == null) {
            if (other.cache != null)
                return false;
        } else if (!cache.equals(other.cache))
            return false;
        if (signer == null) {
            if (other.signer != null)
                return false;
        } else if (!signer.equals(other.signer))
            return false;
        return true;
    }    
    
    
    
    
}
