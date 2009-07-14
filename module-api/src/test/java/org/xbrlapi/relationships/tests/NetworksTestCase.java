package org.xbrlapi.relationships.tests;

/**
 * @see org.xbrlapi.impl.Networks
 * @see org.xbrlapi.impl.NetworksImpl
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.LabelResource;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.utilities.Constants;

public class NetworksTestCase extends DOMLoadingTestCase {

	Networks networks = null;
	LabelResource label = null;
	Concept concept = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(getURI("test.data.xlink.titles"));
		List<LabelResource> labels = store.<LabelResource>getXMLResources("LabelResource");
		label = labels.get(0);
		List<Concept> concepts = store.<Concept>getXMLResources("Concept");
		concept = concepts.get(0);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public NetworksTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test whether the networks loading process operates.
	 */
	public void testRetrievalOfArcAndLinkRoles() {	

		try {

		    networks = store.getNetworks(Constants.LabelArcrole);

			List<URI> arcroles = networks.getArcroles();
			assertEquals(1, arcroles.size());
			assertEquals(Constants.LabelArcrole,arcroles.get(0));

			List<URI> linkroles = networks.getLinkRoles(arcroles.get(0));
			assertEquals(1, linkroles.size());
			assertEquals(Constants.StandardLinkRole,linkroles.get(0));
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
    public void testMultipleAdditionsOfTheOneRelationship() {  

        try {
            
            networks = store.getNetworks(Constants.LabelArcrole);
            
            List<URI> arcroles = networks.getArcroles();
            assertEquals(1, arcroles.size());
            assertEquals(Constants.LabelArcrole,arcroles.get(0));

            List<URI> linkRoles = networks.getLinkRoles(arcroles.get(0));
            assertEquals(1, linkRoles.size());
            assertEquals(Constants.StandardLinkRole,linkRoles.get(0));
            
            Networks myNetworks = store.getNetworks();
            networks.addAll(myNetworks);
            assertEquals(networks.getSize(),myNetworks.getSize());
            
            for (Network network: networks) {
                Network myNetwork = myNetworks.getNetwork(network.getLinkRole(),network.getArcrole());
                assertEquals(network.getNumberOfRelationships(),myNetwork.getNumberOfRelationships());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	

	/**
	 * Test whether the networks loading process operates.
	 */
	public void testRetrievalOfANetwork() {	

		try {
            networks = store.getNetworks(Constants.LabelArcrole);

			List<URI> arcroles = networks.getArcroles();
			List<URI> linkroles = networks.getLinkRoles(arcroles.get(0));
			Network network = networks.getNetwork(linkroles.get(0),arcroles.get(0));
			assertEquals(Constants.LabelArcrole,network.getArcrole());
			assertEquals(Constants.StandardLinkRole,network.getLinkRole());
			SortedSet<Relationship> relationships = network.getActiveRelationshipsFrom(label.getIndex());
			assertEquals(0,relationships.size());
			relationships = network.getActiveRelationshipsTo(label.getIndex());
			assertEquals(1,relationships.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test information provided by active relationships in a network.
	 */
	public void testRelationshipRetrieval() {	
		
		try {

		    networks = store.getNetworks(Constants.LabelArcrole);
			
		    List<URI> arcroles = networks.getArcroles();
			List<URI> linkroles = networks.getLinkRoles(arcroles.get(0));
			Network network = networks.getNetwork(linkroles.get(0),arcroles.get(0));
			SortedSet<Relationship> relationships = network.getActiveRelationshipsTo(label.getIndex());
			Relationship relationship = relationships.first();
			assertEquals(Constants.LabelArcrole,relationship.getArc().getArcrole());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	/**
	 * Test retrieval of fragments given a source or target and arc role and link role
	 * information.
	 */
	public void testFragmentRetrieval() {	
		
		try {

			List<LabelResource> labels = store.getTargets(concept.getIndex(),null,Constants.LabelArcrole);
			assertEquals(1,labels.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	/**
	 * Test retrieval of networks for a given arc role.
	 */
	public void testGeneralNetworkRetrievalForAGivenArcrole() {	
		
		try {

			networks = store.getNetworks(Constants.LabelArcrole);
			assertEquals(1,networks.getSize());
			Network network = networks.getNetwork(Constants.StandardLinkRole,Constants.LabelArcrole);
			assertNotNull(network);
			Set<String> rootIndexes = network.getRootFragmentIndexes();
			assertEquals(1,rootIndexes.size());
			List<Concept> roots = network.getRootFragments();
			assertEquals(1,roots.size());
			assertEquals(concept.getPeriodType(),roots.get(0).getPeriodType());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	


}
