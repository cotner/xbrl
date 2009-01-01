package org.xbrlapi.data.bdbxml.tests;

import java.net.URI;

import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.data.resource.Matcher;

/**
 * Tests the Store based matcher using the BDB XML data store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class StoreMatcherTestCase extends BaseTestCase {
	private final String ORIGINAL = "real.data.sec.usgaap.1";
	private final String DUPLICATE = "real.data.sec.usgaap.2";
	
	protected void setUp() throws Exception {
		super.setUp();
		Matcher matcher = new InStoreMatcherImpl(store,loader.getCache());
		store.setMatcher(matcher);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public StoreMatcherTestCase(String arg0) {
		super(arg0);
	}
	
	public void testURIStashingWithMatcher() {
		try {
	        loader.discover(this.getURI(ORIGINAL));
	        loader.discover(this.getURI(DUPLICATE));       
		    
            URI uri1 = new URI(this.getURI(ORIGINAL));
	        URI uri2 = new URI(this.getURI(DUPLICATE));
	        
	        assertEquals(uri1,store.getMatcher().getMatch(uri2));
	        
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
