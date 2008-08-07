package org.xbrlapi.sax;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


/**
 * Entity resolver that dynamically adds to the local document
 * cache if it is set up and that gives preference to the local 
 * cache (if it is set up) as resources are identified by the 
 * resolution process.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class EntityResolverImpl implements EntityResolver, XMLEntityResolver {

	static Logger logger = Logger.getLogger(EntityResolverImpl.class);		
	
    /**
     * The local document cache.
     */
    private CacheImpl cache = null;

    /**
     * Construct the entity resolver without a cache.
     */
    public EntityResolverImpl() {
        ;
    }
    
    
    /**
     * Construct the entity resolver by storing the cache root.
     * @param cacheRoot The root directory of the local cache.
     * @throws XBRLException if the cache cannot be initialised.
     */
    public EntityResolverImpl(File cacheRoot) throws XBRLException {
		this.cache = new CacheImpl(cacheRoot);
    }

	/**
	 * Create the entity resolver with a set of local URLs 
	 * to be used by the loader in place of actual URLs.  
	 * These local URLs, pointing to resources on the local file system, are used
	 * by the loader's entity resolver to swap the local resource for the  
	 * original resource at the original URL.  Such substitutions are used by the 
	 * entity resolver when doing SAX parsing and when building XML Schema grammar
	 * models.
	 * @param cacheRoot The root directory of the local cache.
	 * @param urlMap The map from original URLs to local URLs.
	 * @throws XBRLException if any of the objects in the list of URLs is not a 
	 * java.net.URL object.
	 */
	public EntityResolverImpl(File cacheRoot, Map<String,String> urlMap) throws XBRLException {
		this.cache = new CacheImpl(cacheRoot, urlMap);
	}
    
    /**
     * Resolve the entity for a SAX parser using the system identifier.
     * @param publicId The public identifier.
     * @param systemId The system identifier that gets resolved.
     */
    public InputSource resolveEntity(String publicId, String systemId) {

		logger.debug(System.currentTimeMillis() + " SAX: Resolving the entity for " + systemId);
    	
    	try {
    		URL url = new URL(systemId);
    		if (hasCache()) { 
    		    url = cache.getCacheURL(url);
    		}
    		return new InputSource(url.toString());

    	} catch (XBRLException e) {
    		logger.warn("Cache handling for " + systemId + "failed.");
    		return new InputSource(systemId);
    	} catch (MalformedURLException e) {
    		logger.warn(systemId + " is a malformed URL.");
    		return new InputSource(systemId);
    	}

    }

    /**
     * @return true if the resolver has a cache and false otherwise.
     */
    private boolean hasCache() {
        if (cache == null) return false;
        return true;
    }
    
	/**
	 * Implements the resolveEntity method defined in the org.apache.xerces.xni.parser.XMLEntityResolver
	 * interface, incorporating interactions with the local document cache (if it exists) to ensure that any
	 * new documents are cached and any documents already in the cache are sourced from the cache.
	 * @param resource The XML Resource Identifier used to identify the XML resource to be converted
	 * into an XML input source and to be cached if it is not already cached.
	 * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
	 */
	public XMLInputSource resolveEntity(XMLResourceIdentifier resource) throws XNIException, IOException {

		try {
			
			URL url = new URL(resource.getExpandedSystemId());
			if (hasCache()) {
			    url = cache.getCacheURL(url);
			}
			logger.debug(System.currentTimeMillis() + " SCHEMA: Resolving the entity for " + url);
			
			return new XMLInputSource(resource.getPublicId(),url.toString(), url.toString());
			
    	} catch (XBRLException e) {
    		logger.warn("Cache handling for " + resource.getExpandedSystemId() + "failed.");
			return new XMLInputSource(resource.getPublicId(),resource.getExpandedSystemId(), resource.getBaseSystemId());
    	} catch (MalformedURLException e) {
    		logger.warn(resource.getExpandedSystemId() + " is a malformed URL.");
			return new XMLInputSource(resource.getPublicId(),resource.getExpandedSystemId(), resource.getBaseSystemId());
    	}

	}    
}
