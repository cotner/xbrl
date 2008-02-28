package org.xbrlapi.xmlbase.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.xmlbase.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(BaseURLDOMResolverImplTestCase.class);
		suite
				.addTestSuite(BaseURLDOMResolverImplIllegalCharacterHandlingTestCase.class);
		suite.addTestSuite(BaseURLSAXResolverImplTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
