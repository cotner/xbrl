package org.xbrlapi.xdt.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.xdt.tests");
		//$JUnit-BEGIN$
        suite.addTest(org.xbrlapi.xdt.tests.aspects.AllTests.suite());
        suite.addTest(org.xbrlapi.xdt.tests.values.AllTests.suite());
        suite.addTestSuite(HypercubeTestCase.class);
		suite.addTestSuite(DimensionIdentificationTestCase.class);
        suite.addTestSuite(HypercubeTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
