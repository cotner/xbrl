package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.NetworksImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests the implementation of the org.xbrlapi.Concept interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ConceptTestCase extends DOMLoadingTestCase {
	private final String FOOTNOTELINKS = "test.data.footnote.links";
	private final String LABELLINKS = "test.data.label.links";
    private final String PRESENTATIONLINKS = "test.data.local.xbrl.presentation.simple";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ConceptTestCase(String arg0) {
		super(arg0);
	}

	public void testGetPeriodType() {	

        try {
            loader.discover(this.getURL(FOOTNOTELINKS));        
            FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.getLength() > 0);
            for (Concept concept: concepts) {
                if (concept.getName().equals("CurrentAsset"))
                    assertEquals("duration", concept.getPeriodType());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
    public void testGetFacts() {   

        boolean testDone = false;
        try {
            loader.discover(this.getURL(FOOTNOTELINKS));        
            FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.getLength() > 0);
            for (Concept concept: concepts) {
                FragmentList<Fact> facts = concept.getFacts();
                for (Fact fact: facts) {
                    assertEquals(fact.getLocalname(), fact.getConcept().getName());
                    testDone = true;
                }
            }
            assertTrue(testDone);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }	
	
	public void testGetBalance() {	

		try {
	        loader.discover(this.getURL(FOOTNOTELINKS));        
            FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.getLength() > 0);
            for (Concept concept: concepts) {
                if (concept.getName().equals("CurrentAsset"))
                    assertNull(concept.getBalance());
            }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testGetLocators() {	

        try {
            loader.discover(this.getURL(FOOTNOTELINKS));        
            FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
            assertTrue(concepts.getLength() > 0);
            for (Concept concept: concepts) {
                if (concept.getName().equals("CurrentAsset"))
                    assertEquals(0,concept.getReferencingLocators().getLength());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}	
	
	public void testGetLabels() {
		try {
	        loader.discover(this.getURL(LABELLINKS));       

			FragmentList<Concept> concepts = store.getFragments("Concept");
			for (Concept concept: concepts) {
				if (concept.getName().equals("CurrentAsset"))
					assertEquals(3,concept.getLabels().getLength());
			}

		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}
	
    public void testGetPresentationNetworks() {
        try {
            loader.discover(this.getURL(this.PRESENTATIONLINKS));       

            Networks networks = new NetworksImpl(store);
            store.setStoredNetworks(networks);
            FragmentList<Item> facts = ((XBRLStore) store).getItems();
            assertEquals(1,facts.getLength());
            for (Item fact: facts) {
                logger.info(fact.getConcept().getLabels().get(0).getStringValue());
                networks = ((XBRLStore) store).getMinimalNetworksWithArcrole(fact.getConcept(),Constants.PresentationArcRole);
            }
            List<Network> presentationNetworks = networks.getNetworks(Constants.PresentationArcRole);
            
            assertEquals(1,presentationNetworks.size());
            
            Network network = presentationNetworks.get(0);
            
            assertEquals(2,network.getNumberOfActiveRelationships());

            FragmentList<Fragment> roots = network.getRootFragments(); 
            assertEquals(1,roots.getLength());

            network.complete();
            
            assertEquals(6,network.getNumberOfActiveRelationships());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
	
}
