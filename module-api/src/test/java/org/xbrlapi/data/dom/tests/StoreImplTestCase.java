package org.xbrlapi.data.dom.tests;

import java.net.URI;
import java.util.Set;

import org.xbrlapi.Fragment;
import java.util.List;
import org.xbrlapi.Mock;
import org.xbrlapi.impl.MockImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Test the XML DOM XBRLAPI Store implementation.
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
			String index = loader.getNextFragmentId();
			MockImpl d = null;
			d = new MockImpl(index);
			store.persist(d);
			Fragment f = null;
			f = store.getFragment(index);
			assertNotNull(f);
			assertEquals(index,f.getIndex());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}

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

	public void testRemoveFragmentUsingFragment() {
		try {
			String index = "1";
			store.persist(new MockImpl(index));
			assertTrue(store.hasXML(index));
			MockImpl document = (MockImpl) store.getFragment(index);
			assertNotNull(document);
			store.remove(index);
			assertFalse(store.hasXML(index));
		} catch (XBRLException e) {
			fail("Unexpected exception. " + e.getMessage());
		}
	}
	
	public void testQueryData() throws Exception {
	    Mock fragment = new MockImpl("WooHoo");
	    fragment.appendDataElement(Constants.XBRLAPINamespace,"info",Constants.XBRLAPIPrefix + ":info");
	    store.persist(fragment);
		String index = "1";
		store.serialize(fragment);
		List<Fragment> fragments = null;
		try {
	        String query = "#roots#[@type='org.xbrlapi.impl.MockImpl' and */xbrlapi:info]";
	        logger.info(query);
	        fragments = store.<Fragment>queryForFragments(query);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			assertEquals("1",(new Integer(fragments.size())).toString());
	        assertEquals("info",fragments.get(0).getDataRootElement().getLocalName());
			store.remove(index);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
    public void testQueryForIndices() {
        try {
            String xpathQuery = "#roots#";
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
	
    public void testGetFragments() {
        try {
            List<Fragment> fragments = store.<Fragment>getFragments("Schema");
            List<Fragment> sameFragments = store.<Fragment>getFragments("org.xbrlapi.impl.SchemaImpl");
            assertTrue(sameFragments.size() > 0);
            assertTrue(fragments.size() == sameFragments.size());
        } catch (XBRLException e) {
            e.printStackTrace();
            fail("Unexpected " + e.getMessage());
        }
    }	

	public void testHasDocument() {
		try {
			assertTrue(store.hasDocument(getURI(STARTING_POINT)));
			assertFalse(store.hasDocument(new URI("http://www.rubbish.gcs/crazy.xyz")));
		} catch (Exception e) {
			fail("Unexpected " + e.getMessage());
		}
	}
}
