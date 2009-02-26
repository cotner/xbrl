package org.xbrlapi.relationships.tests;

/**
 * @see org.xbrlapi.impl.Networks
 * @see org.xbrlapi.impl.NetworksImpl
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.xbrlapi.ActiveRelationship;
import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;
import org.xbrlapi.networks.Analyser;
import org.xbrlapi.networks.AnalyserImpl;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.Storer;
import org.xbrlapi.networks.StorerImpl;

public class PersistedNetworksTestCase extends DOMLoadingTestCase {

	Networks networks = null;
	LabelResource label = null;
	Concept concept = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(getURI("test.data.xlink.titles"));
		FragmentList<LabelResource> labels = store.<LabelResource>getFragments("LabelResource");
		label = labels.get(0);
		FragmentList<Concept> concepts = store.<Concept>getFragments("Concept");
		concept = concepts.get(0);
		networks = label.getNetworks();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public PersistedNetworksTestCase(String arg0) {
		super(arg0);
	}
	
	private int initialSize = 0;
	private Storer storer = null;
	private void storeNetworks() throws Exception {
        storer = new StorerImpl(store);
        Networks networks = store.getAllNetworks();
        assertEquals(1,networks.getSize());
        initialSize = store.getSize();
        storer.storeRelationships(networks);
	}
	
    /**
     * Test persisting of active relationships for all networks.
     */
    public void testPersistingOfAllActiveRelationships() { 
        try {
            storeNetworks();
            logger.info("Initial size = " + initialSize);
            int augmentedSize = store.getSize();
            assertTrue(augmentedSize > initialSize);
            logger.info("Augmented size = " + augmentedSize);
            storer.deleteRelationships();
            assertEquals(initialSize,store.getSize());
            List<URI> arcroles = networks.getArcroles();
            assertEquals(1,arcroles.size());
            List<URI> linkRoles = networks.getLinkRoles(arcroles.get(0));
            assertEquals(1,linkRoles.size());
            Network network = networks.getNetwork(linkRoles.get(0),arcroles.get(0));
            storer.storeRelationships(network);
            assertEquals(augmentedSize,store.getSize());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test retrieval of arcroles
     */
    public void testAnalyserArcroleRetrieval() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> arcroles = analyser.getArcroles();
            assertTrue(arcroles.size() > 0);
            for (URI arcrole: arcroles) {
                logger.info(arcrole);
                Set<URI> linkRoles = analyser.getLinkRoles(arcrole);
                assertTrue(linkRoles.size() > 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
    }
    
    /**
     * Test retrieval of link roles
     */
    public void testAnalyserLinkRoleRetrieval() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> linkRoles = analyser.getLinkRoles();
            assertTrue(linkRoles.size() > 0);
            for (URI linkRole: linkRoles) {
                logger.info(linkRole);
                Set<URI> arcroles = analyser.getArcroles(linkRole);
                assertTrue(arcroles.size() > 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
    }
    
    public void testRelationshipRetrievalByArcrole() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> arcroles = analyser.getArcroles();
            assertTrue(arcroles.size() > 0);
            for (URI arcrole: arcroles) {
                FragmentList<ActiveRelationship> relationships = analyser.getRelationships(arcrole);
                assertTrue(relationships.size() > 0);
                for (ActiveRelationship relationship: relationships) {
                    assertEquals(relationship.getArcrole(),arcrole);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    public void testRelationshipRetrievalByArcroleAndLinkRole() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> linkRoles = analyser.getLinkRoles();
            for (URI linkRole: linkRoles) {
                Set<URI> arcroles = analyser.getArcroles(linkRole);
                assertTrue(arcroles.size() > 0);
                for (URI arcrole: arcroles) {
                    FragmentList<ActiveRelationship> relationships = analyser.getRelationships(linkRole,arcrole);
                    assertTrue(relationships.size() > 0);
                    for (ActiveRelationship relationship: relationships) {
                        assertEquals(relationship.getArcrole(),arcrole);
                        assertEquals(relationship.getLinkRole(),linkRole);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    public void testRelationshipRetrievalByArcroleAndSourceIndex() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> arcroles = analyser.getArcroles();
            assertTrue(arcroles.size() > 0);
            for (URI arcrole: arcroles) {
                FragmentList<ActiveRelationship> relationships = analyser.getRelationships(arcrole);
                assertTrue(relationships.size() > 0);
                for (ActiveRelationship relationship: relationships) {
                    String sourceIndex = relationship.getSourceIndex();
                    FragmentList<ActiveRelationship> otherRelationships = analyser.getRelationshipsFrom(sourceIndex,arcrole);
                    assertEquals(relationship.getArcrole(),arcrole);
                    assertEquals(sourceIndex,otherRelationships.get(0).getSourceIndex());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    public void testRelationshipRetrievalByArcroleAndTargetIndex() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> arcroles = analyser.getArcroles();
            assertTrue(arcroles.size() > 0);
            for (URI arcrole: arcroles) {
                FragmentList<ActiveRelationship> relationships = analyser.getRelationships(arcrole);
                assertTrue(relationships.size() > 0);
                for (ActiveRelationship relationship: relationships) {
                    String targetIndex = relationship.getTargetIndex();
                    FragmentList<ActiveRelationship> otherRelationships = analyser.getRelationshipsTo(targetIndex,arcrole);
                    assertEquals(relationship.getArcrole(),arcrole);
                    assertEquals(targetIndex,otherRelationships.get(0).getTargetIndex());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    public void testGetRootRelationshipsByArcroleAndLinkRole() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> arcroles = analyser.getArcroles();
            assertTrue(arcroles.size() > 0);
            for (URI arcrole: arcroles) {
                Set<URI> linkRoles= analyser.getLinkRoles(arcrole);
                for (URI linkRole: linkRoles) {
                    FragmentList<ActiveRelationship> relationships = analyser.getRootRelationships(linkRole, arcrole);
                    assertTrue(relationships.size() > 0);
                    for (ActiveRelationship relationship: relationships) {
                        String sourceIndex = relationship.getSourceIndex();
                        FragmentList<ActiveRelationship> otherRelationships = analyser.getRelationshipsTo(sourceIndex,arcrole);
                        assertEquals(0,otherRelationships.getLength());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }        
 
    public void testRelationshipRetrievalByArcrolesAndLinkRole() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> linkRoles = analyser.getLinkRoles();
            for (URI linkRole: linkRoles) {
                Set<URI> arcroles = analyser.getArcroles(linkRole);
                assertTrue(arcroles.size() > 0);
                for (URI arcrole: arcroles) {
                    Set<URI> arcroleSet = new TreeSet<URI>();
                    arcroleSet.add(arcrole);
                    FragmentList<ActiveRelationship> relationships = analyser.getRelationships(linkRole,arcroleSet);
                    assertTrue(relationships.size() > 0);
                    for (ActiveRelationship relationship: relationships) {
                        assertEquals(relationship.getArcrole(),arcrole);
                        assertEquals(relationship.getLinkRole(),linkRole);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    public void testLabelRelationshipRetrievalByArcroleAndSourceIndex() { 
        try {
            storeNetworks();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> arcroles = analyser.getArcroles();
            assertTrue(arcroles.size() > 0);
            for (URI arcrole: arcroles) {
                FragmentList<ActiveRelationship> relationships = analyser.getRelationships(arcrole);
                assertTrue(relationships.size() > 0);
                for (ActiveRelationship relationship: relationships) {
                    String sourceIndex = relationship.getSourceIndex();
                    FragmentList<ActiveRelationship> labelRelationships = analyser.getLabelRelationships(sourceIndex);
                    for (ActiveRelationship r: labelRelationships) {
                        assertEquals("label", r.getTargetName());
                        assertTrue(r.isToLabel());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }    
}
