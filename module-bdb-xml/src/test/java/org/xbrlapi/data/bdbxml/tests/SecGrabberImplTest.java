package org.xbrlapi.data.bdbxml.tests;

import java.net.URL;
import java.util.List;

import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;

public class SecGrabberImplTest extends BaseTestCase {
    
    public SecGrabberImplTest(String arg0) {
        super(arg0);
    }

    private List<URL> resources = null;
    protected void setUp() throws Exception {
        super.setUp();
        logger.info("Getting the SEC feed.");
        String secFeed = configuration.getProperty("real.data.sec");
        URL feedUrl = new URL(secFeed);
        Grabber grabber = new SecGrabberImpl(feedUrl);
        resources = grabber.getResources();
        assertTrue(resources.size() > 1900);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }   
    
    public void testSecGrabberResourceRetrieval() {
        try {

            int cnt = 3;
            List<URL> r1 = resources.subList(0,cnt);
            logger.info("About to discover SEC data.");
            loader.discover(r1);

        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    

    
}
