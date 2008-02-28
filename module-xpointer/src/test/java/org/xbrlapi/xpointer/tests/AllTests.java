package org.xbrlapi.xpointer.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.xpointer.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(PointerGrammarTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
