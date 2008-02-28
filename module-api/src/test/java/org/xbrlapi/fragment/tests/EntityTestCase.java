package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Entity;
/**
 * Tests the implementation of the org.xbrlapi.Entity interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class EntityTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.segments";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public EntityTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the scenario.
	 */
	public void testGetScenario() {

		try {
			Entity fragment = (Entity) store.getFragment("4");
			assertEquals("org.xbrlapi.impl.SegmentImpl", fragment.getSegment().getType());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the identifier information.
	 */
	public void testGetScheme() {

		try {
			Entity fragment = (Entity) store.getFragment("4");
			assertEquals("www.dnb.com", fragment.getIdentifierScheme());
			assertEquals("121064880", fragment.getIdentifierValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
