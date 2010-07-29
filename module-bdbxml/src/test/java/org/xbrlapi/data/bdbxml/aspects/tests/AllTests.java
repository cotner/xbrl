package org.xbrlapi.data.bdbxml.aspects.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.data.bdbxml.aspects.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(LabellerTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
