package org.xbrlapi.fragment.tests;

import org.xbrlapi.Arc;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.utilities.Constants;

/**
 * Tests the implementation of the org.xbrlapi.Arc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ArcTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.multi.concept.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ArcTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the xlink:to attribute value.
	 */
	public void testGetTo() {	

		try {
			FragmentList<Arc> fragments = store.query("" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='arc']");
			Arc fragment = fragments.getFragment(0);
			assertEquals("contributingItem", fragment.getTo());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

	/**
	 * Test getting xlink from value.
	 */
	public void testGetFrom() {	

		try {
			FragmentList<Arc> fragments = store.<Arc>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='arc']");
			Arc fragment = fragments.getFragment(0);
			assertEquals("summationItem", fragment.getFrom());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting xlink show value.
	 */
	public void testGetShow() {	

		try {
			FragmentList<Arc> fragments = store.<Arc>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='arc']");
			Arc fragment = fragments.getFragment(0);
			assertNull(fragment.getShow());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}		
	
	/**
	 * Test getting xlink actuate value.
	 */
	public void testGetActuate() {	

		try {
			FragmentList<Arc> fragments = store.<Arc>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='arc']");
			Arc fragment = fragments.getFragment(0);
			assertNull(fragment.getActuate());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting xbrl order attribute value.
	 */
	public void testGetOrder() {	

		try {
			FragmentList<Arc> fragments = store.<Arc>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='arc']");
			Arc fragment = fragments.getFragment(0);
			assertEquals("1", fragment.getOrder());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting list of fragments that the arc runs from.
	 */
	public void testGetSourceFragments() {	

		try {
			FragmentList<Arc> fragments = store.<Arc>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='arc']");
			Arc fragment = fragments.getFragment(0);
			assertEquals(1, fragment.getSourceFragments().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting list of fragments that the arc runs to.
	 */
	public void testGetTargetFragments() {	

		try {
			FragmentList<Arc> fragments = store.<Arc>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='arc']");
			Arc fragment = fragments.getFragment(0);
			assertEquals(2, fragment.getTargetFragments().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
