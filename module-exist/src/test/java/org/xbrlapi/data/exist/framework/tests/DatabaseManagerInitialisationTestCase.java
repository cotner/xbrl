package org.xbrlapi.data.exist.framework.tests;

import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public class DatabaseManagerInitialisationTestCase extends BaseTestCase {

	//private DBConnectionImpl connection;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for DBConnectionTests.
	 * @param arg0
	 */
	public DatabaseManagerInitialisationTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the creation and registration of an XMLDB database object
	 */
	public final void testDatabaseManagerInitialisation() {
        Database database = new org.exist.xmldb.DatabaseImpl();
        try {
        	DatabaseManager.registerDatabase(database);
        } catch (XMLDBException e) {
        	fail(e.getMessage());
        }
	}
	
}
