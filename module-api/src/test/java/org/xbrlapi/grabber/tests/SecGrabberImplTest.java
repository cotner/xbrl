package org.xbrlapi.grabber.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.data.dom.tests.BaseTestCase;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;

public class SecGrabberImplTest extends BaseTestCase {

    public SecGrabberImplTest(String arg0) {
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

			long start = System.currentTimeMillis();
			for (URI resource: resources) {
				if (! loader.getStore().hasDocument(resource.toString()))
				loader.discover(resource);
				System.out.println("Time taken = " + ((System.currentTimeMillis() - start) / 1000));
				if (loader.getStore().getStoredURIs().size() > 14) {
				    break;
				}
			}
			System.out.println("All done and dusted!");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("An unexpected exception was thrown.");
		}
	}	
	
}
