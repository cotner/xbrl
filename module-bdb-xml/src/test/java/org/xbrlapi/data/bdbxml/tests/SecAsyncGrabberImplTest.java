package org.xbrlapi.data.bdbxml.tests;

import java.net.URL;
import java.util.List;

import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;
import org.xbrlapi.loader.discoverer.DiscoveryManager;

public class SecAsyncGrabberImplTest extends BaseTestCase {
    
    public SecAsyncGrabberImplTest(String arg0) {
        super(arg0);
    }

    private List<URL> resources = null;
    protected void setUp() throws Exception {
        super.setUp();
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

            int cnt = 20;
            List<URL> r1 = resources.subList(0,cnt);
            DiscoveryManager d1 = new DiscoveryManager(loader, r1);
            Thread t1 = new Thread(d1);
            t1.start();

            while (t1.isAlive()) {
                Thread.sleep(2000);
            }
            
            logger.info("Discovery was interrupted.");
            
        } catch (Exception e) {
            fail("An unexpected exception was thrown.");
        }
    }
    

    
}
