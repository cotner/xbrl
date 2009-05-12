package org.xbrlapi.data.bdbxml.volume.tests;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;
import org.xbrlapi.networks.AnalyserImpl;

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

        // Make the store persist networks
        store.setAnalyser(new AnalyserImpl(store));
        
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
        // Close but DO NOT delete the data stores.
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
    }   
    
    public void testSecGrabberResourceRetrieval() {
        try {
            
            loader.stashURI(new URI("http://www.sec.gov/Archives/edgar/data/796343/000079634309000021/adbe-20090227.xml"));
            loader.discover();
            
            for (URI uri: resources) {
                loader.stashURI(uri);
                loader.discover();
            }

        } catch (Exception e) {
            logger.error("Trapped exception: " + e.getMessage());
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
