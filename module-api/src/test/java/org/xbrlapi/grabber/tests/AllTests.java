package org.xbrlapi.grabber.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.grabber.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(SecGrabberImplTest.class);
		//$JUnit-END$
		return suite;
	}

}
