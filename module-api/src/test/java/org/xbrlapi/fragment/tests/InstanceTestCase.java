package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.DOMLoadingTestCase;
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
		loader.discover(this.getURI(STARTING_POINT));		
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
	        List<Instance> instances = store.<Instance>getXMLResources("Instance");
	        assertTrue(instances.size() > 0);
	        for (Instance instance: instances) {
	            assertEquals(1, instance.getContexts().size());    
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
	        List<Instance> instances = store.<Instance>getXMLResources("Instance");
	        assertTrue(instances.size() > 0);
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
	        List<Instance> instances = store.<Instance>getXMLResources("Instance");
	        assertTrue(instances.size() > 0);
	        for (Instance instance: instances) {
	            assertEquals(2, instance.getUnits().size());
            }
	    } catch (Exception e) {
	        fail(e.getMessage());
	    }
	}
	
	/**
	 * Test getting a specific unit.
	 */
	public void testGetSpecificUnit() {
	    try {
	        List<Instance> instances = store.<Instance>getXMLResources("Instance");
	        assertTrue(instances.size() > 0);
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
            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            assertTrue(instances.size() > 0);
            for (Instance instance: instances) {
                assertEquals(1, instance.getSchemaRefs().size());
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
            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            assertTrue(instances.size() > 0);
            for (Instance instance: instances) {
                assertEquals(0, instance.getLinkbaseRefs().size());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	public void testGetFacts() {
        try {
            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            assertTrue(instances.size() > 0);
            for (Instance instance: instances) {
                assertEquals(2, instance.getFacts().size());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
    public void testGetAllFactCount() {
        try {
            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            assertTrue(instances.size() > 0);
            for (Instance instance: instances) {
                logger.info(instance.getAllFactCount());
                logger.info(instance.getFactCount());
                assertTrue(instance.getAllFactCount() > 0);
                assertTrue(instance.getAllFactCount() > instance.getFactCount());
                assertTrue(instance.getAllFacts().size() > 0);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }	
	
    public void testGetTuples() {
        try {
            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            assertTrue(instances.size() > 0);
            for (Instance instance: instances) {
                assertEquals(2, instance.getTuples().size());
                assertEquals(0, instance.getItems().size());
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
            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            assertTrue(instances.size() > 0);
            for (Instance instance: instances) {
                assertEquals(0, instance.getFootnoteLinks().size());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}	
		
}
