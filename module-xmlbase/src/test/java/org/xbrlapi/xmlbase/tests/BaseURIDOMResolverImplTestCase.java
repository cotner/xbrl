package org.xbrlapi.xmlbase.tests;

import java.net.URI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.utilities.XMLDOMBuilder;
import org.xbrlapi.xmlbase.BaseURIDOMResolver;
import org.xbrlapi.xmlbase.BaseURIDOMResolverImpl;
import org.xbrlapi.xmlbase.XMLBaseException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class BaseURIDOMResolverImplTestCase extends BaseTestCase {
	
	private String xmlS1, xmlS2, xmlS3;
	private Document xmlD1, xmlD2, xmlD3;
	private BaseURIDOMResolver baseURIResolver, nullBaseURIResolver;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// Create the XML documents to analyse
		xmlS1 = 
			"<?xml version=\"1.0\" ?>\n"
			+ "<root xml:base=\"http://www.xbrlapi.org/root.xml\">"
			+ "<child1 xml:base=\"http://www.xbrlapi.org/child1.xml\"/>"
			+ "<child2 xml:base=\"child2.xml\"/>"
			+ "<child3 />"
			+ "<child4 xml:base=\"\"/>"
			+ "</root>";
		xmlD1 = XMLDOMBuilder.newDocument(xmlS1);
		
		xmlS2 = 
			"<?xml version=\"1.0\" ?>\n"
			+ "<my:root xml:base=\"http://www.xbrlapi.org/root.xml\" xmlns:my=\"http://www.xbrlapi.org/ns/\">"
			+ "<my:child1 xml:base=\"http://www.xbrlapi.org/child1.xml\"/>"
			+ "<my:child2 xml:base=\"child2.xml\"/>"
			+ "<my:child3 />"
			+ "<my:child4 xml:base=\"\"/>"
			+ "</my:root>";
		xmlD2 = XMLDOMBuilder.newDocument(xmlS2);

		xmlS3 = 
			"<?xml version=\"1.0\" ?>\n"
			+ "<my:root xmlns:my=\"http://www.xbrlapi.org/ns/\">"
			+ "<my:child1 xml:base=\"http://www.xbrlapi.org/child1.xml\"/>"
			+ "<my:child2 xml:base=\"child2.xml\"/>"
			+ "<my:child3 />"
			+ "<my:child4 xml:base=\"\"/>"
			+ "</my:root>";
		xmlD3 = XMLDOMBuilder.newDocument(xmlS3);

		baseURIResolver = new BaseURIDOMResolverImpl(new URI("http://www.xbrlapi.org/document.xml"));
		nullBaseURIResolver = new BaseURIDOMResolverImpl();
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
	public BaseURIDOMResolverImplTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the BaseURISAXResolverImpl constructor.  
	 * The constructor is too simple to break.
	 */
	public final void testBaseURIImpl() {
	}

	/**
	 * Test the Base URI construction for the root element
	 * in an XML document without namespaces for elements and
	 * where the root element has an explicit xml:base attribute
	 */
	public final void testGetExplicitBaseURIForRootElement() {
		NodeList elts = xmlD1.getElementsByTagName("root");
		assertEquals("DOM operation failed",1,elts.getLength());
		Element elt = (Element) elts.item(0);
		try {
			assertEquals("http://www.xbrlapi.org/root.xml",baseURIResolver.getBaseURI(elt).toString());
		} catch (XMLBaseException e) {
			fail("Unexpected XBRLException thrown when determining the base URI.");
		}
	}

	/**
	 * Test the Base URI construction for the root element
	 * in an XML document without namespaces for elements and
	 * where the root element has NO explicit xml:base attribute.
	 */
	public final void testGetImplicitBaseURIForRootElement() {
		NodeList elts = xmlD3.getElementsByTagNameNS("http://www.xbrlapi.org/ns/","root");
		assertEquals("DOM operation failed",1,elts.getLength());
		Element elt = (Element) elts.item(0);
		try {
			assertEquals("http://www.xbrlapi.org/document.xml",baseURIResolver.getBaseURI(elt).toString());
		} catch (XMLBaseException e) {
			fail("Unexpected XBRLException thrown when determining the base URI.");
		}
	}

	/**
	 * Test the resolution of a relative child elements xml:base
	 * against the absolute xml:base of the root element.
	 */
	public final void testGetResolvedBaseURI() {
		NodeList elts = xmlD1.getElementsByTagName("child2");
		assertEquals("DOM operation failed",1,elts.getLength());
		Element elt = (Element) elts.item(0);
		try {
			assertEquals("http://www.xbrlapi.org/child2.xml",nullBaseURIResolver.getBaseURI(elt).toString());
		} catch (XMLBaseException e) {
			fail("Unexpected XBRLException thrown when determining the base URI. " + e.getMessage());
		}
	}	

	/**
	 * Test the resolution of a relative child against a relative root
	 * against an absolute document URI.
	 */
	public final void testGetResolvedBaseURICascadingToDocumentURI() {
		NodeList elts = xmlD2.getElementsByTagNameNS("http://www.xbrlapi.org/ns/","child2");
		assertEquals("DOM operation failed",1,elts.getLength());
		Element elt = (Element) elts.item(0);
		try {
			assertEquals("http://www.xbrlapi.org/child2.xml",baseURIResolver.getBaseURI(elt).toString());
		} catch (XMLBaseException e) {
			fail("Unexpected XBRLException thrown when determining the base URI. " + e.getMessage());
		}
	}
	
	/**
	 * Test the throwing of an exception when the xml:base attribute
	 * is a relative address and there is no absolute URI to resolve
	 * it against.
	 */
	public final void testFailureForMalformedXMLBaseURI() {
		NodeList elts = xmlD3.getElementsByTagNameNS("http://www.xbrlapi.org/ns/","child2");
		assertEquals("Child element was not retrieved correctly.",1,elts.getLength());
		Element elt = (Element) elts.item(0);
		try {
			assertEquals("http://www.xbrlapi.org/child2.xml",nullBaseURIResolver.getBaseURI(elt).toString());
			fail("Relative URI with no absolute base URI to resolve against should have thrown an XBRLException.");
		} catch (XMLBaseException expected) {
			;
		}
	}	
	
	/**
	 * Test XML DOM set up as part of the testing fixture.
	 * This is not directly testing the BaseURISAXResolver implementation.
	 */
	public final void testFixtureXMLDOMCreation() throws Exception {
		// TODO Replace the version of the XML Base testing class being used.
/*		try {
			assertXpathEvaluatesTo("4","count(/root/*)",xmlD1);
			assertXpathEvaluatesTo("child2.xml","/root/child2/@*[local-name()='base' and namespace-uri()='http://www.w3.org/XML/1998/namespace']",xmlD1);
		} catch (TransformerException e) {
			fail("The TransformerException was not anticipated.  " + e.getMessage());
		}*/
	}
	
}