package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.ReferenceArc;

/**
 * Tests the implementation of the org.xbrlapi.ReferenceArc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ReferenceArcTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.reference.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ReferenceArcTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting resource role value.
	 */
	public void testGetFrom() {	

		try {
			FragmentList<ReferenceArc> fragments = store.<ReferenceArc>getFragments("ReferenceArc");
			ReferenceArc arc = fragments.getFragment(0); 
			assertEquals("aaa", arc.getFrom());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
