package org.xbrlapi.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

/**
 * Translates 
 * 1. original URIs into cache File objects or cache URI objects
 * 2. cache URIs into original URI objects
 * The translation from cache URIs to original URIs is a hack that
 * enables relative URIs in cached files to be identified as such an
 * resolved to obtain the original URI of the resource identified
 * by the relative URI.
 * This class also provides a method for testing if a URI is a cache URI.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */
public class CacheImpl {

	Logger logger = Logger.getLogger(CacheImpl.class);
	
    /**
     * Root of the local document cache.
     */
    private File cacheRoot;
    
    /**
     * TODO !!! Make the caching uriMap a map from URI to URI rather than string to string.
     * The map of local URIs to use in place of
     * original URIs.  The original URI points to the 
     * local URI in the map that is used.
     */
    private Map<String,String> uriMap = null;      

    /**
     * Constructs a URI translator for usage with a local cache location.
     * @param cacheRoot The root directory for the cache.
     * @throws XBRLException if the cacheRoot is null or does not exist or cannot be
     * written to or read from.
     */
	public CacheImpl(File cacheRoot) throws XBRLException {
		if (cacheRoot == null) throw new XBRLException("The cache root is null.");
		if (! cacheRoot.exists()) throw new XBRLException("The cache " + cacheRoot + " does not exist.");
		if (! cacheRoot.canWrite()) throw new XBRLException("The cache " + cacheRoot + " cannot be written to.");
        if (! cacheRoot.canRead()) throw new XBRLException("The cache " + cacheRoot + " cannot be read.");
		this.cacheRoot = cacheRoot;
	}
    
    /**
     * Constructs a URI translator for usage with a local cache location.
     * @param cacheRoot
	 * @param uriMap The map from original URIs to local URIs.
     * @throws XBRLException if the cacheRoot is null or does not exist
     */
	public CacheImpl(File cacheRoot, Map<String,String> uriMap) throws XBRLException {
		this(cacheRoot);
		this.uriMap = uriMap;
	}	
	
    /**
     * Tests if a URI is a URI of a resource in the local cache.
     * @param uri The URI to be tested to see if it identifies a 
     * resource in the local cache.
     * @return true if and only if the URI is for a resource in the 
     * local cache.
     * @throws XBRLException if the URI status as a cache URI cannot be determined.
     */
    public boolean isCacheURI(URI uri) throws XBRLException {
    	logger.debug("Checking if " + uri + " is in the cache.");
    	
    	if (! uri.getScheme().equals("file")) {
        	logger.debug("Protocol is wrong so not in cache.");
    		return false;
    	}

    	try {
    	    // TODO Make this test work for windows paths.
    	    logger.debug("The canonical path to the cache root is: " + cacheRoot.getCanonicalPath());
            logger.debug("The path component of the URI being tested is: " + uri.getPath());

            String uriPath = "";
            try {
                uriPath = new File(uri.getPath()).getCanonicalPath();
                logger.debug("Canonicalised URI path is: " + uriPath);
            } catch (Exception couldNotCanonicaliseURIPath) {
                logger.debug("Could not canonicalise URI Path " + uri.getPath() + " so we do not have a cache URI.");
                return false;
            }

            if (uriPath.startsWith(cacheRoot.getCanonicalPath().toString())) {
                logger.debug("Path is right so is in cache.");
                return true;
            }

    	} catch (Exception e) {
    		throw new XBRLException("The canonical cache root path cannot be determined.",e);
    	}
    	logger.debug("Path is wrong so not in cache.");    	
    	return false;
    }
    
    /**
     * TODO Modify to use the java.net.URIEncoder and java.net.URIDecoder classes.
     * Adds the resource at the original URI to the cache if it is not already cached.
     * @param uri The URI to be translated into a cache URI (if necessary).
     * @return the cache URI corresponding to the provided URI.
     * @throws XBRLException if the resource cannot be cached.
     */
    public URI getCacheURI(URI uri) throws XBRLException {

    	logger.debug("About to get the cache URI for " + uri);
	
    	// First determine the original URI
    	URI originalURI = uri;
    	if (isCacheURI(uri)) {
    		originalURI = getOriginalURI(uri);
		} else {
			if (uriMap != null) {
	        	if (uriMap.containsKey(uri.toString())) {
	                try {
                        originalURI = new URI(uriMap.get(uri.toString()));
	                } catch (URISyntaxException e) {
	                    throw new XBRLException(uri + " is a malformed URI.", e);
	                }
	        	}
			}
		}
    	
    	// Second determine the cache file from the original URI
    	// so that we can try to cache it if that is necessary.
    	try {
        	File cacheFile = getCacheFile(originalURI);
    		if (! cacheFile.exists()) {
    			copyToCache(originalURI,cacheFile);
    		}
            return cacheFile.toURI();
    	} catch (XBRLException e) {
            logger.debug(e.getMessage());
            return originalURI;
    	}
        
    }
    
