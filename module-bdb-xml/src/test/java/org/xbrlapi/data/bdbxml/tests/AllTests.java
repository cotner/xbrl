package org.xbrlapi.data.bdbxml.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.data.bdbxml.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(DocumentRecoveryFromStoreTestCase.class);
		suite.addTestSuite(StoreImplTestCase.class);
		suite.addTestSuite(AddToExistingStoreTestCase.class);
		suite.addTestSuite(StoreImplConstructorTestCase.class);
		suite.addTestSuite(DTDDeclarationHandlerTestCase.class);
        suite.addTestSuite(SecAsyncGrabberImplTest.class);
		
		suite.addTest(org.xbrlapi.data.bdbxml.framework.tests.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
