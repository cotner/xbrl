package org.xbrlapi.data.bdbxml.tests;

import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.EntityResource;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;

/**
 * Tests the implementation of the org.xbrlapi.EntityResource interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class EntityResourceTestCase extends BaseTestCase {
	private final String STARTING_POINT = "real.data.sec.instance";
	private final String ENTITY_MAP = "real.data.sec.entity.map";
	
	protected void setUp() throws Exception {
		super.setUp();
        loader.discover(this.getURL(STARTING_POINT));       
		loader.discover(this.getURL(ENTITY_MAP));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public EntityResourceTestCase(String arg0) {
		super(arg0);
	}
	
	public void testEntityResourceManipulations() {
		try {
		    
			FragmentList<Context> contexts = store.<Context>query("/*[*/@id='eol_0001193125-08-040200_STD_p12m_20071231_6']");
            assertTrue("There is one context.",contexts.getLength() == 1);

            Context context = contexts.get(0);
			Entity entity = context.getEntity();
			assertEquals("http://www.sec.gov/CIK", entity.getIdentifierScheme());
            assertEquals("0000029669", entity.getIdentifierValue());

            FragmentList<EntityResource> r = store.<EntityResource>getFragments("EntityResource");
            assertTrue(r.getLength() > 0);
            for (EntityResource f: r) {
                if (f.getIdentifierValue().equals("0000029669")) {
                    //store.serialize(f);
                }
            }

            String query = "/*[@type='org.xbrlapi.impl.EntityImpl' and @url='http://www.sec.gov/Archives/edgar/data/29669/000119312508069335/rrd-20071231.xml' and */*/*[@scheme='http://www.sec.gov/CIK' and .='0000029669']]";
            FragmentList<Entity> es = store.<Entity>query(query);
            assertTrue(es.getLength() > 0);
            
			FragmentList<EntityResource> resources = entity.getEntityResources();
			assertEquals(1, resources.getLength());
			EntityResource resource = resources.get(0);

			FragmentList<Entity> entities = resource.getEntities(this.getURL(STARTING_POINT));
			assertTrue("There is more than one context.",entities.getLength() > 1);

			FragmentList<Entity> allEntities = resource.getEntities();
            assertEquals(allEntities.getLength(),entities.getLength());

            FragmentList<EntityResource> equivalents = resource.getEquivalents();
            assertEquals(2,equivalents.getLength());

            FragmentList<LabelResource> labels = entity.getEntityLabels();
            assertEquals(1,labels.getLength());

            FragmentList<LabelResource> allLabels = entity.getAllEntityLabels();
            assertEquals(2,allLabels.getLength());

            FragmentList<LabelResource> entityResourceLabels = resource.getLabels();
            assertEquals(1,entityResourceLabels.getLength());
            
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
