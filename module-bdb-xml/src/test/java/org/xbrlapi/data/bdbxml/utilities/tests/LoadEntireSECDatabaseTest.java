package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.discoverer.Discoverer;

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
    
    Loader secondLoader = null;
    
    protected void setUp() throws Exception {
        super.setUp();

        // Make the store persist networks
        // store.setAnalyser(new AnalyserImpl(store));
        
        logger.info("Getting the SEC feed.");
        URI feedURI = this.getURI("real.data.sec");
        Grabber grabber = new SecGrabberImpl(feedURI);
        resources = grabber.getResources();
        feedURI = this.getURI("test.data.local.sec");
        Grabber oldGrabber = new SecGrabberImpl(feedURI);
        resources.addAll(oldGrabber.getResources());
        
        secondLoader = createLoader(store);
    }

    protected void tearDown() throws Exception {
        // Close but DO NOT delete the data stores.
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
    }   
    
    public void testLoadingAllSECFilings() {
        try {   

            int breakPoint = new Double(Math.floor(resources.size()/2)).intValue();
            loader.stashURIs(resources.subList(0,breakPoint));
            secondLoader.stashURIs(resources.subList(breakPoint+1,resources.size()-1));
            Discoverer discoverer = new Discoverer(loader);
            Discoverer secondDiscoverer = new Discoverer(secondLoader);
            Thread t1 = new Thread(discoverer);
            t1.start();
            Thread t2 = new Thread(secondDiscoverer);
            t2.start();

            while (t1.isAlive() || t2.isAlive()) {
                Thread.sleep(5000);
            }
            
         
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
