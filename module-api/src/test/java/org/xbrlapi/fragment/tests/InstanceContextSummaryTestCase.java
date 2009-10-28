package org.xbrlapi.fragment.tests;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Instance;

/**
 * Tests the period summary methods of the Instance interface.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InstanceContextSummaryTestCase extends DOMLoadingTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public InstanceContextSummaryTestCase(String arg0) {
		super(arg0);
	}

	public void testGetLatestPeriod() {
	    try {
	        URI uri = this.getURI("test.data.local.xbrl.instance.period.summary.1");
	        loader.discover(uri);
	        Instance instance = store.<Instance>getRootFragmentForDocument(uri);
	        assertEquals("2008-12-31",instance.getLatestPeriod());
	    } catch (Exception e) {
	        e.printStackTrace();
	        fail(e.getMessage());
	    }
	}
	
    public void testGetEarliestPeriod() {
        try {
            URI uri = this.getURI("test.data.local.xbrl.instance.period.summary.1");
            loader.discover(uri);
            Instance instance = store.<Instance>getRootFragmentForDocument(uri);
            assertEquals("2007-12-31",instance.getEarliestPeriod());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
	
    
    public void testGetEntityResources() {
        try {
            URI uri = this.getURI("test.data.local.xbrl.instance.period.summary.1");
            loader.discover(uri);
            Instance instance = store.<Instance>getRootFragmentForDocument(uri);
            assertEquals(0,instance.getEntityResources().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    public void testGetConcepts() {
        try {
            URI uri = this.getURI("test.data.local.xbrl.instance.period.summary.1");
            loader.discover(uri);
            Instance instance = store.<Instance>getRootFragmentForDocument(uri);
            List<Concept> concepts = instance.getChildConcepts();
            assertEquals(2,concepts.size());
            assertEquals(2,instance.getChildConceptsCount());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }    

    public void testGetEntityIdentifiers() {
        try {
            URI uri = this.getURI("test.data.local.xbrl.instance.period.summary.1");
            loader.discover(uri);
            Instance instance = store.<Instance>getRootFragmentForDocument(uri);
            Map<String,Set<String>> map = instance.getEntityIdentifiers();
            assertEquals(1,map.size());
            assertEquals("http://xbrl.org/entity/identification/scheme",map.keySet().iterator().next());
            assertEquals(1,map.get("http://xbrl.org/entity/identification/scheme").size());
            assertEquals("AAA001",map.get("http://xbrl.org/entity/identification/scheme").iterator().next());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }   
    
}
