package org.xbrlapi.sax;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.xbrlapi.cache.Cache;
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

public class EntityResolverImpl implements EntityResolver, XMLEntityResolver, Serializable {

	private static final Logger logger = Logger.getLogger(EntityResolverImpl.class);		
	
    /**
     * The local document cache.
     */
    private Cache cache = null;

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
	 * Create the entity resolver with a set of local URIs 
	 * to be used by the loader in place of actual URIs.  
	 * These local URIs, pointing to resources on the local file system, are used
	 * by the loader's entity resolver to swap the local resource for the  
	 * original resource at the original URI.  Such substitutions are used by the 
	 * entity resolver when doing SAX parsing and when building XML Schema grammar
	 * models.
	 * @param cacheRoot The root directory of the local cache.
	 * @param uriMap The map from original URIs to local URIs.
	 * @throws XBRLException if any of the objects in the list of URIs is not a 
	 * java.net.URI object.
	 */
	public EntityResolverImpl(File cacheRoot, Map<URI, URI> uriMap) throws XBRLException {
		this.cache = new CacheImpl(cacheRoot, uriMap);
	}
    
    /**
     * Resolve the entity for a SAX parser using the system identifier.
     * @param publicId The public identifier.
     * @param systemId The system identifier that gets resolved.
     */
    public InputSource resolveEntity(String publicId, String systemId) {

		logger.debug("SAX: Resolving the entity for " + systemId);
    	
    	try {
    		URI uri = new URI(systemId);
    		if (hasCache()) { 
    		    uri = cache.getCacheURI(uri);
    		}
    		return new InputSource(uri.toString());

    	} catch (XBRLException e) {
    		logger.warn("Cache handling for " + systemId + "failed.");
    		return new InputSource(systemId);
    	} catch (URISyntaxException e) {
    		logger.warn(systemId + " is a malformed URI.");
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
            logger.debug("SCHEMA: Resolving the entity for " + resource.getExpandedSystemId());
			
			URI uri = new URI(resource.getExpandedSystemId());
			if (hasCache()) {
			    uri = cache.getCacheURI(uri);
			}
			logger.debug("... so resolving the entity for URI " + uri);
			
			return new XMLInputSource(resource.getPublicId(),uri.toString(), uri.toString());
			
    	} catch (XBRLException e) {
    		logger.warn("Cache handling for " + resource.getExpandedSystemId() + "failed.");
			return new XMLInputSource(resource.getPublicId(),resource.getExpandedSystemId(), resource.getBaseSystemId());
    	} catch (URISyntaxException e) {
    		logger.warn(resource.getExpandedSystemId() + " is a malformed URI.");
			return new XMLInputSource(resource.getPublicId(),resource.getExpandedSystemId(), resource.getBaseSystemId());
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
        return result;
    }

    /**
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
        EntityResolverImpl other = (EntityResolverImpl) obj;
        if (cache == null) {
            if (other.cache != null)
                return false;
        } else if (!cache.equals(other.cache))
            return false;
        return true;
    }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(cache);
   }    
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        this.cache = (Cache) in.readObject();
    }
    
}
