package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;

/**
 * Use this unit test to analyse 
 * and report on the state of the
 * data in a data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class FindLinksToGivenURI extends BaseTestCase {

    public FindLinksToGivenURI(String arg0) {
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
    
    public void testFindDocumentsLinkingToGivenURI() {
        try {

            URI uri = new URI("http://www.sec.gov/Archives/edgar/data/30554/000095013508003083/dd-20071231.xsd");
            uri = new URI("http://www.sec.gov/Archives/edgar/data/30554/000095013508003083/dd-20071231_def.xml");

            List<URI> referencingDocuments = store.getReferencingDocuments(uri);

            for (URI ref: referencingDocuments) {
                logger.info(uri + " is referenced by " + ref);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
