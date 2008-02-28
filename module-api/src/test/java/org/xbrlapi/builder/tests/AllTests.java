package org.xbrlapi.builder.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.builder.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(BuilderImplTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
