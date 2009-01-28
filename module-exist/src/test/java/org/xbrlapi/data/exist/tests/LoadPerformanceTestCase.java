package org.xbrlapi.data.exist.tests;

import java.net.URI;
import java.util.List;
/**
 * Tests of performance with larger data sets.
 * TODO Create a load test for other persistent data store implementations.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */
public abstract class LoadPerformanceTestCase extends BaseTestCase {
	private final String STARTING_POINT = "real.data.large.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public LoadPerformanceTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test creation of an reconnection to a large data store.
	 */
	public void testLargerStore() {
		try {

			List<URI> uris = store.getStoredURIs();
			assertTrue(uris.size() > 22);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
