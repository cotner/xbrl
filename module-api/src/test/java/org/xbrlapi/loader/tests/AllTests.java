package org.xbrlapi.loader.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.loader.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(AsyncLoaderImplTestCase.class);
        suite.addTestSuite(HistoryImplTestCase.class);
        suite.addTestSuite(LoaderImplTestCase.class);
		suite.addTestSuite(LoaderSerializationTestCase.class);
        suite.addTestSuite(LoadingDuplicateDocumentsTestCase.class);
        suite.addTestSuite(PartialLoadingTestCase.class);
        suite.addTestSuite(SimpleLinkDiscoveryTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
