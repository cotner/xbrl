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

public class TitleTestCase extends TestCase {

	private String xmlS1;



	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		xmlS1 = 
			"<?xml version='1.0' ?>\n"
			+ "<root xmlns:xlink=\"" + Constants.XLinkNamespace + "\">"

			+ "<child1 xlink:type=\"title\">"
			+ "Child 1 title"
			+ "</child1>"

			+ "<child2 xlink:type=\"extended\">"
			
			+ "<child3 xlink:type=\"title\">"
			+ "Child 3 title"
			+ "<child6 xlink:type=\"title\">Child 6 title</child6>"
			+ "</child3>"
			
			+ "<child4 xlink:type=\"arc\">"
			+ "<child5 xlink:type=\"title\">Child 5 title</child5>"
			+ "</child4>"

			+ "<child7 xlink:type=\"resource\">"
			+ "<child8 xlink:type=\"title\">Child 8 title</child8>"
			+ "</child7>"
			
			+ "</child2>"
			
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
	public TitleTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the basic features of a title
	 */
	public final void testTitle() throws Exception {
		XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(new TitleHandler(this));		
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
