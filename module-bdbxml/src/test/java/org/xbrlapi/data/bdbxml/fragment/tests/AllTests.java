package org.xbrlapi.data.bdbxml.fragment.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.data.bdbxml.fragment.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(ConceptTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
