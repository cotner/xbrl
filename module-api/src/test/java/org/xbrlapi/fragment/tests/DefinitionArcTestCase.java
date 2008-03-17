package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.DefinitionArc;
import org.xbrlapi.FragmentList;
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
            FragmentList<DefinitionArc> fragments = store.<DefinitionArc>getFragments("DefinitionArc");
            assertTrue(fragments.getLength() > 0);
            for (DefinitionArc fragment: fragments) {
                assertNull(fragment.getShow());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test getting actuate value.
	 */
	public void testGetActuate() {	
        try {
            FragmentList<DefinitionArc> fragments = store.<DefinitionArc>getFragments("DefinitionArc");
            assertTrue(fragments.getLength() > 0);
            for (DefinitionArc fragment: fragments) {
                assertNull(fragment.getActuate());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
}
