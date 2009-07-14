package org.xbrlapi.xlink.tests;
/**
 * XLink Processor tests
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.io.StringReader;

import junit.framework.TestCase;

import org.xbrlapi.utilities.Constants;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ExtendedLinkTestCase extends TestCase {

	private String xmlS1;



	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		xmlS1 = 
			"<?xml version='1.0' ?>\n"
			+ "<root xmlns:xlink=\"" + Constants.XLinkNamespace + "\">"

			+ "<child1 xlink:type=\"simple\" xlink:href=\"http://www.xbrlapi.org/\">"
			+ "<child2 xlink:type=\"extended\"/>"
			+ "<child3 xlink:type=\"extended\" xlink:role=\"www.xbrlapi.org/malformedRole/\"/>"
			+ "</child1>"

			+ "<child4 xlink:type=\"extended\" xlink:title=\"Human readable extended link title\">"
			+ "<child5 xlink:type=\"extended\"/>"
			+ "</child4>"

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
	public ExtendedLinkTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the basic features of an extended link
	 */
	public final void testExtendedLink() throws Exception {
		XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(new ExtendedLinkHandler(this));		
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
			fail("Unexpected Exception when testing SAX Base URI handling. " + e.getMessage());
		}
	}
	
	protected final void checkIsNull(Object actual) {
		try {
			assertNull(actual);
		} catch (Exception e) {
			fail("Unexpected Exception when testing SAX Base URI handling. " + e.getMessage());
		}
	}
	
	protected final void confirmFail(String message) {
		fail(message);
	}
		
}
