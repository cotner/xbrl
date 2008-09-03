package org.xbrlapi.relationships.tests;

/**
 * @see org.xbrlapi.impl.Networks
 * @see org.xbrlapi.impl.NetworksImpl
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.List;
import java.util.Set;

import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.Relationship;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;
import org.xbrlapi.utilities.Constants;

public class NetworksTestCase extends DOMLoadingTestCase {

	Networks networks = null;
	LabelResource label = null;
	Concept concept = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(getURL("test.data.xlink.titles"));
		FragmentList<LabelResource> labels = store.<LabelResource>getFragments("LabelResource");
		label = labels.get(0);
		FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
		concept = concepts.get(0);
		networks = label.getNetworks();
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

			List<String> arcroles = networks.getArcRoles();
			assertEquals(1, arcroles.size());
			assertEquals(Constants.LabelArcRole,arcroles.get(0));

			List<String> linkroles = networks.getLinkRoles(arcroles.get(0));
			assertEquals(1, linkroles.size());
			assertEquals(Constants.StandardLinkRole,linkroles.get(0));
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test whether the networks loading process operates.
	 */
	public void testRetrievalOfANetwork() {	

		try {

			List<String> arcroles = networks.getArcRoles();
			List<String> linkroles = networks.getLinkRoles(arcroles.get(0));
			Network network = networks.getNetwork(arcroles.get(0),linkroles.get(0));
			assertEquals(Constants.LabelArcRole,network.getArcRole());
			assertEquals(Constants.StandardLinkRole,network.getLinkRole());
			List<Relationship> relationships = network.getActiveRelationshipsFrom(label.getFragmentIndex());
			assertEquals(0,relationships.size());
			relationships = network.getActiveRelationshipsTo(label.getFragmentIndex());
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

			List<String> arcroles = networks.getArcRoles();
			List<String> linkroles = networks.getLinkRoles(arcroles.get(0));
			Network network = networks.getNetwork(arcroles.get(0),linkroles.get(0));
			List<Relationship> relationships = network.getActiveRelationshipsTo(label.getFragmentIndex());
			Relationship relationship = relationships.get(0);
			assertEquals(Constants.LabelArcRole,relationship.getArc().getArcrole());
			
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

			//Network network = networks.getNetwork(Constants.LabelArcRole,Constants.StandardLinkRole);
			FragmentList<LabelResource> labels = networks.getTargetFragments(concept.getFragmentIndex(),Constants.LabelArcRole);
			assertEquals(1,labels.getLength());
			assertEquals(label.getStringValue(),labels.getFragment(0).getStringValue());
			
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

			networks = store.getNetworks(Constants.LabelArcRole);
			assertEquals(1,networks.getSize());
			Network network = networks.getNetwork(Constants.LabelArcRole,Constants.StandardLinkRole);
			assertNotNull(network);
			Set<String> rootIndexes = network.getRootFragmentIndexes();
			assertEquals(1,rootIndexes.size());
			FragmentList<Concept> roots = network.getRootFragments();
			assertEquals(1,roots.getLength());
			assertEquals(concept.getPeriodType(),roots.get(0).getPeriodType());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
}
