package org.xbrlapi.data.bdbxml.tests;

import java.util.List;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.impl.MockFragmentImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

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
			fail("The addition of a document fragment to the data store failed." + e.getMessage());
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
	        String xpathQuery = "/" + Constants.XBRLAPIPrefix + ":" + "fragment";
	        FragmentList<Fragment> fragments = store.<Fragment>query(xpathQuery);
			assertTrue(fragments.getLength() > 1);
	        Fragment fragment = fragments.getFragment(0);
	        assertEquals("fragment",fragment.getMetadataRootElement().getLocalName());
		} catch (XBRLException e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testFilteredQuery() throws Exception {
        
        List<String> urls = store.getStoredURLs();
        assertTrue(urls.size() > 1);
        urls.remove(0);
        try {
            String xpathQuery = "/*[" + Constants.XBRLAPIPrefix + ":data]";
            FragmentList<Fragment> filteredFragments = store.<Fragment>query(xpathQuery,urls);
            FragmentList<Fragment> fragments = store.<Fragment>query(xpathQuery);
            assertTrue(fragments.getLength() > filteredFragments.getLength());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }	

	public void testQueryLoadedFragments() {
		
		try {
	        String query = "/" + Constants.XBRLAPIPrefix + ":" + "fragment[" + Constants.XBRLAPIPrefix + ":" + "data/" + Constants.XMLSchemaPrefix + ":element]";
	        FragmentList<Fragment> fragments = store.<Fragment>query(query);
	        Fragment fragment = fragments.getFragment(0);
	        assertEquals("element",fragment.getDataRootElement().getLocalName());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}



	public void testHasDocument() {
		try {
		    List<String> urls = store.getStoredURLs();
		    assertTrue(urls.size() > 0);
		    for (String url: urls) {
	            assertTrue(store.hasDocument(url));
		    }
			assertFalse(store.hasDocument("http://www.rubbish.gcs/crazy.xyz"));
		} catch (XBRLException e) {
			fail("Unexpected " + e.getMessage());
		}
	}	
}
