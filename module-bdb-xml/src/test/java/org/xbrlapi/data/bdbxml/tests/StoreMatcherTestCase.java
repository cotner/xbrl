package org.xbrlapi.data.bdbxml.tests;

import java.net.URL;

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
	
	public void testURLStashingWithMatcher() {
		try {
	        loader.discover(this.getURL(ORIGINAL));
	        loader.discover(this.getURL(DUPLICATE));       
		    
            URL url1 = new URL(this.getURL(ORIGINAL));
	        URL url2 = new URL(this.getURL(DUPLICATE));
	        
	        assertEquals(url1,store.getMatcher().getMatch(url2));
	        
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
