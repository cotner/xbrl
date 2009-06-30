package org.xbrlapi.fragment.tests;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.ExtendedLink;
import java.util.List;
import org.xbrlapi.Schema;

/**
 * Tests the implementation of the org.xbrlapi.Fact interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	private final String SCHEMA_WITH_EMBEDDED_LINKS = "test.data.embedded.links.in.schema";	
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public SchemaTestCase(String arg0) {
		super(arg0);
	}

	public void testGetConceptBySubstitutionGroup() {

		try {
			List<Schema> fragments = store.<Schema>getXMLs("Schema");
			for (int i=0; i< fragments.size(); i++) {
				Schema schema = fragments.get(i);
				if (schema.getTargetNamespace().equals("schema.getTargetNamespaceURI")) {
					List<Concept> concepts = schema.getConcepts();
					assertTrue(concepts.size() > 0);
					Concept concept = (Concept) fragments.get(0);
					assertEquals(4, schema.getConceptsBySubstitutionGroup(concept.getSubstitutionGroupNamespace(),concept.getSubstitutionGroupLocalname()).size());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	public void testGetConceptByType() {

		try {
			List<Schema> fragments = store.<Schema>getXMLs("Schema");
			for (int i=0; i< fragments.size(); i++) {
				Schema schema = fragments.get(i);
				if (schema.getTargetNamespace().equals("schema.getTargetNamespaceURI")) {
					List<Concept> concepts = schema.getConcepts();
					Concept concept = concepts.get(0);
					assertEquals(3, schema.getConceptsByType(concept.getTypeNamespace(),concept.getTypeLocalname()).size());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testGetConceptByName() {

		try {
			List<Schema> fragments = store.<Schema>getXMLs("Schema");
			for (int i=0; i< fragments.size(); i++) {
				Schema schema = fragments.get(i);
				if (schema.getTargetNamespace().equals("schema.getTargetNamespaceURI")) {
					String name = "managementName";
					assertEquals(name, schema.getConceptByName(name).getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testGetReferencePartDeclarationByName() {
		// TODO !!! Create an example with reference part declarations in the conformance suite
		try {
			List<Schema> fragments = store.<Schema>getXMLs("Schema");
			Schema schema = fragments.get(0);
			String name = "managementName";
			assertNull(schema.getReferencePartDeclaration(name));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testGetReferencePartDeclarations() {
		try {
			List<Schema> fragments = store.<Schema>getXMLs("Schema");
			Schema schema = fragments.get(0);
			assertEquals(0,schema.getReferencePartDeclarations().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

	public void testGetConcepts() {

		try {
			List<Schema> fragments = store.<Schema>getXMLs("Schema");
			for (int i=0; i< fragments.size(); i++) {
				Schema schema = fragments.get(i);
				List<Concept> concepts = schema.getConcepts();
				if (concepts.size() > 0) {
					assertTrue(concepts.get(0).getType().equals("org.xbrlapi.impl.ConceptImpl"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testGetExtendedLinks() {

		try {
			loader.discover(this.getURI(this.SCHEMA_WITH_EMBEDDED_LINKS));
			List<Schema> fragments = store.<Schema>getXMLs("Schema");
			logger.debug("There are " + fragments.size() + " schemas.");
			for (int i=0; i< fragments.size(); i++) {
				Schema schema = fragments.get(i);
				List<ExtendedLink> links = schema.getExtendedLinks();
				if (links.size() > 0) {
					logger.debug("Schema " + schema.getURI() + " contains " + links.size() + " extended links.");
					assertTrue(links.get(0).getType().equals("org.xbrlapi.impl.ExtendedLinkImpl"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testIsElementFormQualified() {

		try {
			List<Schema> fragments = store.<Schema>getXMLs("Schema");
			Schema schema = fragments.get(0);
			assertEquals(true, schema.isElementFormQualified());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testGetTargetNamespace() {
		try {
			List<Schema> schemas = store.<Schema>getXMLs("Schema");
			Schema schema = schemas.get(0);
			assertEquals("http://mycompany.com/xbrl/taxonomy", schema.getTargetNamespace().toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
