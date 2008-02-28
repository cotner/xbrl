package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FootnoteArc;

/**
 * Tests the implementation of the org.xbrlapi.FootnoteArc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FootnoteArcTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.footnote.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public FootnoteArcTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting show value.
	 */
	public void testGetShow() {	

		try {
			FootnoteArc arc = (FootnoteArc) store.getFragment("8");
			assertEquals("replace", arc.getShow());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting actuate value.
	 */
	public void testGetActuate() {	

		try {
			FootnoteArc arc = (FootnoteArc) store.getFragment("8");
			assertEquals("onRequest", arc.getActuate());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
