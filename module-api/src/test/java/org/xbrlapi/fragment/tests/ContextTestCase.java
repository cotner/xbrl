package org.xbrlapi.fragment.tests;

import org.xbrlapi.Context;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;

/**
 * Tests the implementation of the org.xbrlapi.Context interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ContextTestCase extends DOMLoadingTestCase {
    
	private final String STARTING_POINT = "test.data.scenarios";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
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
		    FragmentList<Context> contexts = store.<Context>getFragments("Context");
		    assertTrue(contexts.getLength() > 0);
		    for (Context context: contexts) {
	            assertEquals("org.xbrlapi.impl.EntityImpl", context.getEntity().getType());
		    }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting period.
	 */
	public void testGetPeriod() {

		try {
            FragmentList<Context> contexts = store.<Context>getFragments("Context");
            assertTrue(contexts.getLength() > 0);
            for (Context context: contexts) {
                assertEquals("org.xbrlapi.impl.PeriodImpl", context.getPeriod().getType());
            }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting scenario.
	 */
	public void testGetScenario() {

		try {
            FragmentList<Context> contexts = store.<Context>getFragments("Context");
            assertTrue(contexts.getLength() > 0);
            int scenarios = 0;
            for (Context context: contexts) {
                store.serialize(context);
                FragmentList<Fragment> children = context.getAllChildren();
                for (Fragment child: children) {
                    store.serialize(child);
                }
                if (context.getScenario() != null) {
                    assertEquals("org.xbrlapi.impl.ScenarioImpl", context.getScenario().getType());                    
                    scenarios++;
                }
            }
            assertTrue(scenarios > 0);
		} catch (Exception e) {
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
