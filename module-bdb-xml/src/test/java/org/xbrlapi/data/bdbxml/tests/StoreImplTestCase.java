package org.xbrlapi.data.bdbxml.tests;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.impl.MockImpl;
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
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public StoreImplTestCase(String arg0) {
		super(arg0);
	}

	public void testAddFragment() {
		try {
			String index = "1";
			MockImpl d = new MockImpl(index);
			store.persist(d);
			assertEquals(index,store.getFragment(index).getIndex());	
			
		} catch (XBRLException e) {
			e.printStackTrace();
			fail("The addition of a document fragment to the data store failed." + e.getMessage());
		}
	}

	public void testRemoveFragmentUsingIndex() {
		try {
			String index = "1";
			store.persist(new MockImpl(index));
			assertTrue(store.hasFragment(index));
			store.remove(index);
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
	
    public void testQueryForIndices() {
        try {
            String xpathQuery = "/" + Constants.XBRLAPIPrefix + ":" + "fragment";
            Map<String,String> indices = store.queryForIndices(xpathQuery);
            assertTrue(! indices.isEmpty());
            for (String index: indices.keySet()) {
                logger.info(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
		    List<URI> uris = store.getStoredURIs();
		    assertTrue(uris.size() > 0);
		    for (URI uri: uris) {
	            assertTrue(store.hasDocument(uri));
		    }
			assertFalse(store.hasDocument(new URI("http://www.rubbish.gcs/crazy.xyz")));
		} catch (Exception e) {
			fail("Unexpected " + e.getMessage());
		}
	}	
}
