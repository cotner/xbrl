package org.xbrlapi.loader.discoverer;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

/**
 * The discovery manager is responsible for managing 
 * the discovery process in a manner that interrupts the
 * discovery thread every so often (based upon a default
 * or user specified time interval before the interrupt is
 * sent to the loader).  If this kind of interruption is not
 * useful, then the Discoverer may be more useful.
 * @author Geoff Shuetrim (geoff@galexy.net)
 * @see org.xbrlapi.loader.discoverer.Discoverer
 */
public class DiscoveryManager implements Runnable {

    static Logger logger = Logger.getLogger(DiscoveryManager.class);
    
    private Loader loader = null;
    private long interval = 600000;
    
    public DiscoveryManager(Loader loader) {
        try {
            if (loader == null) throw new XBRLException("The loader is null.");
            this.loader = loader;
        } catch (XBRLException e) {
            e.printStackTrace();
            logger.error(Thread.currentThread().getName() + ": The discoverer could not be instantiated.");
        }
    }

    public DiscoveryManager(Loader loader, long interval) {
        this(loader);
        this.interval = interval;
    }
    
    List<URI> resources = new LinkedList<URI>();
    
    /**
     * @param loader The loader to do the discovery with.
     * @param resources The URIs to discover.
     */
    public DiscoveryManager(Loader loader, List<URI> resources) {
        this(loader);
        if (resources != null) {
            this.resources = resources;
        }
    }
    
    public DiscoveryManager(Loader loader, List<URI> resources, long interval) {
        this(loader,interval);
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
                
                for (URI resource: resources) {
                    loader.stashURI(resource);
                }

                Discoverer discoverer = new Discoverer(loader);
                Thread thread = new Thread(discoverer);
                thread.start();
                
                while (thread.isAlive()) {
                    
                    int sleptTime = 0;
                    while (sleptTime < interval) {
                        Thread.sleep(1000);
                        sleptTime = sleptTime + 1000;
                        if (! thread.isAlive()) {
                            break;
                        }
                    }
                    
                    if (thread.isAlive()) {
                        loader.requestInterrupt();
                        while (thread.isAlive()) {
                            Thread.sleep(2000);
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
