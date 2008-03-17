package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Instance;

/**
 * Tests the implementation of the org.xbrlapi.Instance interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InstanceTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public InstanceTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting contexts.
	 */
	public void testGetContexts() {
	    try {
	        FragmentList<Instance> instances = store.<Instance>getFragments("Instance");
	        assertTrue(instances.getLength() > 0);
	        for (Instance instance: instances) {
	            assertEquals(1, instance.getContexts().getLength());    
	        }
	    } catch (Exception e) {
	        fail(e.getMessage());
	    }
	}
	
	/**
	 * Test getting a specific context.
	 */
	public void testGetSpecificContext() {
	    try {
	        FragmentList<Instance> instances = store.<Instance>getFragments("Instance");
	        assertTrue(instances.getLength() > 0);
	        for (Instance instance: instances) {
	            assertEquals("context", instance.getContext("ci").getLocalname());
	        }
	    } catch (Exception e) {
	        fail(e.getMessage());
	    }
	}
	
	/**
	 * Test getting units.
	 */
	public void testGetUnits() {
	    try {
	        FragmentList<Instance> instances = store.<Instance>getFragments("Instance");
	        assertTrue(instances.getLength() > 0);
	        for (Instance instance: instances) {
	            assertEquals(2, instance.getUnits().getLength());            }
	    } catch (Exception e) {
	        fail(e.getMessage());
	    }
	}
	
	/**
	 * Test getting a specific unit.
	 */
	public void testGetSpecificUnit() {
	    try {
	        FragmentList<Instance> instances = store.<Instance>getFragments("Instance");
	        assertTrue(instances.getLength() > 0);
	        for (Instance instance: instances) {
	            assertEquals("unit", instance.getUnit("u1").getLocalname());
	        }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
	
	/**
	 * Test getting schemaRefs.
	 */
	public void testGetSchemaRefs() {
        try {
            FragmentList<Instance> instances = store.<Instance>getFragments("Instance");
            assertTrue(instances.getLength() > 0);
            for (Instance instance: instances) {
                assertEquals(1, instance.getSchemaRefs().getLength());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test getting linkbaseRefs.
	 */
	public void testGetLinkbaseRefs() {
        try {
            FragmentList<Instance> instances = store.<Instance>getFragments("Instance");
            assertTrue(instances.getLength() > 0);
            for (Instance instance: instances) {
                assertEquals(0, instance.getLinkbaseRefs().getLength());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test getting facts.
	 */
	public void testGetFacts() {
        try {
            FragmentList<Instance> instances = store.<Instance>getFragments("Instance");
            assertTrue(instances.getLength() > 0);
            for (Instance instance: instances) {
                assertEquals(2, instance.getFacts().getLength());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test getting footnote links.
	 */
	public void testGetFootnoteLinks() {
        try {
            FragmentList<Instance> instances = store.<Instance>getFragments("Instance");
            assertTrue(instances.getLength() > 0);
            for (Instance instance: instances) {
                assertEquals(0, instance.getFootnoteLinks().getLength());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}	
		
}
