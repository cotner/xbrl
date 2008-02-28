package org.xbrlapi.xlink.tests;


import java.io.StringReader;

import junit.framework.TestCase;

import org.xbrlapi.utilities.Constants;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
/**
 * XLink Processor tests
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ArcTestCase extends TestCase {

	private String xmlS1;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		xmlS1 = 
			"<?xml version='1.0' ?>\n"
			+ "<root xmlns:xlink=\"" + Constants.XLinkNamespace + "\">"

			+ "<child1 xlink:type=\"arc\" />"
			+ "<child2 xlink:type=\"arc\" xlink:from=\"source\" xlink:to=\"destination\" xlink:show=\"new\" xlink:actuate=\"onLoad\" xlink:title=\"Human readable title\"/>"
			+ "<child3 xlink:type=\"extended\">"
			+ "<child4 xlink:type=\"arc\" />"
			
			+ "<child5 xlink:type=\"arc\" xlink:from=\"source\" xlink:to=\"destination\" xlink:show=\"new\" xlink:actuate=\"onLoad\" xlink:title=\"Human readable title\"/>"
			+ "<child6 xlink:type=\"locator\" xlink:href=\"http://www.xbrlapi.org/\" xlink:role=\"http://www.xbrlapi.org/role/\" xlink:title=\"Human readable title\">"
			+ "<child7 xlink:type=\"arc\" xlink:from=\"source\" xlink:to=\"destination\" xlink:show=\"new\" xlink:actuate=\"onLoad\" xlink:title=\"Human readable title\"/>"
			
			+ "</child6>"
			+ "<child8 xlink:type=\"arc\" xlink:from=\"the source\" xlink:to=\"destination\" xlink:show=\"new\" xlink:actuate=\"onLoad\" xlink:title=\"Human readable title\"/>"
			+ "</child3>"

			+ "</root>";
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for SimpleLinkTests.
	 * @param arg0
	 */
	public ArcTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the basic features of an arc
	 */
	public final void testArc() throws Exception {
		XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(new ArcHandler(this));		
        ContentHandlerImpl handler = new ContentHandlerImpl(xlinkProcessor);
		XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		reader.setContentHandler(handler);
		reader.setFeature("http://xml.org/sax/features/namespaces",true);
		reader.parse(new InputSource(new StringReader(xmlS1)));
	}

	protected final void checkEqual(String expected, String actual) {
		try {
			assertEquals(expected,actual);
		} catch (Exception e) {
			fail("Unexpected Exception when testing SAX Base URL handling. " + e.getMessage());
		}
	}
	
	protected final void checkIsNull(Object actual) {
		try {
			assertNull(actual);
		} catch (Exception e) {
			fail("Unexpected Exception when testing SAX Base URL handling. " + e.getMessage());
		}
	}
	
	protected final void confirmFail(String message) {
		fail(message);
	}
		
}
