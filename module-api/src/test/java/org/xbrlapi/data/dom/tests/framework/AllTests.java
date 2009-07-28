package org.xbrlapi.data.dom.tests.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.data.dom.framework.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(SaxonXQueryTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
