package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Stub;
import org.xbrlapi.data.bdbxml.tests.BaseTestCase;

/**
 * Use this unit test to analyse 
 * and report on the state of the
 * data in a data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DeleteObsoleteStub extends BaseTestCase {
    
    public DeleteObsoleteStub(String arg0) {
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
    
    public void testDataStoreState() {
        try {
            
            URI uri = new URI("http://www.sec.gov/Archives/edgar/data/789019/000119312508089475/xbrl-instance-2003-12-31.xsd");
            List<Stub> stubs = store.getStubs();
            for (Stub stub: stubs) {
                if (stub.getResourceURI().equals(uri)) {
                    store.remove(stub);
                    logger.info("Removed stub for " + stub.getResourceURI());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
