package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Schema;
import org.xbrlapi.SchemaContent;
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
		loader.discover(this.getURL(STARTING_POINT));		
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
			SchemaContent sc = (SchemaContent) store.getFragment("5");
			Schema s = sc.getSchema();
			assertEquals("http://xbrl.example.com/397/ABC", s.getTargetNamespaceURI());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test getting the schema target namespace URI.
	 */
	public void testGetSchemaTargetNamespaceURI() {		
		try {
			SchemaContent sc = (SchemaContent) store.getFragment("5");
			assertEquals("http://xbrl.example.com/397/ABC", sc.getTargetNamespaceURI());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}	

	/**
	 * Test getting the locators that target a schema component.
	 */
	public void testGetLocators() {		
		try {
			SchemaContent sc = (SchemaContent) store.getFragment("5");
			assertEquals(1, sc.getReferencingLocators().getLength());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}	
}
