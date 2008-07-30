package org.xbrlapi.xmlbase.tests;

import java.io.StringReader;
import java.net.URL;

import org.xbrlapi.xmlbase.BaseURLSAXResolver;
import org.xbrlapi.xmlbase.BaseURLSAXResolverImpl;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class BaseURLSAXResolverImplTestCase extends BaseTestCase {
	
	private String xml;
	private BaseURLSAXResolver baseURLResolver, nullBaseURLResolver;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// Create the XML documents to analyse

		xml = 
			"<?xml version=\"1.0\" ?>\n"
			+ "<my:root xml:base=\"http://www.xbrlapi.org/root.xml\" xmlns:my=\"http://www.xbrlapi.org/ns/\">"
			+ "<my:child1 xml:base=\"http://www.xbrlapi.org/child1.xml\"/>"
			+ "<my:child2 xml:base=\"child2.xml\"/>"
			+ "<my:child3 />"
			+ "<my:child4 xml:base=\"\"/>"
			+ "</my:root>";

		baseURLResolver = new BaseURLSAXResolverImpl(new URL("http://www.xbrlapi.org/document.xml"));
		nullBaseURLResolver = new BaseURLSAXResolverImpl();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for BaseURLDOMResolverImplTests.
	 * @param arg0
	 */
	public BaseURLSAXResolverImplTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the BaseURLSAXResolverImpl constructor.  
	 * The constructor is too simple to break.
	 */
	public final void testBaseURLImpl() {
	}
	
	/**
	 * Test SAX parser XML Base resolver
	 */
	public final void testSAXBaseURLResolution() throws Exception {
        new BaseTestHandler(this,nullBaseURLResolver);
		XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		reader.setFeature("http://xml.org/sax/features/namespaces",true);
		reader.parse(new InputSource(new StringReader(xml)));
	}

	protected final void checkSAXBaseURLHandling(String expected, Attributes attrs, String inheritedURL) {
		try {
			assertEquals(expected,baseURLResolver.getBaseURL());
			assertNotNull(attrs);
            assertNotNull(inheritedURL);
		} catch (Exception e) {
			fail("Unexpected Exception when testing SAX Base URL handling. " + e.getMessage());
		}
	}
}