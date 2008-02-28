package org.xbrlapi.data.xindice.framework.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.data.xindice.framework.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(DatabaseManagerTestCase.class);
		suite.addTestSuite(CollectionCreationAndDeletionTestCase.class);
		suite.addTestSuite(SetContentAsDOMTestCase.class);
		suite.addTestSuite(DatabaseManagerInitialisationTestCase.class);
		suite.addTestSuite(GetContentAsDOMTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
