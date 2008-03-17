package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FootnoteArc;
import org.xbrlapi.FragmentList;

/**
 * Tests the implementation of the org.xbrlapi.FootnoteArc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FootnoteArcTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.footnote.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public FootnoteArcTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting show value.
	 */
	public void testGetShow() {
        try {
            FragmentList<FootnoteArc> fragments = store.<FootnoteArc>getFragments("FootnoteArc");
            assertTrue(fragments.getLength() > 0);
            for (FootnoteArc fragment: fragments) {
                assertEquals("replace", fragment.getShow());
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
            FragmentList<FootnoteArc> fragments = store.<FootnoteArc>getFragments("FootnoteArc");
            assertTrue(fragments.getLength() > 0);
            for (FootnoteArc fragment: fragments) {
                assertEquals("onRequest", fragment.getActuate());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
}
