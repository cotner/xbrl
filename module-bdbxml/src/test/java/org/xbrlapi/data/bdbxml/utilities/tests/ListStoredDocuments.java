package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;

/**
 * Use this unit test to persist all relationships in the data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class ListStoredDocuments extends BaseTestCase {
    
    public ListStoredDocuments(String arg0) {
        super(arg0);
    }

    private List<URI> resources = new Vector<URI>();
    
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        // Close but DO NOT delete the data stores.
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
    }   
    
    public void testListStoredDocuments() {
        try {


            Set<URI> uris = store.getDocumentURIs();
            for (URI uri: uris) {
                logger.info(uri);
            }
            logger.info("# documents = " + uris.size());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
