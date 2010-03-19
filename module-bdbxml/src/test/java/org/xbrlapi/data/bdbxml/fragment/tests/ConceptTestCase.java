package org.xbrlapi.data.bdbxml.fragment.tests;

import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.data.bdbxml.BaseTestCase;
import org.xbrlapi.impl.ConceptImpl;

/**
 * Tests the implementation of the org.xbrlapi.Concept getFactCount method.
 * Uses the Berkeley XML-based data store to confirm namespace declaration
 * handling testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ConceptTestCase extends BaseTestCase {
	private final String FOOTNOTELINKS = "test.data.footnote.links";
	private final String LABELLINKS = "test.data.label.links";
    private final String PRESENTATIONLINKS = "test.data.local.xbrl.presentation.simple";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ConceptTestCase(String arg0) {
		super(arg0);
	}

    public void testGetFactCount() {   

        try {
            loader.discover(this.getURI(FOOTNOTELINKS));        
            List<Concept> concepts = store.<Concept>getXMLResources(ConceptImpl.class);
            assertTrue(concepts.size() > 0);
            boolean foundFacts = false;
            for (Concept concept: concepts) {
                if (concept.getFactCount() > 0) {
                    foundFacts = true;
                }
                assertEquals(concept.getFactCount(),concept.getFacts().size());
                logger.info(concept.getFactCount());
            }
            assertTrue("no concepts found to have facts.", foundFacts);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }    

}
