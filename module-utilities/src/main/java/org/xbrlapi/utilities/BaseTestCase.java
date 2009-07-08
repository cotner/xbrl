package org.xbrlapi.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * Provides a base test case for all tests in the XBRL API test suite.
 *  This package provides some base testcase classes
 *  that extend in useful ways on the JUnit base test case.
 *  Te XMLBase subclass helps with tests related to XML.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
abstract public class BaseTestCase extends TestCase {

	// Create the logger
	protected static Logger logger = Logger.getLogger(BaseTestCase.class);	
	
	/**
	 * Use this constant to determine the properties file that configures the Unit testing of the XBRLAPI.
	 */  
	private final String configurationPropertiesIdentifier = "test.configuration.properties";
	protected Properties configuration = null;
	
	/**
	 * Absolute path to the local document cache
	 */  	
	protected String cachePath;
	
	/**
	 * Base URI for test data
	 */
	protected String baseURI;
	
	protected void setUp() throws Exception {
		super.setUp();
        try {
            // Load the test configuration details from the specified properties file
            configuration = new Properties();
            File configurationFile = new File(System.getProperty("xbrlapi.test.configuration"));
            if (!configurationFile.exists()) {
                configurationFile = new File(configurationPropertiesIdentifier);
            }
            if (!configurationFile.exists()) {
                fail("The configuration File " + configurationFile + " does not exist.");
            }
            configuration.load(new FileInputStream(configurationFile));
            if (!configuration.containsKey("local.cache")) {
                fail("The test configuration file: " + configurationFile + " is not valid.");
            }

            // Local Cache location
            cachePath = configuration.getProperty("local.cache");
            
            // Base URI for test data
            baseURI = configuration.getProperty("test.data.baseURI");

        } catch (NullPointerException e) {
            // This additional error handling was contributed by Walter Hamscher (25 July 2008)
            fail("Null system property xbrlapi.test.configuration");            
        } catch (FileNotFoundException e) {
            fail("The test suite configuration properties file was not found.");            
        } catch (IOException e) {
            fail("An I/O error occurred while accessing the test suite configuration properties file.");
        }
		GrammarCacheImpl.emptyGrammarPool();		
	}

	/**
	 * Base test case constructor loads the test suite configuration properties.
	 * The test configuration information must be available in a properties file.
	 * If the System property "xbrlapi.test.configuration" is set equal to the location
	 * of a test configuration properties file, then that file will be used.  Otherwise
	 * the constructor will attempt to locate a file called test.configuration.properties
	 * in the working directory that the tests are being executed from.
	 * @param arg0
	 */
	public BaseTestCase(String arg0) {
		super(arg0);		
	}
	
	/**
	 * @param property determining the URI of the test data
	 * @return the absolute URI of the test data
	 */
	public URI getURI(String property) {

		String myProperty = configuration.getProperty(property);

		URI uri = null;
		try {
	        if (myProperty.startsWith("http:/")) {
	            uri = new URI(myProperty);
	        } else if (myProperty.startsWith("file:/")) {
	            uri = new URI(myProperty);
	        } else if (property.startsWith("test.data.local.")) {
	            File root = new File(configuration.getProperty("local.test.data.root"));
	            File file = new File(root,configuration.getProperty(property));
	            uri = file.toURI();
	        } else {
	            uri = new URI(baseURI + configuration.getProperty(property));		    
	        }
		} catch (URISyntaxException e) {
		    fail("The URI syntax is invalid.");
		}
        logger.debug("Test URI is " + uri);
		return uri;

	}	

	/**
	 * Serializes an XML fragment to the output stream.
	 * @param what The DOM Document or Element node to use as 
	 * the root for the XML fragment to serialize.
	 * @throws XBRLException if the node is not a document or element node.
	 * @throws IOException if the serialization IO operation fails.
	 */
    public void serialize(Node what) throws IOException, XBRLException {
    	OutputFormat format = null;
		if (what.getNodeType() == Node.DOCUMENT_NODE) {
	    	format = new OutputFormat((Document) what, "UTF-8", true);
		} else {
			format = new OutputFormat(what.getOwnerDocument(), "UTF-8", true);
		}
		XMLSerializer output = new XMLSerializer(System.out, format);
		output.setNamespaces(true);
		if (what.getNodeType() == Node.DOCUMENT_NODE) {
			output.serialize((Document) what);
		} else if (what.getNodeType() == Node.ELEMENT_NODE) {
			output.serialize((Element) what);
		} else {
			throw new XBRLException("The node is not a document or element node.");
		}
    }	
	
}
