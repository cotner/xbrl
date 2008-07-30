package org.xbrlapi.data.xindice.framework.tests;
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

import org.xbrlapi.data.xindice.DBConnectionImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

public class DatabaseManagerTestCase extends BaseTestCase {

	private DBConnectionImpl connection;
	private Collection collection;

	private String scheme = configuration.getProperty("xindice.scheme");
	private String domain = configuration.getProperty("xindice.domain");
	private String port = configuration.getProperty("xindice.port");
	private String db = configuration.getProperty("xindice.database");
	private String databaseURI = scheme + "://" + domain + ":" + port + "/" + db;         		

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
        Database database = new org.apache.xindice.client.xmldb.DatabaseImpl();
        try {
        	DatabaseManager.registerDatabase(database);
            collection = DatabaseManager.getCollection(databaseURI);
            assertNotNull("Collection is null", collection);
        } catch (XMLDBException e) {
        	fail("Collection creation failed using " + databaseURI + " " + e.getMessage());
        }
		
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
        collection.close();
	}

	/**
	 * Constructor for DBConnectionTests.
	 * @param arg0
	 */
	public DatabaseManagerTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test the usage of the database manager to get a list of child collections
	 */
	public final void testListingOfChildCollections() {
        try {
            String[] childCollections = collection.listChildCollections();     
            assertEquals(2,childCollections.length);
            Collection system = collection.getChildCollection("system");
            assertEquals("system",system.getName());
        } catch (XMLDBException e) {
        	fail(e.getMessage());
        }
	}	

	/**
	 * Test retrieval of a child collection
	 */
	public final void testRetrievalOfAChildCollection() {
        try {
            Collection system = collection.getChildCollection("system");
            assertEquals("system",system.getName());
        } catch (XMLDBException e) {
        	fail(e.getMessage());
        }
	}	
	
	/**
	 * Test instantiation of a collection manager service
	 */
	public final void testInstantiationOfACollectionManagerService() {
        try {
            collection.getService("CollectionManager", "1.0");
        } catch (XMLDBException e) {
        	fail("Collection manager service could not be instantiated. " + e.getMessage());
        }
	}

	/**
	 * Test instantiation of a collection management service
	 */
	public final void testInstantiationOfACollectionManagementService() {
        try {
            collection.getService("CollectionManagementService", "1.0");
        } catch (XMLDBException e) {
        	fail("Collection management service could not be instantiated. " + e.getMessage());
        }
	}	
	
}
