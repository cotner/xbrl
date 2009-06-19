package org.xbrlapi.data.bdbxml.utilities.tests;

import java.util.List;

import org.xbrlapi.Stub;
import org.xbrlapi.data.bdbxml.tests.BaseTestCase;

/**
 * Use this unit test to analyse 
 * and report on the state of the
 * data in a data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalysisOfDocumentsThatFailedToLoad extends BaseTestCase {
    
    public AnalysisOfDocumentsThatFailedToLoad(String arg0) {
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
    
    public void testAnalyseDataStoreDocumentStubs() {
        try {
           
            List<Stub> stubs = store.getStubs();
            logger.info("The data store failed to load " + stubs.size() + " documents.");

            for (Stub stub: stubs) {
                logger.info(stub.getResourceURI() + " failed to load. " + stub.getReason());
                logger.info("Store contains this document? " + store.hasDocument(stub.getResourceURI()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
