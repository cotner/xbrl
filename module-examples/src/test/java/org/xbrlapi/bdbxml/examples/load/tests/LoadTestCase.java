package org.xbrlapi.bdbxml.examples.load.tests;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.bdbxml.examples.load.Load;
import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests @link org.xbrlapi.bdbxml.examples.load.Load
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LoadTestCase extends BaseTestCase {

    private final String DOCUMENT_URI = "test.data.small.schema";
    
    // Create the logger
    protected static Logger logger = Logger.getLogger(LoadTestCase.class);  

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public LoadTestCase(String arg0) {
		super(arg0);
	}

    public void testSuccessfulLoadOfSingleDocument() {

        URI uri = getURI(DOCUMENT_URI);

        logger.info("The URI to load is " + uri);
        
        try {
            assertFalse(store.hasDocument(uri));
        } catch (XBRLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        String[] args = {
                "-database", location, 
                "-container",containerName, 
                "-cache", cachePath, 
                uri.toString() 
                };
        
        logger.info("Starting the load process.");
        Load.main(args);

        try {
            logger.info("Checking if the store has " + uri);
            assertTrue(store.hasDocument(uri));
        } catch (XBRLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    public void testLoadOfSingleDocumentFailingBecauseDatabaseParameterIsMissing() {

        URI uri = getURI(DOCUMENT_URI);

        logger.info("The URI to load is " + uri);
        
        try {
            assertFalse(store.hasDocument(uri));
        } catch (XBRLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        String[] args = {
                "-container",containerName, 
                "-cache", cachePath, 
                uri.toString() 
                };
        
        logger.info("Starting the load process.");
        Load.main(args);

        try {
            logger.info("Checking if the store has " + uri);
            assertFalse(store.hasDocument(uri));
        } catch (XBRLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }    
}