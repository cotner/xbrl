package org.xbrlapi.fragment.tests;

import java.net.URI;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.UsedOn;
import org.xbrlapi.utilities.Constants;

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
	        List<UsedOn> fragments = store.<UsedOn>getFragments("UsedOn");
	        assertTrue(fragments.size() > 0);
	        for (UsedOn fragment: fragments) {
	            if (fragment.getParent().getDataRootElement().getAttribute("id").equals("newExtendedRoleType")) {
                    assertEquals(Constants.XBRL21LinkNamespace,fragment.getUsedOnNamespace().toString());
                    assertEquals("presentationLink", fragment.getUsedOnLocalname());
                    store.serialize(fragment);
                    assertTrue(fragment.isUsedOn(new URI(Constants.XBRL21LinkNamespace), "presentationLink"));
	            }
	        }
	    } catch (Exception e) {
	        fail(e.getMessage());
	    }
	}
}
