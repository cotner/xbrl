package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;

/**
 * Tests the implementation of the org.xbrlapi.Title interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class TitleTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.multi.concept.schema";
	private final String STARTING_POINT_B = "test.data.extended.link.documentation.element";
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
		loader.discover(this.getURL(STARTING_POINT_B));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public TitleTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting the documentation fragments in the extended link.
	 */
	public void testGetDocumentations() {
		// TODO Create a test document to test XLink title elements.
	}
		
}
