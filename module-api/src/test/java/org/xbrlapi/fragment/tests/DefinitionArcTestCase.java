package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.DefinitionArc;
/**
 * Tests the implementation of the org.xbrlapi.DefinitionArc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class DefinitionArcTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.definition.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public DefinitionArcTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting show value.
	 */
	public void testGetShow() {	

		try {
			DefinitionArc arc = (DefinitionArc) store.getFragment("83");
			assertNull(arc.getShow());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting actuate value.
	 */
	public void testGetActuate() {	

		try {
			DefinitionArc arc = (DefinitionArc) store.getFragment("83");
			assertNull(arc.getActuate());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
