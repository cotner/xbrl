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
    
    private Loader loader = null;
    private long interval = 20000;
    private String name = "";
    
    public DiscoveryManager(Loader loader) {
        try {
            if (loader == null) throw new XBRLException("The loader is null.");
            this.loader = loader;
        } catch (XBRLException e) {
            e.printStackTrace();
            logger.error(Thread.currentThread().getName() + ": The discoverer could not be instantiated.");
        }
    }

    public DiscoveryManager(Loader loader, String name) {
        this(loader);
        if (name != null) this.name = name;
    }
    
    public DiscoveryManager(Loader loader, long interval) {
        this(loader);
        this.interval = interval;
    }
    
    public DiscoveryManager(Loader loader, long interval, String name) {
        this(loader,interval);
        if (name != null) this.name = name;
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
            logger.info(Thread.currentThread().getName() + ": Successfully began.");            
            if (loader == null) {
                logger.error(Thread.currentThread().getName() + ": Discovery failed because the discoverer does not have a loader.");
            } else {
                
                for (URL resource: resources) {
                    loader.stashURL(resource);
                }                

                Store store = loader.getStore();
                Discoverer discoverer = new Discoverer(loader);
                Thread thread = new Thread(discoverer);
                thread.start();
                
                while (thread.isAlive()) {
                    Thread.sleep(interval);
                    if (thread.isAlive()) {
                        loader.requestInterrupt();
                        while (thread.isAlive()) {
                            Thread.sleep(200);
                        }
                        discoverer = new Discoverer(loader);
                        thread = new Thread(discoverer);
                        thread.start();
                    }
                }
                
            }
            logger.info(Thread.currentThread().getName() + ": Successfully exited."); 
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Thread.currentThread().getName() + ": The discovery manager experienced an Exception.");
        }
    }

}
