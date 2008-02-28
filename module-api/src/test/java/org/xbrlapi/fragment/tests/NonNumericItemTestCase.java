package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.NonNumericItem;

/**
 * Tests the implementation of the org.xbrlapi.NonNumericItem interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NonNumericItemTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public NonNumericItemTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting all child facts.
	 */
	public void testGetAllChildFacts() {

		try {
			NonNumericItem fact = (NonNumericItem) store.getFragment("10");
			assertEquals("My Family Name2", fact.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
