package org.xbrlapi.data.bdbxml.framework.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.data.bdbxml.framework.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(DatabaseManagerInitialisationAndQueryingTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
