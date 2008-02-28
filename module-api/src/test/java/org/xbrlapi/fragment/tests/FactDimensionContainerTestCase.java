package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FactDimensionContainer;

/**
 * Tests the implementation of the org.xbrlapi.FactDimensionContainer interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FactDimensionContainerTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public FactDimensionContainerTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the id of a context.
	 */
	public void testGetContextId() {

		try {
			FactDimensionContainer container = (FactDimensionContainer) store.getFragment("16");
			assertEquals("ci", container.getId());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

	/**
	 * Test getting the id of a unit.
	 */
	public void testGetUnitId() {

		try {
			FactDimensionContainer container = (FactDimensionContainer) store.getFragment("14");
			assertEquals("u1", container.getId());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
