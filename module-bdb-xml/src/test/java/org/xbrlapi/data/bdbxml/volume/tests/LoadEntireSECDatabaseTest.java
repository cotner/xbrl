package org.xbrlapi.data.bdbxml.volume.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;

/**
 * Use this unit test to load the entire SEC database.
 * @author Geoff Shuetrim (geoff@galexy.net)
 *
 */
public abstract class LoadEntireSECDatabaseTest extends BaseTestCase {
    
    public LoadEntireSECDatabaseTest(String arg0) {
        super(arg0);
    }

    private List<URI> resources = null;
    protected void setUp() throws Exception {
        super.setUp();
        logger.info("Getting the SEC feed.");
        String secFeed = configuration.getProperty("real.data.sec");
        URI feedURI = new URI(secFeed);
        Grabber grabber = new SecGrabberImpl(feedURI);
        resources = grabber.getResources();
        assertTrue(resources.size() > 100);
        secFeed = configuration.getProperty("test.data.local.sec");
        feedURI = new URI(secFeed);
        grabber = new SecGrabberImpl(feedURI);
        resources.addAll(grabber.getResources());
    }

    protected void tearDown() throws Exception {
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
    }   
    
    public void testSecGrabberResourceRetrieval() {
        try {

            for (URI uri: resources) {
                loader.stashURI(uri);
            }
            loader.discover();

        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
