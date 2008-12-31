package org.xbrlapi.xmlbase.tests;

import java.io.StringReader;
import java.net.URI;

import org.xbrlapi.xmlbase.BaseURISAXResolver;
import org.xbrlapi.xmlbase.BaseURISAXResolverImpl;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class BaseURISAXResolverImplTestCase extends BaseTestCase {
	
	private String xml;
	private BaseURISAXResolver baseURIResolver, nullBaseURIResolver;

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

		baseURIResolver = new BaseURISAXResolverImpl(new URI("http://www.xbrlapi.org/document.xml"));
		nullBaseURIResolver = new BaseURISAXResolverImpl();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for BaseURIDOMResolverImplTests.
	 * @param arg0
	 */
	public BaseURISAXResolverImplTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the BaseURISAXResolverImpl constructor.  
	 * The constructor is too simple to break.
	 */
	public final void testBaseURIImpl() {
	}
	
	/**
	 * Test SAX parser XML Base resolver
	 */
	public final void testSAXBaseURIResolution() throws Exception {
        new BaseTestHandler(this,nullBaseURIResolver);
		XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		reader.setFeature("http://xml.org/sax/features/namespaces",true);
		reader.parse(new InputSource(new StringReader(xml)));
	}

	protected final void checkSAXBaseURIHandling(String expected, Attributes attrs, String inheritedURI) {
		try {
			assertEquals(expected,baseURIResolver.getBaseURI());
			assertNotNull(attrs);
            assertNotNull(inheritedURI);
		} catch (Exception e) {
			fail("Unexpected Exception when testing SAX Base URI handling. " + e.getMessage());
		}
	}
}