package org.xbrlapi.data.exist.tests.framework;
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
	
	private String scheme = null;
	private String domain = null;
	private String port = null;
	private String db = null;
	private String username = null;
	private String password = null;	
	private String databaseURI = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	    scheme = configuration.getProperty("exist.scheme");
	    domain = configuration.getProperty("exist.domain");
	    port = configuration.getProperty("exist.port");
	    db = configuration.getProperty("exist.database");
	    username = configuration.getProperty("exist.username");
	    password = configuration.getProperty("exist.password");  
	    databaseURI = scheme + "://" + domain + ":" + port + "/" + db;		
        
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
