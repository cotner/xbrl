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
            logger.error("The discoverer could not be instantiated.");
        }
    }

    public void run() {
        try {
            if (loader == null) {
                logger.error("Discovery failed because the discoverer does not have a loader.");
            } else {
                logger.info("Starting the discovery process in the separate thread.");
                loader.discover();
            }
        } catch (XBRLException e) {
            e.printStackTrace();
            logger.error("Error doing the data discovery.");
        }
    }

}
