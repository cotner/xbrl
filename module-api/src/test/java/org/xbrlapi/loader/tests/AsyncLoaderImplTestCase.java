package org.xbrlapi.loader.tests;

import java.net.URI;

import org.xbrlapi.data.dom.tests.BaseTestCase;
import org.xbrlapi.loader.discoverer.Discoverer;

/**
 * Test the loader implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class AsyncLoaderImplTestCase extends BaseTestCase {
	
	private final String STARTING_POINT = "test.data.small.schema";
	private final String STARTING_POINT_2 = "test.data.small.instance";
    private final String STARTING_POINT_3 = "real.data.xbrl.2.1.roles";	
	private URI uri1 = null;
	private URI uri2 = null;
    private URI uri3 = null;	
	
	protected void setUp() throws Exception {
		super.setUp();
		uri1 = getURI(this.STARTING_POINT);
		uri2 = getURI(this.STARTING_POINT_2);
        uri3 = getURI(this.STARTING_POINT_3);
	}
	
	public AsyncLoaderImplTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test discovery given an XBRL instance as a starting point.
	 */
	public void testInterruption() {
	    try {
	        loader.stashURI(this.uri3);
            loader.stashURI(this.uri1);
            loader.stashURI(this.uri2);
            
            Discoverer d1 = new Discoverer(loader);
            Thread t1 = new Thread(d1);
            t1.start();

            Thread.sleep(200);
            loader.requestInterrupt();
            
            while (t1.isAlive()) {
                Thread.sleep(100);
            }
            loader.storeDocumentsToAnalyse();
            logger.info("# stored URIs = " + store.getStoredURIs().size());
            assertTrue(store.getStoredURIs().size() < 14);
            assertTrue(store.getDocumentsToDiscover().size() > 0);
			loader.discover();
            logger.info("# stored URIs = " + store.getStoredURIs().size());
            for (URI uri: store.getStoredURIs()) {
                logger.info(uri);
            }
			assertTrue(store.getStoredURIs().size() > 14);
		} catch (Exception e) {
		    e.printStackTrace();
			fail("Unexpected " + e.getMessage());
		}
	}
	
}
