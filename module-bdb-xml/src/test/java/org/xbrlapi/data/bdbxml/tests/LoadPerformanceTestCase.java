package org.xbrlapi.data.bdbxml.tests;

import java.io.File;
import java.util.List;
/**
 * Tests of performance with larger data sets.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */
public abstract class LoadPerformanceTestCase extends BaseTestCase {
	private final String STARTING_POINT = "real.data.large.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(getURL(STARTING_POINT));		
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
			
			List<String> urls = store.getStoredURLs();
			assertEquals(23,urls.size());
			
			File databaseFile = new File(new File(location),containerName);
			if (databaseFile.exists()) {
				logger.info("The final database contains " + databaseFile.length() + " bytes.");
			} else {
				logger.info("The final database does not exist.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
