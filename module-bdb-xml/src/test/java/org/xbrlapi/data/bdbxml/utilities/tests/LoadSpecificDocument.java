package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;

/**
 * Use this unit test to analyse 
 * and report on the state of the
 * data in a data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LoadSpecificDocument extends BaseTestCase {
    
    public LoadSpecificDocument(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        // Close but DO NOT delete the data stores.
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
    }   
    
    public void testLoadSpecificDocument() {
        try {
           
            URI uri = new URI("http://www.sec.gov/Archives/edgar/data/796343/000079634309000021/adbe-20090227.xsd"); 
            
            if (store.hasDocument(uri)) 
                store.deleteDocument(uri);
            
            loader.discover(uri);

        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
