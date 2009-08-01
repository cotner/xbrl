package org.xbrlapi.data.dom.tests;

import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Instance;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class XBRLFunctionTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.small.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public XBRLFunctionTestCase(String arg0) {
		super(arg0);
	}

    public void testGetConcepts() {
        try {
            List<Instance> instances = store.<Instance>getXMLResources("Instance");
            for (Instance instance: instances) {
                for (Fact fact: instance.getFacts()) {
                    Concept concept = store.getConcept(fact.getNamespace(),fact.getLocalname());
                    logger.info("Testing concept " + concept.getName() + " " + concept.getTargetNamespace());
                    assertEquals(fact.getLocalname(),concept.getDataRootElement().getAttribute("name"));
                    assertEquals(fact.getNamespace(),concept.getTargetNamespace());
                }
            }
        } catch (XBRLException e) {
            e.printStackTrace();
            fail("Unexpected " + e.getMessage());
        }
    }

}
