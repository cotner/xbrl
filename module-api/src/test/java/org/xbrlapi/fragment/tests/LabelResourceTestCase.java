package org.xbrlapi.fragment.tests;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;
/**
 * Tests the implementation of the org.xbrlapi.LabelResource interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LabelResourceTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.label.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public LabelResourceTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the string value of the resource.
	 */
	public void testGetStringValue() {	

		try {
			FragmentList<LabelResource> fragments = store.<LabelResource>getFragments("LabelResource");
			LabelResource fragment = fragments.getFragment(0);
			assertEquals("Current Asset", fragment.getStringValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    /**
     * Test getting the set of concepts with this label.
     */
    public void testGetConcepts() {  

        try {
            FragmentList<LabelResource> fragments = store.<LabelResource>getFragments("LabelResource");
            LabelResource fragment = fragments.getFragment(0);
            FragmentList<Concept> concepts = fragment.getConcepts();
            assertTrue(concepts.getLength() > 0);
            for (Concept concept: concepts) {
                assertEquals("org.xbrlapi.impl.ConceptImpl",concept.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
}