    /**
     * @param uri The URI to be translated into an original URI (if necessary).
     * @return the original (non-cache) URI corresponding to the provided URI.
     * @throws XBRLException if a caching operation fails 
     * or if a cache file cannot be translated into a URI.
     */
    public URI getOriginalURI(URI uri) throws XBRLException {
    	
    	logger.debug("About to get the original URI for " + uri);
    	
    	// Just return the URI if it is not a cache URI
    	if (! isCacheURI(uri)) {
    		logger.debug("Returning the URI as it is already original.");
    		return uri;
    	}

		String data = uri.getPath();
		
		try {
		    data = (new File(data)).getCanonicalPath();
		} catch (IOException e) {
		    throw new XBRLException("Canonical path could not be obtained from the URI.",e);
		}

		// Eliminate the cacheRoot part of the path
		try {
			data = data.replace(cacheRoot.getCanonicalPath().toString().substring(1),"").substring(2);
		} catch (IOException e) {
			throw new XBRLException("The original URI could not be determined for " + uri);
		}
		
		String[] parts = data.split(File.separator);
		
        String scheme = parts[0];
        if (scheme.equals("null")) scheme = null;
        String user = parts[1];
        if (user.equals("null")) user = null;
        String host = parts[2];
        if (host.equals("null")) host = null;
        int port = new Integer(parts[3]).intValue();
        String query = parts[4];
        if (query.equals("null")) query = null;
        String fragment = parts[5];
        if (fragment.equals("null")) fragment = null;

        String path = "";
        for (int i=6; i<parts.length; i++) {
            path += "/" + parts[i];
        }

		try {
			URI originalURI = new URI(scheme, user,host, port, path,query,fragment);
	    	logger.debug("Got the original URI " + originalURI);
			return originalURI;
		} catch (URISyntaxException e) {
			throw new XBRLException("Malformed original URI.",e);
		}

    }
    
    /**
     * Gets the cache file for an original URI.
     * @param uri The URI to obtain the cache file for,
     * @return The File for the provided URI.
     * @throws XBRLException if the URI cannot be translated into
     * a location in the local cache.
     */
    public File getCacheFile(URI uri) throws XBRLException {
        
    	logger.debug("Getting the cache file for " + uri);

        String scheme = uri.getScheme();
        String user = uri.getUserInfo();
        String host = uri.getHost();
        String port = (new Integer(uri.getPort())).toString();
        String path = uri.getPath();
        String query = uri.getQuery();
        String fragment = uri.getFragment();
        
        String s = File.separator;
        String relativeLocation = scheme;
        relativeLocation = relativeLocation.concat(s+user);
        relativeLocation = relativeLocation.concat(s+host);
        relativeLocation = relativeLocation.concat(s+port);
        relativeLocation = relativeLocation.concat(s+query);
        relativeLocation = relativeLocation.concat(s+fragment);
        StringTokenizer tokenizer = new StringTokenizer(path, "/");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token != null)
                if (! token.equals(""))
                    relativeLocation = relativeLocation.concat(s+token);
        }

        try {
            File cacheFile = new File(this.cacheRoot,relativeLocation);
            logger.debug("Got cacheFile" + cacheFile);
            return cacheFile;
        } catch (Exception e) {
            throw new XBRLException(uri + " cannot be translated into a location in the cache");
        }
    	
    }
    
    /**
     * Copy the original resource into the local cache if the resource exists and is
     * able to be copied into the cache and does nothing otherwise.  Thus, caching fails
     * silently.
     * @param originalURI the URI of the resource to be copied into the cache.
     * @param cacheFile The file to be used to store the cache version of the resource.
     */
    public void copyToCache(URI originalURI, File cacheFile) {
    	
    	// If necessary, create the directory to contain the cached resource
		File parent = cacheFile.getParentFile();
		if (parent != null) parent.mkdirs();
		
		try {

			// Establish the connection to the original CacheURIImpl data source
		    InputStream inputStream = null;
		    
		    if (originalURI.getScheme().equals("file")) {
	            String path = originalURI.getPath();
                File f = new File(path);
	            inputStream = new FileInputStream(f);
		    } else {
                inputStream =  originalURI.toURL().openConnection().getInputStream();
		    }
		    
		    BufferedInputStream bis = new BufferedInputStream(inputStream);
		    
		    // Establish the connection to the destination file
		    FileOutputStream fos = new FileOutputStream(cacheFile);
		    BufferedOutputStream bos = new BufferedOutputStream(fos);
	
		    // Write the source file to the destination file
		    int bite = bis.read();
		    while (bite != -1) {
				bos.write(bite);
				bite = bis.read();
		    }
	
		    // Clean up the reader and writer
		    bos.flush();
		    bis.close();
		    bos.close();

		} catch (java.net.NoRouteToHostException e) {
		    logger.debug(e.getMessage());
		} catch (FileNotFoundException e) {
            logger.debug(e.getMessage());
		} catch (IOException e) {
            logger.debug(e.getMessage());
		}
    }
    
    /**
     * Copy the original resource into the local cache.
     * @param originalURI the URI of the resource to be copied into the cache.
     * @param xml The XML to store in the cache at the given URI.
     * @throws XBRLException if the resource cannot be copied into the local cache.
     */
    public void copyToCache(URI originalURI, String xml) throws XBRLException {
    	
    	logger.debug("Attempting to cache a string XML document using : " + originalURI);

    	File cacheFile = this.getCacheFile(originalURI);
    	
    	logger.debug("The cache file is : " + cacheFile.toString());
    	
    	// If necessary, create the directory to contain the cached resource
		File parent = cacheFile.getParentFile();
		if (parent != null) parent.mkdirs();

		try {
	        FileWriter out = new FileWriter(cacheFile);
	        out.write(xml);
	        out.close();		
		} catch (IOException e) {
			 throw new XBRLException("The String resource could not be cached.",e);
		}
    }    
    

    

    
    /**
     * Delete a resource from the cache.
     * @param uri The original or the cache URI.
     * @throws XBRLException if the cache file cannot be determined.
     */
    public void purge(URI uri) throws XBRLException {
		File file = this.getCacheFile(uri);
		file.delete();
        logger.debug("Purged " + file);
    }
    
}