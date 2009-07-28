package org.xbrlapi.data.exist.tests;

import org.xbrlapi.data.Store;
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
		logger.info(this.getURI(STARTING_POINT));
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public AddToExistingStoreTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the retrieval of a list of URIs from the data store.
	 */
	public void testReconnectionToAnExistingStore() {

		try {
			
			Store secondStore = createStore();
			stores.add(secondStore);

			Loader secondLoader = createLoader(secondStore);

			int original = secondStore.getDocumentURIs().size(); 
			assertTrue(original >= 1);

			secondLoader.discover(this.getURI("test.data.small.instance"));

			assertTrue(secondStore.getDocumentURIs().size() > original);

		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
		
}
