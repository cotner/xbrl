package org.xbrlapi.sax.identifiers.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.xbrlapi.relationships.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(BasicIdentifierTestCase.class);
        suite.addTestSuite(SchemaIdentifierTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
