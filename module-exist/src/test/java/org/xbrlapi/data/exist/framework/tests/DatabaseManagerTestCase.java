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
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

public class DatabaseManagerTestCase extends BaseTestCase {

	private DBConnectionImpl connection;
	private Collection collection;

	private String scheme;
	private String domain;
	private String port;
	private String db;
	private String databaseURI;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	    scheme = configuration.getProperty("exist.scheme");
	    domain = configuration.getProperty("exist.domain");
	    port = configuration.getProperty("exist.port");
	    db = configuration.getProperty("exist.database");
	    databaseURI = scheme + "://" + domain + ":" + port + "/" + db;
		
        Database database = new org.exist.xmldb.DatabaseImpl();
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
            for (int i=0; i<childCollections.length; i++) {
                Collection child = collection.getChildCollection(childCollections[i]);
                int nameLength = childCollections[i].length();
                String name = child.getName();
                name = name.substring(name.length() - nameLength);
                assertEquals(childCollections[i],name);
            }
            
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
            assertEquals("/db/system",system.getName());
        } catch (XMLDBException e) {
        	fail(e.getMessage());
        }
	}	
	
	/**
	 * Test instantiation of a collection manager service
	 */
	public final void testInstantiationOfACollectionManagerService() {
        try {
        	// TODO Determine why Exist does not provide a CollectionManager service for collections.
            //CollectionManager service = (CollectionManager) collection.getService("CollectionManager", "1.0");
        } catch (Exception e) {
        	fail(e.getMessage());
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
