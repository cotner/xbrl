package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.SimpleNumericItem;
import org.xbrlapi.Tuple;

/**
 * Tests the implementation of the org.xbrlapi.Fact interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class FactTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public FactTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting instance.
	 */
	public void testGetInstance() {

		try {
			List<SimpleNumericItem> fragments = store.<SimpleNumericItem>getXMLResources("SimpleNumericItem");
			Fact fragment = fragments.get(0);
			assertEquals("xbrl", fragment.getInstance().getLocalname());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	/**
	 * Test getting the parent tuple for a simple fact, a tuple and a 
	 * tuple that is not contained by another tuple.
	 */
	public void testGetTuple() {

		try {
			List<Fact> fragments = store.<Fact>getXMLResources("SimpleNumericItem");
			Fact fragment = fragments.get(0);
			assertEquals("org.xbrlapi.impl.TupleImpl", fragment.getTuple().getType());
			fragments = store.getXMLResources("Tuple");
			assertEquals(4,fragments.size());
			for (int i=0; i<fragments.size();i++) {
				fragment = fragments.get(i);
				Tuple tuple = fragment.getTuple();
				if (tuple != null) {
					assertEquals("org.xbrlapi.impl.TupleImpl", tuple.getType());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the concept for this fact.
	 */
	public void testGetConcept() {
		try {
			List<Fact> fragments = store.<Fact>getXMLResources("SimpleNumericItem");
			for (int i=0; i< fragments.size(); i++) {
				Fact fact = fragments.get(i);
				if (fact.getLocalname().equals("managementAge")) {
					Concept concept = fact.getConcept();
					assertNotNull(concept);
					assertEquals("org.xbrlapi.impl.ConceptImpl", concept.getType());
					assertEquals(false, concept.isAbstract());
					assertEquals(fact.getLocalname(), concept.getName());					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
