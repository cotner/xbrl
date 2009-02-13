package org.xbrlapi.data.bdbxml.volume.tests;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;

/**
 * Use this unit test to load the entire SEC database.
 * @author Geoff Shuetrim (geoff@galexy.net)
 *
 */
public class LoadEntireSECDatabaseTest extends BaseTestCase {
    
    public LoadEntireSECDatabaseTest(String arg0) {
        super(arg0);
    }

    private List<URI> resources = new Vector<URI>();

    protected void setUp() throws Exception {
        super.setUp();
        logger.info("Getting the SEC feed.");
        URI feedURI = this.getURI("real.data.sec");
        Grabber grabber = new SecGrabberImpl(feedURI);
        resources = grabber.getResources();
        assertTrue(resources.size() > 100);
        feedURI = this.getURI("test.data.local.sec");
        Grabber oldGrabber = new SecGrabberImpl(feedURI);
        resources.addAll(oldGrabber.getResources());
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
