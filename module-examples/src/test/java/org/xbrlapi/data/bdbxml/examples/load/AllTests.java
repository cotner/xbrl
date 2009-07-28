package org.xbrlapi.data.bdbxml.examples.load;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("org.xbrlapi.data.bdbxml.examples.load.tests suite");
		//$JUnit-BEGIN$
		suite.addTest(org.xbrlapi.data.bdbxml.examples.load.tests.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
