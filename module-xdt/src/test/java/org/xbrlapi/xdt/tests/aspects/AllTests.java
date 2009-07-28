package org.xbrlapi.xdt.tests.aspects;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.xdt.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(DimensionalAspectModelTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
