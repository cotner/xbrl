package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.loader.Loader;

/**
 * Use this unit test to find documents that have been stored 
 * more than once in the data store and to purge them and then
 * to reload them properly.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class ListDuplicateDocumentsInStore extends BaseTestCase {
    
    public ListDuplicateDocumentsInStore(String arg0) {
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
    
    public void testReloadDuplicateDocuments() {
        try {

            String query = "for $document in #roots#[@parentIndex=''], $duplicate in #roots#[@parentIndex=''] where ($document/@uri=$duplicate/@uri and $document/@index!=$duplicate/@index) return string($document/@uri)";
            Set<String> uris = store.queryForStrings(query);
            logger.info("# duplicated documents = " + uris.size());
            for (String uri: uris) {
                logger.info(uri + " has duplicates in the data store.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
