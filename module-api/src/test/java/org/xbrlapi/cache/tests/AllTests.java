package org.xbrlapi.cache.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.cache.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(CacheImplTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
