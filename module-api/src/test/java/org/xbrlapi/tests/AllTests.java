package org.xbrlapi.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.tests");
		//$JUnit-BEGIN$
		//suite.addTest(org.xbrlapi.utils.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.builder.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.cache.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.data.dom.framework.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.data.dom.tests.AllTests.suite());
        suite.addTest(org.xbrlapi.data.resource.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.fragment.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.loader.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.relationships.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.SAXHandlers.tests.AllTests.suite());
		suite.addTest(org.xbrlapi.xlink.handler.tests.AllTests.suite());
		//suite.addTest(org.xbrlapi.xpointer.tests.AllTests.suite());
		//suite.addTest(org.xbrlapi.xlink.tests.AllTests.suite());
		//suite.addTest(org.xbrlapi.xmlbase.tests.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
