package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.networks.Storer;
import org.xbrlapi.networks.StorerImpl;

/**
 * Use this unit test to persist all relationships in the data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PersistAllRelationshipsInStore extends BaseTestCase {
    
    public PersistAllRelationshipsInStore(String arg0) {
        super(arg0);
    }

    private List<URI> resources = new Vector<URI>();
    
    Loader secondLoader = null;
    
    protected void setUp() throws Exception {
        super.setUp();

    }

    protected void tearDown() throws Exception {
        // Close but DO NOT delete the data stores.
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
    }   
    
    public void testRelationshipPersistence() {
        try {

            // Set up the relationship persistence infrastructure.
            Storer storer = new StorerImpl(store);
            
            Set<URI> uris = store.getStoredURIs();
            logger.info("# documents = " + uris.size());
            int count = 1;
            for (URI uri: uris) {
                storer.storeRelationships(uri);
                if (count > 1) break;
                count++;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
