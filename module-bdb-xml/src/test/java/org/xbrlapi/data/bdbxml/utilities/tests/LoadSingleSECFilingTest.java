package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;
import org.xbrlapi.loader.Loader;

/**
 * Use this unit test to load the first filing in the SEC database.
 * This is useful if you want to examine a small data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 *
 */
public class LoadSingleSECFilingTest extends BaseTestCase {
    
    public LoadSingleSECFilingTest(String arg0) {
        super(arg0);
    }

    private List<URI> resources = new Vector<URI>();
    
    Loader secondLoader = null;
    
    protected void setUp() throws Exception {
        super.setUp();

        logger.info("Getting the SEC feed.");
        URI feedURI = this.getURI("real.data.sec");
        Grabber grabber = new SecGrabberImpl(feedURI);
        resources = grabber.getResources();
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
    
    public void testLoadingSingleSECFiling() {
        try {   

            loader.discover(resources.get(0));
         
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
