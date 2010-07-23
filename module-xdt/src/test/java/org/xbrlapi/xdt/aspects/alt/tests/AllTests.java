package org.xbrlapi.xdt.aspects.alt.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.xbrlapi.xdt.aspects.alt.tests");
        //$JUnit-BEGIN$
        suite.addTestSuite(AspectsTestCase.class);
        suite.addTestSuite(LabellerTestCase.class);
        //$JUnit-END$
        return suite;
	}

}
