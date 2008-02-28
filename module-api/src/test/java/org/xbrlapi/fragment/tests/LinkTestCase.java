package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Link;

/**
 * Tests the implementation of the org.xbrlapi.Link interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LinkTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.custom.link.role";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public LinkTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting link role value when it is missing.
	 */
	public void testGetLinkRoleWhenItIsMissing() {	

		try {
			Link link = (Link) store.getFragment("2");
			assertNull(link.getLinkRole());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting link role value when it is provided.
	 */
	public void testGetLinkRoleWhenItProvided() {	

		try {
			Link link = (Link) store.getFragment("10");
			assertEquals("http://www.xbrl.org/2003/role/presentationLinkbaseRef",link.getLinkRole());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting link role value for a custom link.
	 */
	public void testGetLinkRoleForCustomLink() {	

		try {
			Link link = (Link) store.getFragment("67");
			assertNull(link.getLinkRole());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}		
	
}
