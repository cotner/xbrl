package org.xbrlapi.SAXHandlers.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.SAXHandlers.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(EntityResolverTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
