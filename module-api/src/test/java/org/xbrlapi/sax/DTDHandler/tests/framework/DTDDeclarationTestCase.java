package org.xbrlapi.sax.DTDHandler.tests.framework;

import java.net.URI;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xbrlapi.utilities.BaseTestCase;
import org.xbrlapi.utilities.Constants;
import org.xml.sax.XMLReader;

/**
 * Test the DTD declaration handling system.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public abstract class DTDDeclarationTestCase extends BaseTestCase {
	
    private final URI uri = this.getURI("real.data.schema.with.dtd");

	private SAXParser saxParser = null;
	
	private Handler handler = null;
	
	private XMLReader xmlReader = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		// Set up the SAX parser to analyse the input XML.
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setValidating(false);
			saxParserFactory.setNamespaceAware(true);
			saxParser = saxParserFactory.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			handler = new Handler();
			xmlReader.setContentHandler(handler);
			xmlReader.setErrorHandler(handler);
			xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes",true);
			xmlReader.setProperty("http://xml.org/sax/properties/declaration-handler",handler);
            xmlReader.setProperty(Constants.JAXP_SCHEMA_LANGUAGE,Constants.W3C_XML_SCHEMA);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public DTDDeclarationTestCase(String arg0) {
		super(arg0);
	}
	
	public void testHandlingOfDTDEntities() {
		try {
			xmlReader.parse(uri.toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testHandlingOfDTDs() {
		try {
			xmlReader.parse("http://www.w3.org/2001/XMLSchema.xsd");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testStorageOfDTDs() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}



}
