package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
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
		loader.discover(this.getURI(STARTING_POINT));		
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
	        FragmentList<UsedOn> fragments = store.<UsedOn>getFragments("UsedOn");
	        assertTrue(fragments.getLength() > 0);
	        for (UsedOn fragment: fragments) {
	            if (fragment.getParent().getDataRootElement().getAttribute("id").equals("newExtendedRoleType")) {
                    assertEquals("http://www.xbrl.org/2003/linkbase",fragment.getURI());
                    assertEquals("presentationLink", fragment.getLocalname());
                    assertTrue(fragment.isUsedOn("http://www.xbrl.org/2003/linkbase", "presentationLink"));
	            }
	        }
	    } catch (Exception e) {
	        fail(e.getMessage());
	    }
	}
}
