package org.xbrlapi.fragment.tests;

import org.xbrlapi.Context;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Entity;
import org.xbrlapi.EntityResource;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;

/**
 * Tests the implementation of the org.xbrlapi.EntityResource interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class EntityResourceTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.local.entities.simple";
	private final String ENTITY_MAP = "real.data.sec.entity.map";
	
	protected void setUp() throws Exception {
		super.setUp();
        loader.discover(this.getURL(STARTING_POINT));	
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public EntityResourceTestCase(String arg0) {
		super(arg0);
	}
	
    public void testEntityResourceManipulations() {
        try {
            
            FragmentList<Context> contexts = store.<Context>getFragments("Context");
            assertTrue("There are two contexts.",contexts.getLength() == 2);

            for (Context context: contexts) {
                if (context.getId().equals("c1")) {
                    Entity entity = context.getEntity();
                    assertEquals("http://xbrlapi.org/integer/entity/scheme/", entity.getIdentifierScheme());
                    assertEquals("1", entity.getIdentifierValue());
                    FragmentList<EntityResource> resources = entity.getEntityResources();
                    assertEquals(1,resources.getLength());
                    EntityResource resource = resources.get(0);
                    FragmentList<EntityResource> equivalents = resource.getEquivalents();
                    assertEquals(2,equivalents.getLength());
                    FragmentList<LabelResource> labels = resource.getLabels();
                    assertEquals(1,labels.getLength());
                    labels = entity.getAllEntityLabels();
                    assertEquals(2,labels.getLength());
                    assertEquals("http://xbrlapi.org/integer/entity/scheme/",resource.getIdentifierScheme());
                    assertEquals("1",resource.getIdentifierValue());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
