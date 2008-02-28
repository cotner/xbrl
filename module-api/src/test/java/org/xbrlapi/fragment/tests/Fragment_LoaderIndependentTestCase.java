package org.xbrlapi.fragment.tests;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.MockFragmentImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests the implementation of the org.xbrlapi.Fragment interface without 
 * doing a full data discovery using a LoaderImpl.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class Fragment_LoaderIndependentTestCase extends BaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public Fragment_LoaderIndependentTestCase(String arg0) {
		super(arg0);
	}





	
	/**
	 * Test retrieval of the type of fragment from an unstored fragment.
	 */
	public void testGetFragmentTypeForAnUnstoredFragment() {
		try {
			Fragment f = new MockFragmentImpl("1");
			assertEquals("org.xbrlapi.impl.MockFragmentImpl",f.getType());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test that a null index throws an exception when creating a fragment.
	 */
	public void testExpectedExceptionCreatingFragmentWithNullIndex() {

		try {
			Fragment f = new MockFragmentImpl(null);
			fail("An exception should be thrown if you create a fragment with a null index.");
		} catch (XBRLException expected) {
		}

	}
	
	/**
	 * Test retrieval of the namespace URI of root node in the fragment
	 * when the fragment has a namespace for the root element.
	 */
	public void testGetNamespaceURIOfAnInConstructionFragmentWithANamespace() {
		try {
			String ns = "http://xbrlapi.org/";
			MockFragmentImpl f = new MockFragmentImpl("1", ns, "root", "my:root");
			assertEquals(ns, f.getNamespaceURI());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}

}
