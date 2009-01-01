package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;

/**
 * Tests the implementation of the org.xbrlapi.Scenario interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ScenarioTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.scenarios";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ScenarioTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * TODO Implement specific scenario fragment tests.
	 */
	public void testGetComplexContent() {

/*		try {
			OpenContextComponent fragment = (OpenContextComponent) store.getFragment("6");
			assertEquals(4, fragment.getComplexContent().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
*/	}
	
}
