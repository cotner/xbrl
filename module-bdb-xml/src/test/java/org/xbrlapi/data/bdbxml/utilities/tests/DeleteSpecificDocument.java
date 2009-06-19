package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;

/**
 * Use this unit test to analyse 
 * and report on the state of the
 * data in a data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DeleteSpecificDocument extends BaseTestCase {
    
    public DeleteSpecificDocument(String arg0) {
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
    
    public void testDeleteSpecificDocument() {
        try {
           
            URI uri = new URI("http://www.sec.gov/Archives/edgar/data/30554/000095013508003083/dd-20071231.xsd"); 
            
            store.deleteDocument(uri);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
