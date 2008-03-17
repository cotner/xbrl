package org.xbrlapi.fragment.tests;


import org.w3c.dom.NodeList;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Scenario;

/**
 * Tests the implementation of the org.xbrlapi.OpenContextComponent interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class OpenContextComponentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.scenarios";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public OpenContextComponentTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting complex content.
	 */
	public void testGetComplexContent() {

		try {
            FragmentList<Scenario> scenarios = store.getFragments("Scenario");
            assertTrue(scenarios.getLength() > 0);
            for (Scenario scenario: scenarios) {
                NodeList children = scenario.getDataRootElement().getChildNodes();
                assertEquals(children.getLength(), scenario.getComplexContent().getLength());
            }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test c-equality.
	 */
	public void testGetCEquality() {

		try {
            FragmentList<Scenario> scenarios = store.getFragments("Scenario");
            assertTrue(scenarios.getLength() > 0);
            for (Scenario scenario: scenarios) {
                assertTrue(scenario.equals(scenario));
            }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
}
