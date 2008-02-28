package org.xbrlapi.fragment.tests;


import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.OpenContextComponent;

/**
 * Tests the implementation of the org.xbrlapi.OpenContextComponent interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class OpenContextComponentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.scenarios";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public OpenContextComponentTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting complex content.
	 */
	public void testGetComplexContent() {

		try {
			OpenContextComponent fragment = (OpenContextComponent) store.getFragment("6");
			assertEquals(4, fragment.getComplexContent().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test c-equality.
	 */
	public void testGetCEquality() {

		try {
			OpenContextComponent fragment = (OpenContextComponent) store.getFragment("6");
			assertTrue(fragment.equals(fragment));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
}
