package org.xbrlapi.data.dom.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Instance;
import org.xbrlapi.Schema;
import org.xbrlapi.Tuple;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class XBRLFunctionTestCase extends BaseTestCase {
	private final String STARTING_POINT_1 = "test.data.small.instance";
	private final String STARTING_POINT_2 = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public XBRLFunctionTestCase(String arg0) {
		super(arg0);
	}

    public void testGetConcepts() {
        try {
            loader.discover(this.getURI(STARTING_POINT_1));     
            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            for (Instance instance: instances) {
                for (Fact fact: instance.getFacts()) {
                    Concept concept = store.getConcept(fact.getNamespace(),fact.getLocalname());
                    logger.info("Testing concept " + concept.getName() + " " + concept.getTargetNamespace());
                    assertEquals(fact.getLocalname(),concept.getDataRootElement().getAttribute("name"));
                    assertEquals(fact.getNamespace(),concept.getTargetNamespace());
                }
            }
            
            try {
                store.getConcept(Constants.XBRL21Namespace,"rubbishConceptName");
                fail("There is no such concept as " + Constants.XBRL21Namespace + ":rubbishConceptName");
            } catch (Exception e) {
                ;
            }
            
        } catch (XBRLException e) {
            e.printStackTrace();
            fail("Unexpected " + e.getMessage());
        }
    }

    
    public void testGetTuples() {
        try {
            loader.discover(this.getURI(STARTING_POINT_2));
            List<Tuple> tuples = store.getTuples();
            assertTrue(tuples.size() > 0);

            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            assertTrue(instances.size() > 0);
            for (Instance instance: instances) {
                List<Fact> facts = instance.getFacts();
                assertTrue(facts.size() > 0);
                for (Fact fact: facts) {
                    fact.serialize();
                }
            }
            
        } catch (XBRLException e) {
            e.printStackTrace();
            fail("Unexpected " + e.getMessage());
        }
    }
    
    public void testGetSchemaBasedOnTargetNamespace() {
        Schema schema;
        try {
            loader.discover(this.getURI(STARTING_POINT_2));
            schema = store.getSchema(Constants.XBRL21Namespace);
            assertTrue(schema.getTargetNamespace().equals(Constants.XBRL21Namespace));
        } catch (XBRLException e) {
            e.printStackTrace();
            fail("Unexpected " + e.getMessage());
        }
        try {
            schema = store.getSchema(new URI("http://rubbish.namespace/"));
            assertNull(schema);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected " + e.getMessage());
        }
    }    
    
    public void testGetFacts() {
        try {
            loader.discover(this.getURI(STARTING_POINT_1));
            loader.discover(this.getURI(STARTING_POINT_2));
            List<Fact> facts = store.getFacts();
            assertTrue(facts.size() > 0);

            for (Fact fact: facts) {
                fact.serialize();
            }
            
        } catch (XBRLException e) {
            e.printStackTrace();
            fail("Unexpected " + e.getMessage());
        }
    }    

}
