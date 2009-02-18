package org.xbrlapi.data.xindice.tests;

import org.xbrlapi.data.xindice.DBConnection;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.xindice.DBConnectionImpl;
import org.xbrlapi.data.xindice.StoreImpl;
import org.xbrlapi.utilities.XBRLException;
/**
 * Test the Xindice XBRLAPI Store implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */
public class StoreImplConstructorTestCase extends BaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		connection = createConnection();
		assertNotNull("The database connection was not established properly.", connection);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public StoreImplConstructorTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test that a new store that uses existing collections for 
	 * data and metadata instead of creating them.
	 */
	public void testStoreImplConnectsToAnExistingStore() {
		
		try {
			DBConnection newConnection = createConnection();
			connections.add(newConnection);
			Store newStore = createStore(connection);
			stores.add(newStore);
            assertEquals("Fragment counts do not match.",store.getSize(),newStore.getSize());
		} catch (XBRLException e) {
			e.printStackTrace();
			fail("The connection to an existing store failed to be created. " + e.getMessage());
		}
	}

	/**
	 * Test store creation using a nonexistent parent collection to hold the store collection.
	 */
	public void testStoreImplFailsOnNonexistentContainer() {
		try {
			new StoreImpl((DBConnectionImpl) connection,"/nonexistentStoreParent/", dataCollectionName);
			fail("The store should have failed to be created because no parent exists for the store collection.");
		} catch (XBRLException expected) {
			;
		}
	}

	/**
	 * Test a store that has an illegal name.
	 */
	public void testStoreImplFailsWithBadCharacters() {
		try {
			String badName = "&&<>";
			store = new StoreImpl((DBConnectionImpl) connection, storeParentPath, badName);
			fail("The store was created despite the dud name of the data collection.");
			connection.deleteCollection(storeParentPath + badName);
		} catch (XBRLException e) {
			// Exception thrown as expected.
			logger.info("Generated the error: " + e.getMessage());
		}		
	}
	
	
}
