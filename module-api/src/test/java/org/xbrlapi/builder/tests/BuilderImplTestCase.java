package org.xbrlapi.builder.tests;

import org.xbrlapi.builder.Builder;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xbrlapi.utilities.Constants;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class BuilderImplTestCase extends BaseTestCase {

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
	 * Constructor for BuilderImplTests.
	 * @param arg0
	 */
	public BuilderImplTestCase(String arg0) {
		super(arg0);
	}

	public void testGetInsertionPoint() {
		try {
			Builder b = new BuilderImpl();
			b.appendElement(Constants.XBRLAPINamespace,"root","my:root");
			logger.info(b.getInsertionPoint().getClass().getName());
			assertEquals("org.apache.xerces.dom.ElementNSImpl",b.getInsertionPoint().getClass().getName());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testAppendText() {
		//TODO Test builder appendTextTest().
	}

	public void testAppendProcessingInstruction() {
		//TODO Test builder appendProcessingInstruction().
	}

	public void testAppendComment() {
		//TODO Test builder appendComment().
	}

	public void testAppendElement() {
		try {
			Builder b = new BuilderImpl();
			b.appendElement(Constants.XBRLAPINamespace,"root","xbrlapi:root");
			assertEquals(Constants.XBRLAPINamespace.toString(),(b.getInsertionPoint()).getNamespaceURI());
			assertEquals("root",(b.getInsertionPoint()).getLocalName());
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	public void testEndElement() {
		try {
			Builder b = new BuilderImpl();
			b.appendElement(Constants.XBRLAPINamespace,"root","xbrlapi:root");
			assertEquals("org.apache.xerces.dom.ElementNSImpl",b.getInsertionPoint().getClass().getName());
			b.endElement("http://www.xbrlapi.org/","root","xbrlapi:root");
			assertEquals("org.apache.xerces.dom.ElementNSImpl",b.getInsertionPoint().getClass().getName());
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	public void testAppendNotationDecl() {
		//TODO Test builder appendNotationDecl().
	}

	public void testAppendUnparsedEntityDecl() {
		//TODO Test builder appendUnparsedEntityDecl().
	}

	public void testAppendElementDecl() {
		//TODO Test builder appendElementDecl().
	}

	public void testAppendInternalEntityDecl() {
		//TODO Test builder appendInternalEntityDecl().
	}

	public void testAppendExternalEntityDecl() {
		//TODO Test builder appendExternalEntityDecl().
	}

	public void testAppendAttributeDecl() {
		//TODO Test builder appendAttributeDecl().
	}

}
