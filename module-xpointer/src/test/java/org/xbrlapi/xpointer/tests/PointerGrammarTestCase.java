package org.xbrlapi.xpointer.tests;

import java.util.Vector;

import junit.framework.TestCase;

import org.xbrlapi.xpointer.ParseException;
import org.xbrlapi.xpointer.PointerGrammar;
import org.xbrlapi.xpointer.PointerPart;
import org.xbrlapi.xpointer.TokenMgrError;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class PointerGrammarTestCase extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for PointerGrammarTest.
	 * @param arg0
	 */
	public PointerGrammarTestCase(String arg0) {
		super(arg0);
	}

	@SuppressWarnings("unchecked")
	public void testShortHandPointer() {
		String pointer = "qwerty";
	  	java.io.StringReader sr = new java.io.StringReader(pointer);
		java.io.Reader r = new java.io.BufferedReader(sr);
		PointerGrammar parser = new PointerGrammar(r);
		try {
			Vector pointerParts = parser.Pointer();
			PointerPart p = (PointerPart) pointerParts.get(0);
			assertEquals(pointer,p.getEscapedSchemeData());
			assertEquals(PointerPart.DefaultPointerNamespace,p.getSchemeNamespace().toString());
			assertEquals("element",p.getSchemeLocalName());			
		} catch (ParseException e) {
			fail("X Pointer, " + pointer + " should not cause a PointerGrammar parse error.");
		} catch (Exception e) {
			fail("The shorthand pointer parts were not correctly constructed by the Pointer Grammar.");
		}
	}
	
	public void testInvalidShortHandPointer() {
		String pointer = "Invalid/Id";
	  	java.io.StringReader sr = new java.io.StringReader(pointer);
		java.io.Reader r = new java.io.BufferedReader(sr);
		PointerGrammar parser = new PointerGrammar(r);
		try {
			parser.Pointer();
			fail("X Pointer, " + pointer + ", is not valid.");
		} catch (TokenMgrError e) {
		} catch (ParseException e) {
		}
	}
	

	public void testInvalidShortHandPointerAfterSchemePointer() {
		String pointer = "element(/1) qwerty";
	  	java.io.StringReader sr = new java.io.StringReader(pointer);
		java.io.Reader r = new java.io.BufferedReader(sr);
		PointerGrammar parser = new PointerGrammar(r);
		try {
			parser.Pointer();
			fail("X Pointer, " + pointer + ", is not valid.");
		} catch (TokenMgrError e) {
		} catch (ParseException e) {
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testElementPointer() {
		String pointer = "element(/1)";
	  	java.io.StringReader sr = new java.io.StringReader(pointer);
		java.io.Reader r = new java.io.BufferedReader(sr);
		PointerGrammar parser = new PointerGrammar(r);
		try {
			Vector pointerParts = parser.Pointer();
			PointerPart p = (PointerPart) pointerParts.get(0);
			assertEquals("/1",p.getEscapedSchemeData());
			assertEquals(PointerPart.DefaultPointerNamespace,p.getSchemeNamespace().toString());
			assertEquals("element",p.getSchemeLocalName());			
		} catch (TokenMgrError e) {
			fail("Unexpected failure to parse tokens in " + pointer);
		} catch (ParseException e) {
			fail("Unexpected failure to parse " + pointer);
		}
	}

	@SuppressWarnings("unchecked")
	public void testXMLNSPointer() {
		String pointer = "xmlns(x http://www.xbrlapi.org/stuff)";
		// TODO make sure that xmlns scheme pointer part syntax is used correctly.
	  	java.io.StringReader sr = new java.io.StringReader(pointer);
		java.io.Reader r = new java.io.BufferedReader(sr);
		PointerGrammar parser = new PointerGrammar(r);
		try {
			Vector pointerParts = parser.Pointer();
			PointerPart p = (PointerPart) pointerParts.get(0);
			assertEquals("x http://www.xbrlapi.org/stuff",p.getEscapedSchemeData());
			assertEquals(PointerPart.DefaultPointerNamespace,p.getSchemeNamespace().toString());
			assertEquals("xmlns",p.getSchemeLocalName());			
		} catch (TokenMgrError e) {
			fail("Unexpected failure to parse tokens in " + pointer);
		} catch (ParseException e) {
			fail("Unexpected failure to parse " + pointer);
		}
	}

	@SuppressWarnings("unchecked")
	public void testTwoPartPointer() {
		String pointer = "element(/1/123) \n\r\telement(/1)";
	  	java.io.StringReader sr = new java.io.StringReader(pointer);
		java.io.Reader r = new java.io.BufferedReader(sr);
		PointerGrammar parser = new PointerGrammar(r);
		try {
			Vector pointerParts = parser.Pointer();
			PointerPart p0 = (PointerPart) pointerParts.get(0);
			assertEquals("/1/123",p0.getEscapedSchemeData());
			assertEquals(PointerPart.DefaultPointerNamespace,p0.getSchemeNamespace().toString());
			assertEquals("element",p0.getSchemeLocalName());			
			PointerPart p1 = (PointerPart) pointerParts.get(1);
			assertEquals("/1",p1.getEscapedSchemeData());
			assertEquals(PointerPart.DefaultPointerNamespace,p1.getSchemeNamespace().toString());
			assertEquals("element",p1.getSchemeLocalName());			
		} catch (TokenMgrError e) {
			fail("Unexpected failure to parse tokens in " + pointer);
		} catch (ParseException e) {
			fail("Unexpected failure to parse " + pointer);
		}
	}
	
	
}
