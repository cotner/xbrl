package org.xbrlapi.fragment.tests;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.FragmentList;
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
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public SchemaTestCase(String arg0) {
		super(arg0);
	}

	public void testGetConceptBySubstitutionGroup() {

		try {
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			for (int i=0; i< fragments.getLength(); i++) {
				Schema schema = fragments.getFragment(i);
				if (schema.getTargetNamespaceURI().equals("schema.getTargetNamespaceURI")) {
					FragmentList<Concept> concepts = schema.getConcepts();
					assertTrue(concepts.getLength() > 0);
					Concept concept = (Concept) fragments.getFragment(0);
					assertEquals(4, schema.getConceptsBySubstitutionGroup(concept.getSubstitutionGroupNamespace(),concept.getSubstitutionGroupLocalname()).getLength());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	public void testGetConceptByType() {

		try {
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			for (int i=0; i< fragments.getLength(); i++) {
				Schema schema = fragments.getFragment(i);
				if (schema.getTargetNamespaceURI().equals("schema.getTargetNamespaceURI")) {
					FragmentList<Concept> concepts = schema.getConcepts();
					Concept concept = concepts.getFragment(0);
					assertEquals(3, schema.getConceptsByType(concept.getTypeNamespace(),concept.getTypeLocalname()).getLength());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testGetConceptByName() {

		try {
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			for (int i=0; i< fragments.getLength(); i++) {
				Schema schema = fragments.getFragment(i);
				if (schema.getTargetNamespaceURI().equals("schema.getTargetNamespaceURI")) {
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
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			Schema schema = fragments.getFragment(0);
			String name = "managementName";
			assertNull(schema.getReferencePartDeclaration(name));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testGetReferencePartDeclarations() {
		try {
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			Schema schema = fragments.getFragment(0);
			assertEquals(0,schema.getReferencePartDeclarations().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

	public void testGetConcepts() {

		try {
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			for (int i=0; i< fragments.getLength(); i++) {
				Schema schema = fragments.getFragment(i);
				FragmentList<Concept> concepts = schema.getConcepts();
				if (concepts.getLength() > 0) {
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
			loader.discover(this.getURL(this.SCHEMA_WITH_EMBEDDED_LINKS));
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			logger.debug("There are " + fragments.getLength() + " schemas.");
			for (int i=0; i< fragments.getLength(); i++) {
				Schema schema = fragments.getFragment(i);
				FragmentList<ExtendedLink> links = schema.getExtendedLinks();
				if (links.getLength() > 0) {
					logger.debug("Schema " + schema.getURL() + " contains " + links.getLength() + " extended links.");
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
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			Schema schema = fragments.getFragment(0);
			assertEquals(true, schema.isElementFormQualified());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testGetTargetNamespace() {

		try {
			FragmentList<Schema> fragments = store.<Schema>getFragments("Schema");
			Schema schema = fragments.getFragment(0);
			assertEquals("http://mycompany.com/xbrl/taxonomy", schema.getTargetNamespaceURI());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
