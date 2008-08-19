package org.xbrlapi.xdt.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.xdt.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(DimensionIdentificationTestCase.class);
        suite.addTestSuite(HypercubeTestCase.class);
        suite.addTestSuite(ExplicitDimensionValueTestCase.class);
        suite.addTestSuite(TypedDimensionValueTestCase.class);
        suite.addTestSuite(DefaultDimensionValueTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
