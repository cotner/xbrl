package org.xbrlapi.data.dom.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.data.dom.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(ComposeTestCase.class);
		suite.addTestSuite(DocumentRecoveryFromStoreTestCase.class);
		suite.addTestSuite(StoreImplTestCase.class);
		suite.addTestSuite(XBRLStoreImplTestCase.class);
		suite.addTestSuite(DocumentDeletionTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
