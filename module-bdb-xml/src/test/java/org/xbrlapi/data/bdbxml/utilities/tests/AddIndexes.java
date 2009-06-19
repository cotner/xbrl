package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.data.bdbxml.StoreImpl;
import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.loader.Loader;

/**
 * Use this unit test to persist all relationships in the data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AddIndexes extends BaseTestCase {
    
    public AddIndexes(String arg0) {
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
    
    public void testAddIndexes() {
        try {

            // Set up the relationship persistence infrastructure.
            ((StoreImpl) store).addIndex(null,"uri","node-attribute-presence");
            //((StoreImpl) store).addIndex(null,"parentIndex","node-attribute-presence");
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
