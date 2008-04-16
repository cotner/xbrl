package org.xbrlapi.data.exist.tests;

import org.xbrlapi.data.Store;
import org.xbrlapi.data.exist.DBConnectionImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;
/**
 * Test the Exist XBRLAPI Store implementation store persistence.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class AddToExistingStoreTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public AddToExistingStoreTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the retrieval of a list of URLs from the data store.
	 */
	public void testReconnectionToAnExistingStore() {

		try {
			
			DBConnectionImpl secondConnection = createConnection();
			connections.add(secondConnection);

			Store secondStore = createStore(secondConnection);
			stores.add(secondStore);

			Loader secondLoader = createLoader(secondStore);

			assertTrue(secondStore.getStoredURLs().size() >= 12);

			secondLoader.discover(this.getURL("test.data.small.instance"));

			assertTrue(secondStore.getStoredURLs().size() >= 14);

		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
		
}
