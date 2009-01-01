package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Entity;
import org.xbrlapi.FragmentList;
/**
 * Tests the implementation of the org.xbrlapi.Entity interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class EntityTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.segments";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public EntityTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the scenario.
	 */
	public void testGetScenario() {
        try {
            FragmentList<Entity> entities = store.<Entity>getFragments("Entity");
            assertTrue(entities.getLength() > 0);
            for (Entity entity: entities) {
                assertEquals("org.xbrlapi.impl.SegmentImpl", entity.getSegment().getType());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test getting the identifier information.
	 */
	public void testGetSchemeAndValue() {

        try {
            FragmentList<Entity> entities = store.<Entity>getFragments("Entity");
            assertTrue(entities.getLength() > 0);
            for (Entity entity: entities) {
                assertEquals("www.dnb.com", entity.getIdentifierScheme());
                assertEquals("121064880", entity.getIdentifierValue());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
}
