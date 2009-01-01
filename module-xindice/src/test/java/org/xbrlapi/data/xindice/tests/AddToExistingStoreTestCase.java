package org.xbrlapi.data.xindice.tests;

import org.xbrlapi.data.Store;
import org.xbrlapi.data.xindice.DBConnectionImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

/**
 * Test the ability to reconnect to a Xindice data store. 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
*/
public class AddToExistingStoreTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
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
			
			DBConnectionImpl secondConnection = createConnection();
			connections.add(secondConnection);
			
			Store secondStore = createStore(secondConnection);
			stores.add(secondStore);
			
			Loader secondLoader = createLoader(secondStore);

            int original = secondStore.getStoredURIs().size();
			assertTrue(original > 1);			
			
			secondLoader.discover(this.getURI("test.data.small.instance"));

			assertTrue(secondStore.getStoredURIs().size() > original);

		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

}
