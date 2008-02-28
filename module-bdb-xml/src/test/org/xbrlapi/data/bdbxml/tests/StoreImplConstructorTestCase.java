package org.xbrlapi.data.bdbxml.tests;

import org.xbrlapi.XBRLException;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.bdbxml.StoreImpl;
import org.xbrlapi.impl.MockFragmentImpl;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public class StoreImplConstructorTestCase extends BaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public StoreImplConstructorTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the creation of a data store.
	 */
	public void testStoreImplConstructor() {
		assertNotNull(store);
	}

	/**
	 * Test the a new store that uses existing collections for 
	 * data and metadata instead of creating them.
	 */
	public void testStoreImplConnectsToAnExistingStore() {

		try {
			store.storeFragment(new MockFragmentImpl("1"));
			store.storeFragment(new MockFragmentImpl("2"));
		} catch (XBRLException e) {
			fail("The addition of fragments to the original store failed.");
		}
		
		try {
			Store newStore = this.createStore();
			assertEquals(store.getNextFragmentId(),newStore.getNextFragmentId());
			newStore.close();
		} catch (XBRLException e) {
			e.printStackTrace();
			fail("The connection to an existing store failed to be created.");
		}
		
	}
	
	/**
	 * Test a store that has odd characters in its container name.
	 */
	public void testStoreImplCopesWithPeculiarCharacters() {
		try {
			String badName = "&& <>";
			Store newStore = createStore(location, badName);
			((StoreImpl) newStore).delete();
		} catch (XBRLException e) {
			e.printStackTrace();
			fail("The store was not created.");
		}		
	}	
	
}
