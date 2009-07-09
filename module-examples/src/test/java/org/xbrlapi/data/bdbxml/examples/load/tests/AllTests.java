package org.xbrlapi.data.bdbxml.examples.load.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("org.xbrlapi.data.bdbxml.examples.load.tests suite");
		//$JUnit-BEGIN$
		suite.addTestSuite(LoadTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
