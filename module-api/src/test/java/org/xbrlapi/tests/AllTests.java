package org.xbrlapi.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.tests");
		//$JUnit-BEGIN$
        suite.addTest(org.xbrlapi.aspects.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.builder.tests.AllTests.suite());
        suite.addTest(org.xbrlapi.cache.framework.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.cache.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.data.dom.framework.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.data.dom.tests.AllTests.suite());
        suite.addTest(org.xbrlapi.data.resource.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.fragment.tests.AllTests.suite());
        suite.addTest(org.xbrlapi.grabber.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.loader.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.relationships.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.sax.identifiers.tests.AllTests.suite());
        suite.addTest(org.xbrlapi.sax.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.xlink.handler.tests.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
