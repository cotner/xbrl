package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Resource;
import org.xbrlapi.utilities.Constants;

/**
 * Tests the implementation of the org.xbrlapi.Resource interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ResourceTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.label.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ResourceTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting resource role value.
	 */
	public void testGetResourceRole() {	

		try {
			FragmentList<Resource> fragments = store.<Resource>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='resource']");
			Resource fragment = fragments.getFragment(0);
			assertEquals("http://www.xbrl.org/2003/role/label", fragment.getResourceRole());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting resource title attribute value.
	 */
	public void testGetTitleAttribute() {	

		try {
			FragmentList<Resource> fragments = store.<Resource>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='resource']");
			Resource fragment = fragments.getFragment(0);
			assertEquals("label_CurrentAsset", fragment.getTitleAttribute());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

}
