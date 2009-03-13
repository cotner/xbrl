package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.ArcroleType;
import org.xbrlapi.DOMLoadingTestCase;

/**
 * Tests the implementation of the org.xbrlapi.CustomType interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ArcroleTypeTestCase extends DOMLoadingTestCase {

	private final String STARTING_POINT = "test.data.custom.link.arcrole";

	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(getURI(STARTING_POINT));
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
			List<ArcroleType> fragments = store.<ArcroleType>getFragments("ArcroleType");
			ArcroleType fragment = fragments.get(0); 
			assertEquals("none", fragment.getCyclesAllowed());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
