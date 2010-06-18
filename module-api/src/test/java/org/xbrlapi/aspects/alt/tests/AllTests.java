package org.xbrlapi.aspects.alt.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.aspects.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(AspectsTestCase.class);
        suite.addTestSuite(PeriodAspectTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
