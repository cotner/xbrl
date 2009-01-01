package org.xbrlapi.loader.discoverer.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.data.dom.tests.BaseTestCase;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;
import org.xbrlapi.loader.discoverer.DiscoveryManager;

public class SecAsyncGrabberImplTest extends BaseTestCase {
    
	public SecAsyncGrabberImplTest(String arg0) {
        super(arg0);
    }

    private List<URI> resources = null;
    protected void setUp() throws Exception {
        super.setUp();
        URI feedURI = new URI(getURI("test.data.local.sec"));             
        Grabber grabber = new SecGrabberImpl(feedURI);
        resources = grabber.getResources();
        assertTrue(resources.size() > 1900);
    }

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public void testSecGrabberResourceRetrieval() {
		try {

			int cnt = 2;
            List<URI> r1 = resources.subList(0,cnt);
			DiscoveryManager d1 = new DiscoveryManager(loader, r1);
			Thread t1 = new Thread(d1);
            t1.start();

            List<URI> r2 = resources.subList(cnt,2*cnt);
            DiscoveryManager d2 = new DiscoveryManager(loader, r2);
            Thread t2 = new Thread(d2);
            t2.start();

            while (t1.isAlive() || t2.isAlive()) {
                Thread.sleep(20000);
                loader.requestInterrupt();
            }
            
            logger.info("Discovery was interrupted.");
            
            //loader.discover();
            
		} catch (Exception e) {
			fail("An unexpected exception was thrown.");
		}
	}
	

	
}
