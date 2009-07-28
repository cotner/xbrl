package org.xbrlapi.data.exist.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.data.exist.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(AddToExistingStoreTestCase.class);
        suite.addTestSuite(DBConnectionTestCase.class);
        suite.addTestSuite(DocumentRecoveryFromStoreTestCase.class);
        suite.addTestSuite(QueryForStringsTestCase.class);
		suite.addTestSuite(StoreImplConstructorTestCase.class);
		suite.addTestSuite(StoreImplTestCase.class);
		
		suite.addTest(org.xbrlapi.data.exist.tests.framework.AllTests.suite());		
		//$JUnit-END$
		return suite;
	}

}
