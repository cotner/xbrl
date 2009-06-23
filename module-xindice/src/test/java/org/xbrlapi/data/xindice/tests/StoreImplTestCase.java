package org.xbrlapi.data.xindice.tests;

import java.util.Set;

import org.xbrlapi.Fragment;
import java.util.List;
import org.xbrlapi.data.xindice.StoreImpl;
import org.xbrlapi.impl.MockImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Test the Xindice store implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public class StoreImplTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public StoreImplTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the addition of a mock fragment to the store.
	 */
	public void testAddFragment() {
		try {
			String index = "1";
			MockImpl d = new MockImpl(index);
			store.persist(d);
			assertEquals(index,store.getFragment(index).getIndex());
		} catch (XBRLException e) {
			fail("The addition of a document fragment to the Xindice data store failed." + e.getMessage());
		}
	}

	/**
	 * Test the removal of a fragment from the store.
	 */
	public void testRemoveFragmentUsingIndex() {
		try {
			String index = "1";
			store.persist(new MockImpl(index));
			assertTrue(store.hasXML(index));
			store.remove(index);
			assertFalse(store.hasXML(index));
		} catch (XBRLException e) {
			fail("Unexpected exception. " + e.getMessage());
		}
	}




	
	public void testQueryData() {
		try {
	        String xpathQuery = "/" + Constants.XBRLAPIPrefix + ":" + "fragment";
	        List<Fragment> fragments = store.<Fragment>queryForFragments(xpathQuery);
			assertTrue(fragments.size() > 1);
	        Fragment fragment = fragments.get(0);
	        assertEquals("fragment",fragment.getMetadataRootElement().getLocalName());
		} catch (XBRLException e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testQueryForIndices() {
        try {
            String xpathQuery = "/" + Constants.XBRLAPIPrefix + ":" + "fragment";
            Set<String> indices = store.queryForIndices(xpathQuery);
            assertTrue(! indices.isEmpty());
            for (String index: indices) {
                logger.info(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
	
	public void testQueryLoadedFragments() {
		try {
			loader.discover();
		} catch (XBRLException e) {
			fail(e.getMessage());
		} 
		
		try {
            String query = "#roots#[*/" + Constants.XMLSchemaPrefix + ":element]";
	        List<Fragment> fragments = store.<Fragment>queryForFragments(query);
	        Fragment fragment = fragments.get(0);
	        assertEquals("element",fragment.getDataRootElement().getLocalName());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	
	
	public void testAddIndex() {
		try {
			((StoreImpl) store).addIndex("test","name","document");
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception. " + e.getMessage());
		}
	}
	
	public void testDeleteIndex() {
		try {
			((StoreImpl) store).addIndex("test","name","document");
			((StoreImpl) store).deleteIndex("test");
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception. " + e.getMessage());
		}
	}

	
	




}
