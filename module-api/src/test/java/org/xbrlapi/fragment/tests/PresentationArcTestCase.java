package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.PresentationArc;

/**
 * Tests the implementation of the org.xbrlapi.PresentationArc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class PresentationArcTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.presentation.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public PresentationArcTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting preferred label role value.
	 */
	public void testGetPreferredLabelRole() {	

		try {
			FragmentList<PresentationArc> fragments = store.<PresentationArc>getFragments("PresentationArc");
			PresentationArc arc = fragments.getFragment(0);
			assertEquals("http://www.xbrl.org/2003/role/verboseLabel",arc.getPreferredLabel());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting preferred label role type.
	 */
	public void testGetPreferredLabelRoleType() {	

		try {
			FragmentList<PresentationArc> fragments = store.<PresentationArc>getFragments("PresentationArc");
			PresentationArc arc = fragments.getFragment(0);
			assertNull(arc.getPreferredLabelRoleType());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
