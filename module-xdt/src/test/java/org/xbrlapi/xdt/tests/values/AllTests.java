package org.xbrlapi.xdt.tests.values;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.xdt.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(ExplicitDimensionValueTestCase.class);
        suite.addTestSuite(TypedDimensionValueTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
