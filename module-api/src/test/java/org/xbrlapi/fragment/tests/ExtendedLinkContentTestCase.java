package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.ExtendedLinkContent;
/**
 * Tests the implementation of the org.xbrlapi.ExtendedLinkContent interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ExtendedLinkContentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.custom.link.role";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ExtendedLinkContentTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting the containing extended link.
	 */
	public void testGetExtendedLink() {

		try {
			ExtendedLinkContent fragment = (ExtendedLinkContent) store.getFragment("5");
			assertEquals("presentationLink", fragment.getExtendedLink().getLocalname());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
