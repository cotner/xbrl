package org.xbrlapi.fragment.tests;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests the implementation of the org.xbrlapi.Concept interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ConceptTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.footnote.links";
	private final String STARTING_POINT_2 = "test.data.label.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ConceptTestCase(String arg0) {
		super(arg0);
	}

	public void testGetPeriodType() {	

        try {
            FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.getLength() > 0);
            for (Concept concept: concepts) {
                if (concept.getName().equals("CurrentAsset"))
                    assertEquals("duration", concept.getPeriodType());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	public void testGetBalance() {	

		try {
            FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.getLength() > 0);
            for (Concept concept: concepts) {
                if (concept.getName().equals("CurrentAsset"))
                    assertNull(concept.getBalance());
            }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testGetLocators() {	

        try {
            FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.getLength() > 0);
            for (Concept concept: concepts) {
                if (concept.getName().equals("CurrentAsset"))
                    assertEquals(0,concept.getReferencingLocators().getLength());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}	
	
	public void testGetLabels() {
		try {
			loader.discover(this.getURL(STARTING_POINT_2));		

			FragmentList<Concept> concepts = store.getFragments("Concept");
			for (Concept concept: concepts) {
				if (concept.getName().equals("CurrentAsset"))
					assertEquals(3,concept.getLabels().getLength());
			}

		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}
}
