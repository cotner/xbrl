package org.xbrlapi.data.resource.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.data.resource.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(InMemoryMatcherTestCase.class);
		suite.addTestSuite(InStoreMatcherTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
