package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.XlinkDocumentation;

/**
 * Tests the implementation of the org.xbrlapi.XlinkDocumentation interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XlinkDocumentationTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.extended.link.documentation.element";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public XlinkDocumentationTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting the roleType definition.
	 */
	public void testGetValue() {

		try {
			List<XlinkDocumentation> fragments = store.<XlinkDocumentation>gets("XlinkDocumentation");
			XlinkDocumentation fragment = fragments.get(0);
			assertEquals("Value of the documentation", fragment.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
