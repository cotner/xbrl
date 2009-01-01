package org.xbrlapi.cache.framework.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.cache.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(FileSchemeURITestCase.class);
		//$JUnit-END$
		return suite;
	}

}
