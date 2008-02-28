package org.xbrlapi.data.bdbxml.tests;

import org.xbrlapi.Constants;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.XBRLException;
import org.xbrlapi.impl.MockFragmentImpl;

/**
 * Test the BDB XML data store implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public class StoreImplTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public StoreImplTestCase(String arg0) {
		super(arg0);
	}

	public void testAddFragment() {
		try {
			String index = store.getNextFragmentId();
			MockFragmentImpl d = new MockFragmentImpl(index);
			store.storeFragment(d);
			assertEquals(index,store.getFragment(index).getFragmentIndex());
		} catch (XBRLException e) {
			e.printStackTrace();
			fail("The addition of a document fragment to the Xindice data store failed." + e.getMessage());
		}
	}

	public void testRemoveFragmentUsingIndex() {
		try {
			String index = store.getNextFragmentId();
			store.storeFragment(new MockFragmentImpl(index));
			assertTrue(store.hasFragment(index));
			store.removeFragment(index);
			assertFalse(store.hasFragment(index));
		} catch (XBRLException e) {
			fail("Unexpected exception. " + e.getMessage());
		}
	}
	
	public void testQueryData() {
		try {
			String index = store.getNextFragmentId();
			store.storeFragment(new MockFragmentImpl(index));
	        String xpathQuery = "/" + Constants.XBRLAPIPrefix + ":" + "fragment";
	        FragmentList fragments = store.query(xpathQuery);
			assertEquals("78",(new Long(fragments.getLength())).toString());
	        Fragment fragment = fragments.getFragment(0);
	        assertEquals("fragment",fragment.getMetadataRootElement().getLocalName());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}

	public void testQueryLoadedFragments() {
		
		try {
	        String query = "/" + Constants.XBRLAPIPrefix + ":" + "fragment[" + Constants.XBRLAPIPrefix + ":" + "data/" + Constants.XMLSchemaPrefix + ":element]";
	        FragmentList fragments = store.query(query);
	        Fragment fragment = fragments.getFragment(0);
	        assertEquals("element",fragment.getDataRootElement().getLocalName());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testGetNextFragmentId() {
		try {
			assertEquals("78",store.getNextFragmentId());
		} catch (XBRLException e) {
			fail("Unexpected " + e.getMessage());
		}
	}

}
