package org.xbrlapi.data.xindice;

/**
 * Provides a main method for loading data into a Xindice data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;

import org.xbrlapi.EntryPoint;
import org.xbrlapi.data.Store;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.sax.EntityResolverImpl;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;

public class Load implements EntryPoint {

	/**
	 * Entry point for execution.
	 * @param args The white-space delimited list of arguments.
	 */
	public void run(String[] args) {
		try {
			
			// HashMap to store arguments
			HashMap<String,String> arguments = new HashMap<String,String>();
			
			// List of URIs of documents to load
			LinkedList<URI> startingURIs = new LinkedList<URI>();
			
			// Process command line arguments
			int i = 0;
			if (i >= args.length)
				badUsage("At least one URI must be specified to seed the data discovery process.");

			while (true) {

				if (i == args.length)
					break;

				else if (args[i].charAt(0) == '-') {

					if (args[i].equals("-host")) {
						i++;
						arguments.put("host", args[i]);
	
					} else if (args[i].equals("-port")) {
						i++;
						arguments.put("port", args[i]);
						
					} else if (args[i].equals("-path")) {
						i++;
						arguments.put("path", args[i]);

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
						startingURIs.add(new URI(args[i]));
					} catch (URISyntaxException e) {
						badUsage(args[i] + " is a malformed URI.");
					}
				}
				i++;
			}

			// Create a new database connection
			DBConnectionImpl connection = new DBConnectionImpl(
					arguments.get("host")
					,arguments.get("port")
					,arguments.get("path"));

			// Create a new data store for the DTS
			Store store = new StoreImpl(
					connection,
					arguments.get("container"),
					arguments.get("collection"));

			// Create a loader
			// Create an XLink processor for XBRL DTS discovery
			XBRLXLinkHandlerImpl xlinkHandler = new XBRLXLinkHandlerImpl(); 
			XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(xlinkHandler, new XBRLCustomLinkRecogniserImpl());
			Loader loader = new LoaderImpl(store,xlinkProcessor,startingURIs);
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
		System.err.println("Command line usage: java org.xbrlapi.data.xindice.Load [parameters] [URIs]");
		System.err.println("Parameters: ");
		System.err.println("  -host			(mandatory) Database host domain name");
		System.err.println("  -port			(mandatory) Database host port");
		System.err.println("  -path			(mandatory) Database host path");
		System.err.println("  -container	(mandatory) Full path and name of container collection that will hold the data store collection");
		System.err.println("  -collection	(mandatory) Name of collection that will hold the data store");
		System.err.println("  -cache		(optional) Absolute path of for document cache");
		System.exit(1);
	}
}
