package org.xbrlapi.data.bdbxml.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.data.bdbxml.BaseTestCase;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;

public class SecGrabberImplTest extends BaseTestCase {
    
    public SecGrabberImplTest(String arg0) {
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
        assertTrue(resources.size() > 50);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }   
    
    public void testSecGrabberResourceRetrieval() {
        try {

            int cnt = 3;
            List<URI> r1 = resources.subList(0,cnt);
            logger.info("About to discover SEC data.");
            for (URI uri: r1) {
                loader.stashURI(uri);
            }
            loader.discoverNext();
            loader.discoverNext();
            loader.discoverNext();

        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    

    
}
