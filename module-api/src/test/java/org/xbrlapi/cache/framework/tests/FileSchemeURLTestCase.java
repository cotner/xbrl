package org.xbrlapi.cache.framework.tests;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.xbrlapi.utilities.BaseTestCase;


public class FileSchemeURLTestCase extends BaseTestCase {

	private String cacheRoot;
	
	protected void setUp() throws Exception {
		super.setUp();
		cacheRoot = configuration.getProperty("local.cache");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public FileSchemeURLTestCase(String arg0) {
		super(arg0);
	}

	public final void testURIWithAuthority() {
		try {
		    
		    String urlString = "file://localhost/home/geoff/rr-instance.xml";
            
		    assertEquals("/",File.separator);
		    
		    URI uri = new URI(urlString);
            assertEquals("file",uri.getScheme());
            assertEquals("localhost",uri.getAuthority());
            assertEquals("/home/geoff/rr-instance.xml",uri.getPath());
            assertEquals(-1,uri.getPort());

            URL url = new URL(urlString);
            assertEquals("file",url.getProtocol());
            assertEquals("localhost",url.getAuthority());
            assertEquals("/home/geoff/rr-instance.xml",url.getPath());
            assertEquals(-1,uri.getPort());
		    
		} catch (Exception e) {
		    e.printStackTrace();
			fail("Unexpected exception. " + e.getMessage());
		}
	}
	
    public final void testURIWithoutAuthority() {
        try {
            
            String urlString = "file:///home/geoff/rr-instance.xml";
            
            assertEquals("/",File.separator);
            
            URI uri = new URI(urlString);
            logger.info(uri);
            assertEquals("file",uri.getScheme());
            assertNull(uri.getAuthority());
            assertEquals("/home/geoff/rr-instance.xml",uri.getPath());
            assertEquals(-1,uri.getPort());

            URL url = new URL(urlString);
            logger.info(url);
            assertEquals("file",url.getProtocol());
            assertEquals("",url.getAuthority());
            assertEquals("/home/geoff/rr-instance.xml",url.getPath());
            assertEquals(-1,uri.getPort());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception. " + e.getMessage());
        }
    }	
	
}
