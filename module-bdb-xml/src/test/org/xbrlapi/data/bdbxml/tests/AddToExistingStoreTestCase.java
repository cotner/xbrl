package org.xbrlapi.data.bdbxml.tests;

import org.xbrlapi.XBRLException;
import org.xbrlapi.data.Store;
import org.xbrlapi.loader.Loader;

/**
 * Test the ability to reconnect to a data store. 
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
	 * Test reconnection to an existing store.
	 */
	public void testReconnectionToAnExistingStore() {

		try {
			
			Store secondStore = createStore();
			
			Loader secondLoader = createLoader(secondStore);

			assertEquals(6,secondStore.getStoredURLs().size());
			
			secondLoader.discover(this.getURL("test.data.small.instance"));

			assertEquals(8,secondStore.getStoredURLs().size());
			
			secondStore.close();
			
		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

}
