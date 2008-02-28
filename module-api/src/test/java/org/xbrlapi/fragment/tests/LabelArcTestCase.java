package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelArc;
/**
 * Tests the implementation of the org.xbrlapi.LabelArc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LabelArcTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.label.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public LabelArcTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting from value.
	 */
	public void testGetFrom() {	

		try {
			FragmentList<LabelArc> fragments = store.<LabelArc>getFragments("LabelArc");
			LabelArc fragment = fragments.getFragment(0);
			assertEquals("label_CurrentAsset_1126188937093_0", fragment.getFrom());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
