package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.Segment;

/**
 * Tests the implementation of the org.xbrlapi.Segment interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SegmentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.segments";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public SegmentTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting complex content for the segment.
	 */
	public void testGetComplexContent() {
        try {
            List<Segment> fragments = store.<Segment>getXMLResources("Segment");
            assertTrue(fragments.size() > 0);
            for (Segment fragment: fragments) {
                assertEquals(3, fragment.getComplexContent().getLength());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test getting the segment entity.
	 */
	public void testGetEntity() {
        try {
            List<Segment> fragments = store.<Segment>getXMLResources("Segment");
            assertTrue(fragments.size() > 0);
            for (Segment fragment: fragments) {
                assertEquals("org.xbrlapi.impl.EntityImpl", fragment.getEntity().getType());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
}
