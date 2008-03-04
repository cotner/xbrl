package org.xbrlapi.loader.discoverer;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

public class DiscoveryManager implements Runnable {

    static Logger logger = Logger.getLogger(DiscoveryManager.class);
    
    Loader loader = null;
    
    public DiscoveryManager(Loader loader) {
        try {
            if (loader == null) throw new XBRLException("The loader is null.");
            this.loader = loader;
        } catch (XBRLException e) {
            e.printStackTrace();
            logger.error("The discoverer could not be instantiated.");
        }
    }


    List<URL> resources = new LinkedList<URL>();
    
    /**
     * @param loader The loader to do the discovery with.
     * @param resources The URLs to discover.
     */
    public DiscoveryManager(Loader loader, List<URL> resources) {
        this(loader);
        if (resources != null) {
            this.resources = resources;
        }
    }
    
    
    public void run() {
        try {
            if (loader == null) {
                logger.error("Discovery failed because the discoverer does not have a loader.");
            } else {
                
                for (URL resource: resources) {
                    loader.stashURL(resource);
                }                
                
                Store store = loader.getStore();
                Discoverer discoverer = new Discoverer(loader);
                Thread thread = new Thread(discoverer);
                thread.start();
                
                while (thread.isAlive()) {
                    Thread.sleep(20000);
                    loader.requestInterrupt();
                    while (thread.isAlive()) {
                        Thread.sleep(100);
                    }
                    discoverer = new Discoverer(loader);
                    thread = new Thread(discoverer);
                    thread.start();
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("The discovery manager experienced an Exception.");
        }
    }

}
