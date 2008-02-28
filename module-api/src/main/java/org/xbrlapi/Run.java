package org.xbrlapi;

/**
 * Provides a main method for loading data into a Xindice data store.
 * The real work is passed off to the class specified using the -class
 * argument.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Run {

	protected static Logger logger = Logger.getLogger(Run.class);		
	
	/**
	 * Main line.  This mainline simply determines the class
	 * that actually provides the required entry point.
	 * @param args The white-space delimited list of arguments.
	 */
	@SuppressWarnings("unchecked")
	public static void main(String args[]) {
		try {
			String className = null;
			String loggerConfigurationFile = null;

			int i = 0;
			while (true) {
				if (i >= args.length)
					break;

				if (args[i].equals("-logger")) {
					i++;
					if (i < args.length)
						loggerConfigurationFile = args[i];
					else 
						badUsage("The -logger parameter must be followed by a location of a log4j configuration file.");
				
				} else if (args[i].equals("-class")) {
					i++;
					if (i < args.length)
						className = args[i];
					else 
						badUsage("The -class parameter must be followed by a class name.");
				}
				i++;
			}

			if (loggerConfigurationFile == null) {
				badUsage("A -logger parameter is mandatory.");
			}
			DOMConfigurator.configure(loggerConfigurationFile);
				
			if (className == null) {
				badUsage("A -classname parameter is mandatory.");
			}

			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
		        Class theClass = loader.loadClass(className);
		        Object obj = theClass.newInstance();
		        EntryPoint entryPoint = null;
		        if (obj instanceof EntryPoint) {
		            entryPoint = (EntryPoint) obj;
		        } else {
					badUsage("The specified class does not implement the org.xbrlapi.EntryPoint interface.  Choose another.");
		        }
		        
	            if (entryPoint == null) {
					badUsage("The entry point class, " + className + ", could not be loaded.");
	            }
	            
	            entryPoint.run(args);
		        
			} catch (Exception e) {
				badUsage("The entry point class, " + className + ", could not be loaded.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
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
		System.err.println("Command line usage options:");
		System.err.println("1. java org.xbrlapi.Run -class <value> -logger <value> [other parameters]");
		System.err.println("2. java -jar xbrlapi.jar -class <value> -logger <value> [other parameters]");
		System.err.println("-class <value> must be the name of the class to run.");
		System.err.println("-logger <value> must be the file containing the log4j XML configuration.");
		System.exit(1);
	}
}
