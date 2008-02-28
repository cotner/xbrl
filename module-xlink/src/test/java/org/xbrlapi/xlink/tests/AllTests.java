package org.xbrlapi.xlink.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.xlink.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(SimpleLinkTestCase.class);
		suite.addTestSuite(ArcTestCase.class);
		suite.addTestSuite(TitleTestCase.class);
		suite.addTestSuite(ExtendedLinkTestCase.class);
		suite.addTestSuite(ResourceTestCase.class);
		suite.addTestSuite(LocatorTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
