package org.xbrlapi.data.bdbxml.examples.utilities;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.xbrlapi.cache.Cache;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.bdbxml.StoreImpl;
import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.utilities.EntityResolver;
import org.xbrlapi.utilities.EntityResolverImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;

/**
 * Provides a base class that all of the concrete utility examples extend.
 * Commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>-database [Path to directory containing the database]</li>
 *  <li>-container [Name of the database container]</li>
 *  <li>-cache [Path to directory containing the XBRL cache]</li>
 * </ul>
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public abstract class BaseUtilityExample {

    /**
     * Responsible for parsing the command line arguments into a hashmap
     * that maps from the text string that is the argument name (prefixed by a hyphen
     * that is not part of the string used in the mapping) to the argument value,
     * represented as a text string.
     * @param args
     */
    protected void parseArguments(String[] args) {
        try {
            for (int i=0; i<args.length-1; i=i+2) {
                if (args[i].charAt(0) != '-') {
                    problems += "Each argument name must be followed by its value using space separation.\n";
                }
                mapArgument(args[i].substring(1),args[i+1]);
            }
        } catch (Exception e) {
            badUsage("There is a problem with the command line arguments.");
        }
    }

    protected static Logger logger = Logger.getLogger(BaseUtilityExample.class);  
	protected Cache cache = null;
	protected Store store = null;
	protected Loader loader = null;
	
    // The list of commandline arguments
    protected HashMap<String,String> arguments = new HashMap<String,String>();
    protected String argumentDocumentation = "";
    protected String usage = "java " + this.getClass().getName() + " <ARGUMENTS>";
    protected String problems = "";

    /**
     * Adds an argument to the argument map.
     * @param name The argument name (including the leading hyphen).
     * @param value The argument value.
     */
    protected void mapArgument(String name, String value) {
        arguments.put(name, value);
    }
    
    /**
     * Sets up the database store, the loader and the cache.
     * Also checks that the base arguments (database, container and cache)
     * have been supplied. (Overrides should also call this method).
     */
    protected String setUp() {
        String message = "";
        if (!arguments.containsKey("database")) message += "The database directory is not specified.\n";
        if (!arguments.containsKey("container")) message += "The database container is not specified.\n";
        if (!arguments.containsKey("cache")) message += "The document cache is not specified.\n";
        if (message.equals("")) {
            try {
                File cacheFile = new File(arguments.get("cache"));
                this.cache = new CacheImpl(cacheFile);
                store = new StoreImpl(arguments.get("database"), arguments.get("container"));
                store.setMatcher(new InStoreMatcherImpl(store,cache));
                XBRLXLinkHandlerImpl xlinkHandler = new XBRLXLinkHandlerImpl();
                XBRLCustomLinkRecogniserImpl clr = new XBRLCustomLinkRecogniserImpl(); 
                XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(xlinkHandler ,clr);
                EntityResolver entityResolver = new EntityResolverImpl(cacheFile);      
                loader = new LoaderImpl(store,xlinkProcessor);
                loader.setCache(cache);
                loader.setEntityResolver(entityResolver);
                xlinkHandler.setLoader(loader);                
            } catch (XBRLException e) {
                message += e.getMessage() + "\n";
            }
        }
        return message;

    }
    
    /**
     * Closes the data store. Overrides should also call this method.
     */
    protected void tearDown() {
        try {
            store.close();
        } catch (Exception e) {
            badUsage(e.getMessage());
        }
    }

    /**
     * Report incorrect usage of the utility.
     * @param message The error message.
     */
    protected void badUsage(String message) {
        if (!"".equals(message)) {
            System.err.println(message);
            System.err.print(problems);
        }
        System.err.println("Usage: " + usage);
        System.err.println(argumentDocumentation);
    }    
    
    /**
     * Provides documentation of the usage of the utility.
     * Override this to generate the utility documentation, calling
     * this first to get the initial part of the documentation.
     * @return the documentation of the command line arguments.
     */
    protected String addArgumentDocumentation() {
        String explanation = "You MUST use the Oracle Berkeley XML data store with this example.\n";
        explanation += "Arguments (optional ones marked with an *):\n";
        explanation += "-database\t\tPath to directory containing the database\n";
        explanation += "-container\t\tName of the database container\n";
        explanation += "-cache\t\t\tPath to directory containing the XBRL cache\n";
        return explanation;
    }    

}