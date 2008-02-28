package org.xbrlapi.fragment.tests;

import org.xbrlapi.CalculationArc;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;

/**
 * Tests the implementation of the org.xbrlapi.CalculationArc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class CalculationArcTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.multi.concept.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public CalculationArcTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the weight value.
	 */
	public void testGetWeight() {	

		try {
			FragmentList<CalculationArc> fragments = store.<CalculationArc>getFragments("CalculationArc");
			CalculationArc arc = fragments.getFragment(0);
			assertEquals(1.0, arc.getWeight(),0.0);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

}
