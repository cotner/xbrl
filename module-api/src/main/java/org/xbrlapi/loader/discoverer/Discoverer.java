package org.xbrlapi.loader.discoverer;

import org.apache.log4j.Logger;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

public class Discoverer implements Runnable {

    static Logger logger = Logger.getLogger(Discoverer.class);
    
    Loader loader = null;
    
    public Discoverer(Loader loader) {
        try {
            if (loader == null) throw new XBRLException("The loader is null.");
            this.loader = loader;
        } catch (XBRLException e) {
            e.printStackTrace();
            logger.error(Thread.currentThread().getName() + ": The discoverer could not be instantiated.");
        }
    }

    public void run() {
        try {
            logger.info(Thread.currentThread().getName() + ": Successfully began.");
            if (loader == null) {
                logger.error(Thread.currentThread().getName() + ": Discovery failed because the discoverer does not have a loader.");
            } else {
                logger.info(Thread.currentThread().getName() + ": Starting the discovery process in the separate thread.");
                loader.discover();
            }
            logger.info(Thread.currentThread().getName() + ": Successfully exited.");
        } catch (XBRLException e) {
            e.printStackTrace();
            logger.error(Thread.currentThread().getName() + ": Error doing the data discovery.");
        }
    }

}
