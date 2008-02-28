package org.xbrlapi.data.xindice.framework.tests;

import org.apache.xindice.client.xmldb.services.CollectionManager;
import org.apache.xindice.util.XindiceException;
import org.apache.xindice.xml.dom.DOMParser;
import org.xbrlapi.data.xindice.DBConnectionImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public class CollectionCreationAndDeletionTestCase extends BaseTestCase {

	private DBConnectionImpl connection;
	private Collection collection;
	private CollectionManager service;
	
	private String scheme = configuration.getProperty("xindice.scheme");
	private String domain = configuration.getProperty("xindice.domain");
	private String port = configuration.getProperty("xindice.port");
	private String db = configuration.getProperty("xindice.database");
	private String databaseURI = scheme + "://" + domain + ":" + port + "/" + db;        		
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CollectionCreationAndDeletionTestCase.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
        Database database = new org.apache.xindice.client.xmldb.DatabaseImpl();
        try {
        	DatabaseManager.registerDatabase(database);
        	System.out.println(databaseURI);
            collection = DatabaseManager.getCollection(databaseURI);
            service = (CollectionManager) collection.getService("CollectionManager", "1.0");
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
        	service.dropCollection(childCollectionName);
        } catch (XMLDBException e) {
        	 //Thrown if the child collection does not exist
        }

        // Build up the Collection XML configuration.
        String collectionConfig =
            "<collection compressed=\"true\" name=\"" + childCollectionName + "\">"
          + "    <filer class=\"org.apache.xindice.core.filer.BTreeFiler\"/>"
          + "</collection>";

        try {
            service.createCollection(childCollectionName, DOMParser.toDocument(collectionConfig));
        } catch (XindiceException e) {
        	fail("The collection configuration info could not be parsed into a DOM. " + e.getMessage());
        } catch (XMLDBException e) {
        	fail("The collection failed to be added. " + e.getMessage());
        }

        try {
        	child = collection.getChildCollection(childCollectionName);
            assertEquals(childCollectionName,child.getName());            
        	service.dropCollection(childCollectionName);
        } catch (XMLDBException e) {
        	fail("The newly added collection could not be retrieved, queried or dropped." + e.getMessage());
        }

	}
	
}
