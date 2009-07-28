package org.xbrlapi.relationships.tests;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.xbrlapi.Arc;
import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Relationship;
import org.xbrlapi.networks.Analyser;
import org.xbrlapi.networks.AnalyserImpl;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.Storer;
import org.xbrlapi.networks.StorerImpl;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class PersistedNetworksTestCase extends DOMLoadingTestCase {

	Networks networks = null;
	LabelResource label = null;
	Concept concept = null;

	URI uri;
	
	protected void setUp() throws Exception {
		super.setUp();
		uri = getURI("test.data.xlink.titles");
		loader.discover(uri);
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
        networks = store.getNetworks();
        assertTrue(networks.getSize() > 0);
        initialSize = store.getSize();
        logger.info("Initial size of store = " + initialSize);
        storer.storeRelationships(networks);
	}
	
    /**
     * Test persisting of active relationships for all networks.
     */
    public void testPersistingOfAllActiveRelationships() { 
        try {
            storeNetworks();
            int augmentedSize = store.getSize();
            logger.info("Augmented size = " + augmentedSize);
            assertTrue(augmentedSize > initialSize);
            storer.deleteRelationships();
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
                List<Relationship> relationships = analyser.getRelationships(arcrole);
                if (relationships.size() == 0) {
                    logger.info(arcrole);
                }
                assertTrue(relationships.size() > 0);
                for (Relationship relationship: relationships) {
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
                    List<Relationship> relationships = analyser.getRelationships(linkRole,arcrole);
                    assertTrue(relationships.size() > 0);
                    for (Relationship relationship: relationships) {
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
                List<Relationship> relationships = analyser.getRelationships(arcrole);
                assertTrue(relationships.size() > 0);
                for (Relationship relationship: relationships) {
                    String sourceIndex = relationship.getSourceIndex();
                    List<Relationship> otherRelationships = analyser.getRelationshipsFrom(sourceIndex,arcrole);
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
                List<Relationship> relationships = analyser.getRelationships(arcrole);
                assertTrue(relationships.size() > 0);
                for (Relationship relationship: relationships) {
                    String targetIndex = relationship.getTargetIndex();
                    List<Relationship> otherRelationships = analyser.getRelationshipsTo(targetIndex,arcrole);
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
                    List<Relationship> relationships = analyser.getRootRelationships(linkRole, arcrole);
                    assertTrue(relationships.size() > 0);
                    for (Relationship relationship: relationships) {
                        String sourceIndex = relationship.getSourceIndex();
                        List<Relationship> otherRelationships = analyser.getRelationshipsTo(sourceIndex,arcrole);
                        assertEquals(0,otherRelationships.size());
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
                    List<Relationship> relationships = analyser.getRelationships(linkRole,arcroleSet);
                    assertTrue(relationships.size() > 0);
                    for (Relationship relationship: relationships) {
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
                List<Relationship> relationships = analyser.getRelationships(arcrole);
                assertTrue(relationships.size() > 0);
                for (Relationship relationship: relationships) {
                    String sourceIndex = relationship.getSourceIndex();
                    List<Relationship> labelRelationships = analyser.getLabelRelationships(sourceIndex);
                    for (Relationship r: labelRelationships) {
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
    
    public void testDocumentHasPersistedRelationships() { 
        try {
            Storer storer = new StorerImpl(store);
            storer.deleteRelationships();
            Analyser analyser = new AnalyserImpl(store);
            Set<URI> uris = store.getDocumentURIs();
            for (URI uri: uris) {
                if (store.<Arc>getFragmentsFromDocument(uri,"Arc").size() > 0) {
                    assertFalse("Not persisted relationships for " + uri.toString(),analyser.hasAllRelationships(uri));
                }
            }
            storer.storeAllRelationships();
            for (URI uri: uris) {
                if (store.<Arc>getXMLResources("Arc").size() > 0) {
                    assertTrue("Not persisted relationships for " + uri.toString(),analyser.hasAllRelationships(uri));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }    
}
