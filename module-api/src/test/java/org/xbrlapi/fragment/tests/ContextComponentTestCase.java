package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Entity;
import org.xbrlapi.FragmentList;

/**
 * Tests the implementation of the org.xbrlapi.ContextComponent interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ContextComponentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ContextComponentTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting context.
	 */
	public void testGetContext() {

		try {
		    FragmentList<Entity> entities = store.getFragments("Entity");
		    assertTrue(entities.getLength() > 0);
	        assertEquals("org.xbrlapi.impl.ContextImpl", entities.get(0).getContext().getType());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
