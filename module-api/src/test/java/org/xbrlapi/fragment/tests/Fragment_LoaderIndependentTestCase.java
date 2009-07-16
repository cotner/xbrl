package org.xbrlapi.fragment.tests;

import java.net.URI;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.MockImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xbrlapi.utilities.Constants;
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
			Fragment f = new MockImpl("1");
			assertEquals("org.xbrlapi.impl.MockImpl",f.getType());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test that a null index throws an exception when creating a fragment.
	 */
	public void testExpectedExceptionCreatingFragmentWithNullIndex() {

		try {
			new MockImpl(null);
			fail("An exception should be thrown if you create a fragment with a null index.");
		} catch (XBRLException expected) {
		}

	}
	
	/**
	 * Test retrieval of the namespace URI of root node in the fragment
	 * when the fragment has a namespace for the root element.
	 */
	public void testGetNamespaceOfAnInConstructionFragmentWithANamespace() {
	    try {
			URI ns = Constants.XBRLAPINamespace;
			MockImpl f = new MockImpl("Mockery");
			f.appendDataElement(ns, "root", "my:root");
			assertEquals(ns, f.getNamespace());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}

}
