package org.xbrlapi.data.dom.framework.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.data.dom.framework.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(XalanXPathTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
