package org.xbrlapi.cache.tests;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */

import java.io.File;
import java.net.URI;

import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.utilities.BaseTestCase;


public class CacheImplTestCase extends BaseTestCase {

	private String cacheRoot;

	
	protected void setUp() throws Exception {
		super.setUp();
		cacheRoot = configuration.getProperty("local.cache");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * @param arg0
	 */
	public CacheImplTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test operations on a simple URI
	 */
	public final void testHTTP_URI() {
		try {
			this.examineURI(new URI("http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"));
		} catch (Exception e) {
			fail("Unexpected exception. " + e.getMessage());
		}
	}
	
	

	public final void examineURI(URI originalURI) {
		try {

            logger.info("Original URI: " + originalURI);
			CacheImpl cache = new CacheImpl(new File(cacheRoot));
			assertFalse(cache.isCacheURI(originalURI));
			File cacheFile = cache.getCacheFile(originalURI);
			assertNotNull(cacheFile);
			URI cacheURI = cache.getCacheURI(originalURI);
			logger.info("Cache URI: " + cacheURI);
			assertTrue(cache.isCacheURI(cacheURI));
			URI newURI = cache.getOriginalURI(cacheURI);
            logger.info("New Original URI: " + newURI);
			assertEquals(originalURI, newURI);
			
		} catch (Exception e) {
		    e.printStackTrace();
			fail("Unexpected exception was thrown. " + e.getMessage());
		}
	}

    /**
     * Test operations on a simple URI
     */
    public final void testFile_URI() {
    	try {
    		this.examineURI(new URI("file:///home/geoff/document.xml"));
    	} catch (Exception e) {
    		fail("Unexpected exception. " + e.getMessage());
    	}
    }	
	
}
