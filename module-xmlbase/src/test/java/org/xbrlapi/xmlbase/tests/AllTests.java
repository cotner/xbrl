package org.xbrlapi.xmlbase.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.xmlbase.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(BaseURIDOMResolverImplTestCase.class);
		suite.addTestSuite(BaseURIDOMResolverImplIllegalCharacterHandlingTestCase.class);
		suite.addTestSuite(BaseURISAXResolverImplTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
