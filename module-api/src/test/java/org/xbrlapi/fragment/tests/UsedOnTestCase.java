package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.UsedOn;

/**
 * Tests the implementation of the org.xbrlapi.UsedOn interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class UsedOnTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.custom.link.role";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public UsedOnTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test analysis of the content of a UsedOn fragment.
	 */
	public void testUsedOnContentAnalysis() {

		try {
			UsedOn fragment = (UsedOn) store.getFragment("9");
			assertEquals("presentationLink", fragment.getLocalname());
			assertEquals("http://www.xbrl.org/2003/linkbase", fragment.getURI());
			assertEquals(true, fragment.isUsedOn("http://www.xbrl.org/2003/linkbase", "presentationLink"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	
}
