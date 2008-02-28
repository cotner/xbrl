package org.xbrlapi.data.exist;

/**
 * Provides a main method for loading data into a Exist data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

import org.xbrlapi.EntryPoint;
import org.xbrlapi.SAXHandlers.EntityResolverImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;

public class Load  implements EntryPoint {

	/**
	 * Entry point for execution.
	 * @param args The white-space delimited list of arguments.
	 */
	public void run(String[] args) {
		try {
			
			// HashMap to store arguments
			HashMap<String,String> arguments = new HashMap<String,String>();
			
			arguments.put("host", "tomcat");			
			arguments.put("port", "80");			
			arguments.put("path", "exist/xmlrpc/db");			
			arguments.put("container", "/");			
			arguments.put("collection", "store");
			arguments.put("user", "admin");			
			arguments.put("password", "");	
			
			// List of URLs of documents to load
			LinkedList<URL> startingURLs = new LinkedList<URL>();
			
			// Process command line arguments
			int i = 0;
			if (i >= args.length)
				badUsage("At least one URL must be specified to seed the data discovery process.");

			while (true) {

				if (i == args.length)
					break;

				else if (args[i].charAt(0) == '-') {

					if (args[i].equals("-host")) {
						i++;
						if (args[i].equals("none")) arguments.put("host", "");
						else arguments.put("host", args[i]);
	
					} else if (args[i].equals("-port")) {
						i++;
						if (args[i].equals("none")) arguments.put("port", "");
						else arguments.put("port", args[i]);
						
					} else if (args[i].equals("-path")) {
						i++;
						arguments.put("path", args[i]);

					} else if (args[i].equals("-user")) {
						i++;
						arguments.put("user", args[i]);
						
					} else if (args[i].equals("-password")) {
						i++;
						if (! args[i].equals("none")) arguments.put("password", args[i]);
				
					} else if (args[i].equals("-container")) {
						i++;
						arguments.put("container", args[i]);

					} else if (args[i].equals("-collection")) {
						i++;
						arguments.put("collection", args[i]);

					} else if (args[i].equals("-cache")) {
						i++;
						arguments.put("cache", args[i]);
						
					} else if (args[i].equals("-class")) {
						i++;

					} else if (args[i].equals("-logger")) {
						i++;
						
					} else
						badUsage("Unknown option " + args[i]);

				} else {
					try {
						startingURLs.add(new URL(args[i]));
					} catch (MalformedURLException e) {
						badUsage(args[i] + " is a malformed URL.");
					}
				}
				i++;
			}
	
			// Create a new database connection
			DBConnectionImpl connection = new DBConnectionImpl(
					arguments.get("host")
					,arguments.get("port")
					,arguments.get("path")
					,arguments.get("user")
					,arguments.get("password")
					);

			// Create a new data store for the DTS
			Store store = new StoreImpl(
					connection,
					arguments.get("container"),
					arguments.get("collection")
					);

			// Create a loader
			// Create an XLink processor for XBRL DTS discovery
			XBRLXLinkHandlerImpl xlinkHandler = new XBRLXLinkHandlerImpl(); 
			XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(xlinkHandler, new XBRLCustomLinkRecogniserImpl());
			Loader loader = new LoaderImpl(store,xlinkProcessor,startingURLs);
			xlinkHandler.setLoader(loader);
			
			// Set the entity resolver for the loader to use the specified caching folder
			if (arguments.containsKey("cache")) {
				loader.setEntityResolver(new EntityResolverImpl(new File(arguments.get("cache"))));
			}
			
			loader.discover();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	/**
	 * Report incorrect usage of the command line, with a list of the options
	 * and arguments that are available
	 * 
	 * @param message
	 *            The error message describing why the command line usage of
	 *            the DTSImpl class failed.
	 */
	static protected void badUsage(String message) {
		if (!"".equals(message)) {
			System.err.println(message);
		}
		System.err.println("Command line usage: java org.xbrlapi.data.xindice.Load [parameters] [URLs]");
		System.err.println("Parameters: ");
		System.err.println("  -host			(mandatory) Database host domain name");
		System.err.println("  -port			(mandatory) Database host port  (provide the value 'none' if there is no host)");
		System.err.println("  -path			(mandatory) Database host path  (provide the value 'none' if there is no port)");
		System.err.println("  -user			(mandatory) Iser ID of the database user");
		System.err.println("  -password		(mandatory) Password of the database user (provide the value 'none' if there is no password)");
		System.err.println("  -container	(mandatory) Full path and name of container collection that will hold the data store collection");
		System.err.println("  -collection	(mandatory) Name of collection that will hold the data store");
		System.err.println("  -cache		(optional) Absolute path of for document cache");
		System.exit(1);
	}
}
