package org.xbrlapi.fragment.tests;

import org.xbrlapi.ArcroleType;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;

/**
 * Tests the implementation of the org.xbrlapi.CustomType interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ArcroleTypeTestCase extends DOMLoadingTestCase {

	private final String STARTING_POINT = "test.data.custom.link.arcrole";

	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(getURL(STARTING_POINT));
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ArcroleTypeTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting the cyclesAllowed attribute value.
	 */
	public void testGetCustomArcRoleCyclesAllowed() {

		try {
			FragmentList<ArcroleType> fragments = store.<ArcroleType>getFragments("ArcroleType");
			ArcroleType fragment = fragments.getFragment(0); 
			assertEquals("none", fragment.getCyclesAllowed());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
