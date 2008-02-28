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

public class SimpleLinkTestCase extends TestCase {

	private String xmlS1;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SimpleLinkTestCase.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		xmlS1 = 
			"<?xml version='1.0' ?>\n"
			+ "<my:root xmlns:my=\"http://www.xbrlapi.org/ns/\" xmlns:xlink=\"" + Constants.XLinkNamespace + "\">"

			+ "<my:child1 xlink:type=\"simple\" xlink:href=\"http://www.xbrlapi.org/\"/>"

			+ "<my:child2 xlink:type=\"simple\" xlink:href=\"http://www.xbrlapi.org/\" xlink:role=\"http://www.xbrlapi.org/role/\" xlink:arcrole=\"http://www.xbrlapi.org/arcrole/\" xlink:show=\"new\" xlink:actuate=\"onLoad\" xlink:title=\"Human readable title\"/>"

			+ "<my:child3 xlink:type=\"extended\">"
			+ "<my:child4 xlink:type=\"simple\"/>"
			+ "</my:child3>"

			+ "<my:child5 xlink:type=\"simple\" xlink:href=\"http://www.xbrlapi.org/\">"
			+ "<my:child6 xlink:type=\"extended\">"
			+ "<my:child7 xlink:type=\"simple\" xlink:show=\"embed\"/>"
			+ "</my:child6>"
			+ "</my:child5>"

			+ "<my:child8 xlink:type=\"simple\" xlink:href=\"http://www.xbrlapi.org/\" xlink:role=\"malformed role\" xlink:arcrole=\"http://www.xbrlapi.org/arcrole/\" xlink:show=\"new\" xlink:actuate=\"onLoad\" xlink:title=\"Human readable title\"/>"

			+ "</my:root>";
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
	public SimpleLinkTestCase(String arg0) {
		super(arg0);
	}

	private boolean e8 = false;
	/**
	 * Test the basic features of a simple link
	 */
	public final void testSimpleLink() throws Exception {
		XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(new SimpleLinkHandler(this));		
        ContentHandlerImpl handler = new ContentHandlerImpl(xlinkProcessor);
		XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		reader.setContentHandler(handler);
		reader.setFeature("http://xml.org/sax/features/namespaces",true);
		reader.parse(new InputSource(new StringReader(xmlS1)));
		if ( ! e8 ) {
			fail("child8 malformed role not signalled");
		}
	}

	protected final void noteE8() {
		e8 = true;
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
