package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.Arc;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.utilities.Constants;
/**
 * Tests the implementation of the org.xbrlapi.ExtendedLinkContent interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ExtendedLinkContentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.custom.link.role";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
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
		    List<Arc> arcs = store.<Arc>getFragments("Arc");
		    assertTrue(arcs.size() > 0);
		    for (Arc arc: arcs) {
	            assertEquals("extended", arc.getExtendedLink().getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"type"));
		    }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
