package org.xbrlapi.fragment.tests;

import org.xbrlapi.Context;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;

/**
 * Tests the implementation of the org.xbrlapi.Context interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ContextTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ContextTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting entity.
	 */
	public void testGetEntity() {

		try {
			Context fragment = (Context) store.getFragment("16");
			assertEquals("org.xbrlapi.impl.EntityImpl", fragment.getEntity().getType());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting period.
	 */
	public void testGetPeriod() {

		try {
			Context fragment = (Context) store.getFragment("16");
			assertEquals("org.xbrlapi.impl.PeriodImpl", fragment.getPeriod().getType());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting scenario.
	 */
	public void testGetScenario() {

		try {
			Context fragment = (Context) store.getFragment("16");
			assertNull(fragment.getScenario());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test c-equality assessment.
	 */
	public void testCEquality() {

		try {
			FragmentList<Context> fragments = store.getFragments("Context");
			Context fragment = fragments.getFragment(0); 
			Context other = fragments.getFragment(0);
			assertTrue(fragment.equals(other));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
