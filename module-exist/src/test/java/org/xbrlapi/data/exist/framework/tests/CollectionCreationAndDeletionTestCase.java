package org.xbrlapi.data.exist.framework.tests;
/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

public class CollectionCreationAndDeletionTestCase extends BaseTestCase {

	private Collection collection;
	private CollectionManagementService service;
	
	private String scheme = configuration.getProperty("exist.scheme");
	private String domain = configuration.getProperty("exist.domain");
	private String port = configuration.getProperty("exist.port");
	private String db = configuration.getProperty("exist.database");
	private String username = configuration.getProperty("exist.username");
	private String password = configuration.getProperty("exist.password");	
	private String databaseURI = scheme + "://" + domain + ":" + port + "/" + db;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
        Database database = new org.exist.xmldb.DatabaseImpl();
        try {
        	DatabaseManager.registerDatabase(database);
            collection = DatabaseManager.getCollection(databaseURI,username, password);
            service = (CollectionManagementService) collection.getService("CollectionManagementService", "1.0");
        } catch (XMLDBException e) {
        	fail(e.getMessage());
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
	public CollectionCreationAndDeletionTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test creation and deletion of a child collection
	 */
	public final void testCreationAndDeletionOfAChildCollection() {
    	String childCollectionName = "testChildCollection";
    	Collection child = null;
    	try {
        	child = collection.getChildCollection(childCollectionName);
        	service.removeCollection(childCollectionName);
        } catch (XMLDBException e) {
        	 //Thrown if the child collection does not exist
        }

        try {
            service.createCollection(childCollectionName);
        } catch (XMLDBException e) {
        	fail("The collection failed to be added. " + e.getMessage());
        }

        try {
        	child = collection.getChildCollection(childCollectionName);
            assertEquals("/db/" + childCollectionName,child.getName());            
        	service.removeCollection(childCollectionName);
        } catch (XMLDBException e) {
        	fail("The newly added collection could not be retrieved, queried or dropped." + e.getMessage());
        }

	}
	
}
