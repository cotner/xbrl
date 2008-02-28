package org.xbrlapi.xmlbase.tests;

/**
 * Provides a base test case for all tests in the XBRL API test suite.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
import junit.framework.TestCase;

abstract public class BaseTestCase extends TestCase {


	
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Base test case constructor.
	 * @param arg0
	 */
	public BaseTestCase(String arg0) {
		super(arg0);
	}
	


}
