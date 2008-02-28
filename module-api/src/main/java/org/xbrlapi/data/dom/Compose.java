package org.xbrlapi.data.dom;

/**
 * Provides a main method for composing a DTS into a single DOM object including
 * annotations as per those used in the 
 * <a href="http://www.sourceforge.net/xbrlcomposer/">XBRLComposer</a> 
 * project on Sourceforge.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.xbrlapi.EntryPoint;
import org.xbrlapi.SAXHandlers.EntityResolverImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;

public class Compose implements EntryPoint {

	/**
	 * Load the documents discoverable from the specified URLs.
	 * @param args The white-space delimited list of arguments.
	 */
	public void run(String[] args) {
		try {
			
			String output = null;
			
			String cache = null;
			
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

					if (args[i].equals("-output")) {
						i++;
						output = args[i];
					
					} else if (args[i].equals("-cache")) {
						i++;
						cache = args[i];
					
					} else if (args[i].equals("-class")) {
						i++;

					} else if (args[i].equals("-logger")) {
						i++;
						
					} else
						badUsage("Unknown option " + args[i]);

				} else {
					try {
						System.out.println("Starting URL = " + args[i]);
						startingURLs.add(new URL(args[i]));
					} catch (MalformedURLException e) {
						badUsage(args[i] + " is a malformed URL.");
					}
				}
				i++;
			}

			if (startingURLs.size() == 0)
				badUsage("At least one URL must be specified to seed the data discovery process.");
			
			// Create a new data store for the DTS
			Store store = new StoreImpl();

			// Create a loader
			// Create an XLink processor for XBRL DTS discovery
			XBRLXLinkHandlerImpl xlinkHandler = new XBRLXLinkHandlerImpl(); 
			XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(xlinkHandler, new XBRLCustomLinkRecogniserImpl());
			Loader loader = new LoaderImpl(store,xlinkProcessor,startingURLs);
			xlinkHandler.setLoader(loader);
			
			// Set the entity resolver for the loader to use the specified caching folder
			if (cache != null) {
				loader.setEntityResolver(new EntityResolverImpl(new File(cache)));
			}
			
			loader.discover();
			
			// Generate the necessary composed DOM Object.
			Document composedDTS = loader.getStore().formCompositeDocument();
			
			if (output != null) {
				loader.getStore().serialize(composedDTS.getDocumentElement(),new File(output));
			} else {
				loader.getStore().serialize(composedDTS.getDocumentElement(), System.out);
			}
			
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
	private void badUsage(String message) {
		if (!"".equals(message)) {
			System.err.println(message);
		}
		System.err.println("Command line usage: java org.xbrlapi.Run -class org.xbrlapi.data.dom.Load [parameters] [URLs]");
		System.err.println("Parameters: ");
		System.err.println("  -cache		(optional) Absolute path of for document cache");
		System.exit(1);
	}
}
