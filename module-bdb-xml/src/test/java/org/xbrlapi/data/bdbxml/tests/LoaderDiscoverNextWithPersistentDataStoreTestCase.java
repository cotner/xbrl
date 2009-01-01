package org.xbrlapi.data.bdbxml.tests;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * Test the loader discoverNext implementation using the 
 * BDB XML database as the persistent data store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LoaderDiscoverNextWithPersistentDataStoreTestCase extends BaseTestCase {
	
	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.stashURI(new URI(this.getURI(STARTING_POINT)));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public LoaderDiscoverNextWithPersistentDataStoreTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test discovery one URI at a time
	 */
	public void testDiscoverNext() {
		try {
			loader.discoverNext();
			while (! loader.getDocumentsStillToAnalyse().isEmpty()) {
				for (String document: loader.getDocumentsStillToAnalyse()) {
					logger.info("still to process " + document);
				}
				loader.discoverNext();
			}
		} catch (XBRLException e) {
			fail("Unexpected " + e.getMessage());
		}
	}

}
