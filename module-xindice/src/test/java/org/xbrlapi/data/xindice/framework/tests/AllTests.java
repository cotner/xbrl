package org.xbrlapi.data.xindice.framework.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.data.xindice.framework.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(CollectionCreationAndDeletionTestCase.class);
        suite.addTestSuite(DatabaseManagerInitialisationTestCase.class);
		suite.addTestSuite(DatabaseManagerTestCase.class);
        suite.addTestSuite(GetContentAsDOMTestCase.class);
		suite.addTestSuite(SetContentAsDOMTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
