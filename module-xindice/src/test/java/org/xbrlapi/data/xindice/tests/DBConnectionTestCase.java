package org.xbrlapi.data.xindice.tests;

import org.xbrlapi.data.xindice.DBConnectionImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 * Tests connections to a Xindice database
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class DBConnectionTestCase extends BaseTestCase {

	private DBConnectionImpl connection;
	//private DBConnectionImpl failedConnection;

	private final String scheme = configuration.getProperty("xindice.scheme");
	private final String domain = configuration.getProperty("xindice.domain");
	private final String port = configuration.getProperty("xindice.port");
	private final String database = configuration.getProperty("xindice.database");
	private final String databaseURI = scheme + "://" + domain + ":" + port + "/" + database;
	
	protected void setUp() throws Exception {
		super.setUp();		
		connection = this.createConnection();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		this.cleanup(connection);
	}

	public DBConnectionTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the formation of the database URI.
	 */
	public final void testDatabaseURI() {
		try {
		    logger.info(connection.getDatabaseURI());
			assertEquals("Xindice connection databaseURI is faulty.",databaseURI,connection.getDatabaseURI());
		} catch (Exception e) {
		    e.printStackTrace();
			fail("An exception should not be thrown testing the URI of the database connection.");
		}
	}
	
	/**
	 * Test the successful creation of a database connection.
	 */
	public final void testDBConnection() {
		try {
			new DBConnectionImpl(domain,port,database);
		} catch (XBRLException e) {
			fail("Failed to create a new database connection.");
		}
	}	
	
	/**
	 * Test the failed creation of a database connection to a non-existent machine.
	 */
	public final void testDBConnection_FailsOnBadDomain() throws Exception {
		try {
			new DBConnectionImpl("rubbish.xbrlapi.org", port, database);
			fail("DB Connection was established despite incorrect host name for DB server.");
		} catch (XBRLException expected) {
		}
	}
	
	/**
	 * Test the failed creation of a database connection to a machine on
	 * an incorrect port
	 */
	public final void testDBConnection_FailsOnPortError() throws Exception {
		try {
			new DBConnectionImpl(domain, "8780", database);
			fail("DB Connection was established despite incorrect port.");
		} catch (XBRLException expected) {			
		}
	}

	/**
	 * Test the failed creation of a database connection to a machine with 
	 * an incorrect database name
	 */
	public final void testDBConnection_FailsOnDatabaseName() throws Exception {
		try {
			new DBConnectionImpl(domain, port, "wrongDBName");
			fail("DB Connection was established despite incorrect name for database.");
		} catch (XBRLException expected) {
		}
	}

	/**
	 * Test the ability to get the root collection.
	 */
	public final void testGetRootCollection() {
		try {
		Collection c = connection.getCollection("/");
		assertEquals("db",c.getName());	
		} catch (Exception e) {
			fail("Unexpected exception thrown when getting the root collection.");
		}
	}
	
	/**
	 * Test the ability to get an existing collection.
	 */
	public final void testGetCollection() {
		try {
		Collection c = connection.getCollection("/system");
		assertEquals("Name of collection was actually " + c.getName(),"system",c.getName());
		} catch (Exception e) {
			fail("Unexpected exception thrown when testing a non-root collection.");
		}
	}
	
	/**
	 * Test the ability to get an existing nested collection given parent collection.
	 */
	public final void testGetNestedCollectionGivenParentCollection() {
		try {
	    Collection c = connection.getCollection("/");
	    c = connection.getCollection("system", c);
		assertEquals("Name of collection was actually " + c.getName(),"system",c.getName());
		} catch (Exception e) {
			fail("Unexpected exception thrown when getting a child collection from a parent collection.");
		}
	}	
	
	/**
	 * Test the ability to return null when getting a non-existent collection
	 */
	public final void testReturnNullWhenGettingANonExistentCollection() {
		try {
	    Collection c = connection.getCollection("/doesnotexist");
		assertNull(c);
		} catch (Exception e) {
			fail("Unexpected exception thrown when testing getCollection for a non-existent collection");
		}
	}
	
	/**
	 * Test the creation of a root level collection.
	 */
	public final void testCreateRootLevelCollection() throws Exception {
		try {
			Collection c = connection.createRootCollection("testCollection");
			assertEquals("testCollection",c.getName().toString());
		} catch (XBRLException e) {
			fail("The collection failed to be created");
		} finally {
			connection.deleteCollection("/testCollection");			
		}
	}

	/**
	 * Test the creation and deletion of a nested collection.
	 */
	public final void testCreateNestedCollection() throws Exception {
		try {
			Collection c = connection.createRootCollection("testCollection");
			c = connection.createCollection("nestedCollection",c);			
			assertEquals("nestedCollection",c.getName().toString());
		} catch (XBRLException e) {
			fail("The nested collection failed to be created.");
		}
		try {
			connection.deleteCollection("/testCollection");
		} catch (XBRLException e) {
			fail("The nested collection failed to be deleted.");
		}
	}

	/**
	 * Test the deletion of an empty collection.
	 */
	public final void testDeleteEmptyCollection() {
		
		Collection c = null;

		try {
			c = connection.createRootCollection("testCollection");
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when creating a new empty collection.");
		}

		try {
			assertEquals("testCollection",c.getName());
		} catch (XMLDBException e) {
			fail("Unexpected XMLDB exception when getting the name of a collection.");
		}
		
		
		try {
			connection.deleteCollection("/testCollection");
			c = connection.getCollection("/testCollection");
			assertNull("A deleted collection could still be retrieved. That  is a problem.", c);
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when deleting an empty collection.");
		}
		
	}

	/** 
	 * Test the deletion of a non-empty collection.
	 */
	public final void testDeleteNonEmptyCollection() {
		Collection c = null;
		
		try {
			c = connection.createRootCollection("testCollection");
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when creating an empty collection.");
		}
		
		try {
			XMLResource document = (XMLResource) c.createResource("2", "XMLResource");
            document.setContent("<root><child/></root>");
			c.storeResource(document);
		} catch (XMLDBException e) {
			fail("Unexpected XML DB exception when creating and storing a resource in the new collection.");
		}

		try {
			assertEquals(1,c.getResourceCount());
		} catch (XMLDBException e) {
			fail("Unexpected XML DB exception when getting the number of resources in the collection.");
		}
	
		try {
			c.close();
		} catch (XMLDBException e) {
			fail("Unexpected XML DB closing and deleting a collection.");
		}

		try {
			connection.deleteCollection("/testCollection");
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when deleting a non-empty collection.");
		}
		
		try {
			c = connection.getCollection("/testCollection");
			assertNull("The deletion of the collection must have failed because it could still be retrieved.", c);
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when retrieving a collection that was supposed to be deleted.");
		}
	}

	/**
	 * Test the storing of an XML String resource.
	 */
	public final void testStoreXMLStringResource() {
		try {
			Collection c = connection.createRootCollection("testCollection");
			XMLResource document = (XMLResource) c.createResource("indexValue", "XMLResource");
            document.setContent("<root><child/></root>");
			c.storeResource(document);
			String id = document.getId();
			org.w3c.dom.Document dom = (org.w3c.dom.Document) ((XMLResource) c.getResource(id)).getContentAsDOM();
            assertEquals("root",dom.getDocumentElement().getTagName());
			c.close();
			connection.deleteCollection("/testCollection");
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when storing an XML Resource.");
		} catch (XMLDBException e) {
			fail("Unexpected XML DB exception when storing an XML Resource.");
		}
	}


	
}
