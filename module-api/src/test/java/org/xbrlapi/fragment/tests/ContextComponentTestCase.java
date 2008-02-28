package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Entity;

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
			Entity fragment = (Entity) store.getFragment("17");
			assertEquals("org.xbrlapi.impl.ContextImpl", fragment.getContext().getType());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
