package org.xbrlapi.data.exist.framework.tests;
/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

import org.xbrlapi.data.exist.DBConnectionImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

public class DatabaseManagerInitialisationTestCase extends BaseTestCase {

	private DBConnectionImpl connection;

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
