package org.xbrlapi.data.bdbxml.tests.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.data.bdbxml.framework.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(DatabaseManagerInitialisationAndQueryingTestCase.class);
        suite.addTestSuite(DatabaseDeletionTestCase.class);
        suite.addTestSuite(LoadTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
