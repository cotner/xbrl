package org.xbrlapi.fragment.tests;

import java.net.URI;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Instance;

/**
 * Tests the period summary methods of the Instance interface.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InstancePeriodSummaryTestCase extends DOMLoadingTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public InstancePeriodSummaryTestCase(String arg0) {
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
	
}
