/**
 * Commandline example showing:
 * 1. How to load all of the filings published by the SEC.
 */
package org.xbrlapi.data.bdbxml.examples.load;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.bdbxml.StoreImpl;
import org.xbrlapi.grabber.Grabber;
import org.xbrlapi.grabber.SecGrabberImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.loader.discoverer.Discoverer;
import org.xbrlapi.networks.Storer;
import org.xbrlapi.networks.StorerImpl;
import org.xbrlapi.sax.EntityResolverImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;
import org.xml.sax.EntityResolver;

/**
 *  This example loads all of the data identified in the 
 *  SEC RSS feed.  It does so using a series of threads
 *  the number of which can be set at the command line.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LoadAllSECFilings {

    private static Store store = null;

    /**
     * @param args The array of commandline arguments.
     */
    public static void main(String[] args) {

        try {
            
            // Process command line arguments
            HashMap<String,String> arguments = new HashMap<String,String>();
            int i = 0;
            if (i >= args.length)
                badUsage("No map of taxonomies has been specified");
            while (true) {
                if (i == args.length)
                    break;
                else if (args[i].charAt(0) == '-') {
                    if (args[i].equals("-database")) {
                        i++;
                        arguments.put("database", args[i]);
                    } else if (args[i].equals("-container")) {
                        i++;
                        arguments.put("container", args[i]);
                    } else if (args[i].equals("-threads")) {
                        i++;
                        arguments.put("threads", args[i]);
                    } else if (args[i].equals("-cache")) {
                        i++;
                        arguments.put("cache", args[i]);
                    } else
                        badUsage("Unknown argument: " + args[i]);
                } else {
                    ;// Ignore the parameter
                }
                i++;
            }

            if (! arguments.containsKey("database")) badUsage("You need to specify the database directory.");
            if (! arguments.containsKey("container")) badUsage("You need to specify the database container name.");
            if (! arguments.containsKey("cache")) badUsage("You need to specify the root of the document cache.");
            
            // Make sure that the taxonomy cache exists
            try {
                File cache = new File(arguments.get("cache"));
                if (!cache.exists()) badUsage("The document cache directory does not exist. " + cache.toString());
            } catch (Exception e) {
                badUsage("There are problems with the cache location: " + arguments.get("cache"));
            }
            
            // Set up the data store to load the data
            store = createStore(arguments.get("database"),arguments.get("container"));
            
            // Get the list of URIs to load from the SEC RSS feed.
            Grabber grabber = new SecGrabberImpl(new URI("http://www.sec.gov/Archives/edgar/xbrlrss.xml"));
            List<URI> resources = grabber.getResources();
            
            // Default to using 2 threads.
            int threadCount = 2;
            if (arguments.containsKey("threads")) threadCount = (new Integer(arguments.get("threads")).intValue());
            int gap = new Double(Math.floor(resources.size()/threadCount)).intValue();
            System.out.println("# of URIs per thread = " + gap + " given # URIs = " + resources.size() + " and #threads = " + threadCount);
            List<Thread> threads = new Vector<Thread>();
            for (int counter=0; counter<threadCount; counter++) {
                Loader loader =createLoader(store,arguments.get("cache")); 
                if (counter == threadCount-1) {
                    System.out.println("Thread " + (counter+1) + " gets documents " + (counter*gap) + " to " + (resources.size()-1));
                    loader.stashURIs(resources.subList(counter*gap,resources.size()-1));
                } else {
                    System.out.println("Thread " + (counter+1) + " gets documents " + (counter*gap) + " to " + ((counter+1)*gap-1));
                    loader.stashURIs(resources.subList(counter*gap,(counter+1)*gap-1));
                }
                Discoverer discoverer = new Discoverer(loader);
                Thread thread = new Thread(discoverer);
                threads.add(thread);
                thread.start();
            }
            
            // Wait till data loading is done.
            boolean stillGoing = true;
            while (stillGoing) {
                Thread.sleep(5000);
                stillGoing = false;
                for (Thread thread: threads) {
                    if (thread.isAlive()) stillGoing = true;
                }
            }

            Storer storer = new StorerImpl(store);
            storer.storeAllRelationships();
            
            // Clean up the data store and exit
            cleanup(store);

        } catch (Exception e) {
            badUsage(e.getMessage());
        }
        
    }
    
    /**
     * Report incorrect usage of the command line, with a list of the arguments
     * @param message The error message describing why the command line usage failed.
     */
    private static void badUsage(String message) {
        if (!"".equals(message)) {
            System.err.println(message);
        }
        
        System.err.println("Command line usage: java org.xbrlapi.bdbxml.examples.load.LoadAllSECFilings -database VALUE -container VALUE -cache VALUE");
        System.err.println("Mandatory arguments: ");
        System.err.println(" -database VALUE   directory containing the Oracle BDB XML database");
        System.err.println(" -container VALUE  name of the data container");
        System.err.println(" -cache VALUE      directory that is the root of the document cache");
        System.err.println("Optional arguments: ");
        System.err.println(" -threads VALUE    the number of threads to use when loading the data");

    }
    
    /**
     * Create an Oracle Berkeley XML database store and return it
     * cast to an XBRL data store to expose XBRL store enhancements.
     * @param database The location to use for the new store.
     * @param container The name to use for the XML container.
     * @return the new store.
     * @throws XBRLException if the store cannot be initialised.
     */
    private static Store createStore(String database, String container) throws XBRLException {
        return new StoreImpl(database,container);
    }
    
    
    /**
     * @param store The store to use for the loader.
     * @param cache The root directory of the document cache.
     * @return the loader to use for loading the instance and its DTS
     * @throws XBRLException if the loader cannot be initialised.
     */
    private static Loader createLoader(Store store, String cache) throws XBRLException {
        XBRLXLinkHandlerImpl xlinkHandler = new XBRLXLinkHandlerImpl();
        XBRLCustomLinkRecogniserImpl clr = new XBRLCustomLinkRecogniserImpl(); 
        XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(xlinkHandler ,clr);
        
        File cacheFile = new File(cache);
        
        // Rivet errors in the SEC XBRL data require these URI remappings to prevent discovery process from breaking.
        HashMap<URI,URI> map = new HashMap<URI,URI>();
        try {
            map.put(new URI("http://www.xbrl.org/2003/linkbase/xbrl-instance-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/instance/xbrl-instance-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/linkbase/xbrl-linkbase-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xbrl-linkbase-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/instance/xbrl-linkbase-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xbrl-linkbase-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/instance/xl-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xl-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/linkbase/xl-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xl-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/instance/xlink-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xlink-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/linkbase/xlink-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xlink-2003-12-31.xsd"));
        } catch (URISyntaxException e) {
            throw new XBRLException("URI syntax exception",e);
        }
        EntityResolver entityResolver = new EntityResolverImpl(cacheFile,map);      
        
        Loader myLoader = new LoaderImpl(store,xlinkProcessor);
        myLoader.setCache(new CacheImpl(cacheFile));
        myLoader.setEntityResolver(entityResolver);
        xlinkHandler.setLoader(myLoader);
        return myLoader;
    }
    
    /**
     * Helper method to clean up and shut down the data store.
     * @param store the store for the XBRL data.
     * @throws XBRLException if the store cannot be closed. 
     */
    private static void cleanup(Store store) throws XBRLException {
        store.close();
    }    

}
