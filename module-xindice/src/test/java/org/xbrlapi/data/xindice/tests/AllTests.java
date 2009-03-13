package org.xbrlapi.data.xindice.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.data.xindice.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(AddToExistingStoreTestCase.class);
        suite.addTestSuite(DBConnectionTestCase.class);
        suite.addTestSuite(DocumentRecoveryFromStoreTestCase.class);
		suite.addTestSuite(StoreImplConstructorTestCase.class);
		suite.addTestSuite(StoreImplTestCase.class);
        suite.addTestSuite(QueryForStringsTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
