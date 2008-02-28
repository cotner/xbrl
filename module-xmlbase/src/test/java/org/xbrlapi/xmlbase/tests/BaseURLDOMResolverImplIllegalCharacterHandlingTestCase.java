package org.xbrlapi.xmlbase.tests;

import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.utilities.XMLDOMBuilder;
import org.xbrlapi.xmlbase.BaseURLDOMResolver;
import org.xbrlapi.xmlbase.BaseURLDOMResolverImpl;
import org.xbrlapi.xmlbase.XMLBaseException;

/**
 * Tests of illegal URL characters using the DOM based XML Base resolver
 * TODO confirm that xml base is adequately testing illegal URI characters
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class BaseURLDOMResolverImplIllegalCharacterHandlingTestCase extends BaseTestCase {
	
	private String xmlS1;
	private Document xmlD1;
	private BaseURLDOMResolver baseURLResolver;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// Create the XML documents to analyse
		xmlS1 = 
			"<?xml version=\"1.0\" ?>\n"
			+ "<root xml:base=\"http://www.xbrlapi.org/r[oo]t.xml\">"
			+ "<child1 xml:base=\"http://www.xbrlapi.org/child1.xml\"/>"
			+ "<child2 xml:base=\"child2.xml\"/>"
			+ "<child3 />"
			+ "<child4 xml:base=\"\"/>"
			+ "</root>";
		xmlD1 = XMLDOMBuilder.newDocument(xmlS1);

		baseURLResolver = new BaseURLDOMResolverImpl(new URL("http://www.xbrlapi.org/document.xml"));
		
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
	public BaseURLDOMResolverImplIllegalCharacterHandlingTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the encoding of the space character in an xml:base attribute
	 */
	public final void testGetExplicitBaseURLForRootElement() {
		NodeList elts = xmlD1.getElementsByTagName("root");
		assertEquals("DOM operation failed",1,elts.getLength());
		Element elt = (Element) elts.item(0);
		try {
			assertEquals("http://www.xbrlapi.org/r[oo]t.xml",baseURLResolver.getBaseURL(elt).toString());
		} catch (XMLBaseException e) {
			fail("Unexpected XBRLException thrown when determining the base URL.");
		}
	}
	
}