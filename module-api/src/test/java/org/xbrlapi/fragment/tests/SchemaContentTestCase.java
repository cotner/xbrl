package org.xbrlapi.fragment.tests;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.Locator;
import org.xbrlapi.Schema;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests the implementation of the org.xbrlapi.SchemaContent interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaContentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.multi.concept.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public SchemaContentTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting the schema fragment.
	 */
	public void testGetSchema() {		
		try {
		    List<Concept> concepts = store.<Concept>getFragments("Concept");
		    assertTrue(concepts.size() > 0);
		    for (Concept concept: concepts) {
	            assertEquals(concept.getParent().getIndex(), concept.getSchema().getIndex());
		    }
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the schema target namespace URI.
	 */
	public void testGetSchemaTargetNamespace() {		

	    try {
            List<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.size() > 0);
            for (Concept concept: concepts) {
                Schema schema = concept.getSchema();
                assertEquals(concept.getTargetNamespace().toString(), schema.getDataRootElement().getAttribute("targetNamespace"));
            }
        } catch (XBRLException e) {
            fail(e.getMessage());
        }
	}	

	/**
	 * Test getting the locators that target a schema component.
	 */
	public void testGetLocators() {
        try {
            List<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.size() > 0);
            for (Concept concept: concepts) {
                List<Locator> locators = concept.getReferencingLocators();
                assertTrue(locators.size() > 0);
            }
        } catch (XBRLException e) {
            fail(e.getMessage());
        }
	}	
}
