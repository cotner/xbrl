package org.xbrlapi.data.exist.tests;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xbrlapi.data.exist.DBConnection;
import org.xbrlapi.data.exist.StoreImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class DBConnectionTestCase extends BaseTestCase {

	private DBConnection connection;
	//private DBConnectionImpl failedConnection;

	private String databaseURI = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		String scheme = configuration.getProperty("exist.scheme");
	    databaseURI = scheme + "://" + host + ":" + port + "/" + database;
        connection = ((StoreImpl) store).getConnection();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this.cleanup(store);
	}

	/**
	 * Constructor for DBConnectionTests.
	 * @param arg0
	 */
	public DBConnectionTestCase(String arg0) {
		super(arg0);
	}

	
	/**
	 * Test the formation of the database URI.
	 */
	public final void testDatabaseURI() {
		try {
			assertEquals("Exist connection databaseURI is faulty.",databaseURI,connection.getDatabaseURI());
		} catch (Exception e) {
			fail("An exception should not be thrown testing the URI of the database connection.");
		}
	}
	







	/**
	 * Test the ability to get an existing root collection
	 */
	public final void testGetRootCollection() {
		try {
	    Collection c = connection.getCollection("/");
		assertEquals("Collection name was " + c.getName(),"/db",c.getName());
		} catch (Exception e) {
			fail("Unexpected exception thrown when testing getCollection");
		}
	}	
	
	/**
	 * Test the ability to get an existing collection.
	 */
	public final void testGetCollection() {
		try {
		Collection c = connection.getCollection("/system");
		assertEquals("/db/system",c.getName());	
		} catch (Exception e) {
			fail("Unexpected exception thrown when testing a non-root collection.");
		}
	}		
	
	/**
	 * Test the ability to get an existing nested collection
	 */
	public final void testGetNestedCollection() {
		try {
	    Collection c = connection.getCollection("/system/config");
		assertEquals("/db/system/config",c.getName());	
		} catch (Exception e) {
			fail("Unexpected exception thrown when testing getCollection");
		}
	}	

	/**
	 * Test the ability to get an existing nested collection given parent collection
	 */
	public final void testGetNestedCollectionGivenParentCollection() {
		try {
	    Collection c = connection.getCollection("/system");
	    c = connection.getCollection("config", c);
		assertEquals("/db/system/config",c.getName());	
		} catch (Exception e) {
			fail("Unexpected exception thrown.");
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
	 * Test the creation of a root level collection 
	 */
	public final void testCreateRootLevelCollection() throws Exception {
		try {			
			Collection c = connection.createRootCollection("rootCollection");
			assertEquals("/db/rootCollection",c.getName().toString());
		} catch (XBRLException e) {
			fail("The collection failed to be created. " + e.getMessage());
		} finally {
			try {
				connection.deleteCollection("/rootCollection");
			} catch (Exception e) {
				// No collection to delete perhaps
			}
		}
		
	}

	/**
	 * Test the creation of a nested collection 
	 */
	public final void testCreateNestedCollection() throws Exception {

		try {
			Collection parentCollection = null;
			try {			
				parentCollection = connection.createRootCollection("parentCollection");
			} catch (XBRLException e) {
				fail("The collection failed to be created. " + e.getMessage());
			} 
			
			Collection nestedCollection = connection.createCollection("nestedCollection",parentCollection);
			assertEquals("/db/parentCollection/nestedCollection",nestedCollection.getName().toString());
		} finally {
			try {
				connection.deleteCollection("/parentCollection");
			} catch (Exception e) {
				// No collection to delete perhaps
			}
		}
	}

	public final void testDeleteEmptyCollection() {
		try {
			Collection c = connection.createRootCollection("emptyCollection");
			assertEquals("/db/emptyCollection",c.getName());
			connection.deleteCollection("/emptyCollection");
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when deleting an empty collection.");
		} catch (XMLDBException e) {
			fail("Unexpected XML DB exception when getting the name of an empty collection.");			
		}
		
		try {
			Collection c = connection.getCollection("/emptyCollection");
			assertNull("The empty collection was deleted but could still be instantiated.",c);
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when instantiating a deleted collection.");
		} finally {
			try {
				connection.deleteCollection("/emptyCollection");
			} catch (XBRLException e) {
				;
			}
		}
	}

	public final void testDeleteNonEmptyCollection() {
		try {
			Collection c = connection.createRootCollection("nonEmptyCollection");
			XMLResource document = (XMLResource) c.createResource("2", XMLResource.RESOURCE_TYPE);
            document.setContent("<root><child/></root>");
			c.storeResource(document);			
			assertEquals(1,c.getResourceCount());
			c.close();
			connection.deleteCollection("/nonEmptyCollection");
			c = connection.getCollection("/nonEmptyCollection");
			assertNull("The non empty collection was not deleted because it could still be instantiated.", c);
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when deleting a non-empty collection.");
		} catch (XMLDBException e) {
			fail("Unexpected XML DB exception when getting the name of a collection to verify a non-empty collection deletion.");
		} finally {
			try {
				connection.deleteCollection("/nonEmptyCollection");
			} catch (XBRLException e) {
				;
			}			
		}
	}

	public final void testStoreXMLStringResource() {
		
		try {
			Collection c = connection.createRootCollection("stringXMLCollection");
			XMLResource document = (XMLResource) c.createResource("document.xml", XMLResource.RESOURCE_TYPE);
            document.setContent("<root><child/></root>");
			c.storeResource(document);
			Document dom = (Document) ((XMLResource) c.getResource("document.xml")).getContentAsDOM();
            assertEquals("root",dom.getDocumentElement().getLocalName());
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when retrieving an XMLResource.");
		} catch (XMLDBException e) {
			fail("Unexpected XML DB exception when retrieving an XMLResource.");
		} finally {
			try {
				connection.deleteCollection("/stringXMLCollection");
			} catch (XBRLException e) {
				;
			}
		}
	}

	public final void testStoreXMLDOMResource() {
		try {
			
	        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");        
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document d = builder.newDocument();
            d.appendChild(d.createElementNS("http://www.xbrlapi.org/","c:root"));
			
            Collection c = connection.createRootCollection("xmlNamespacesCollection");
			XMLResource document = (XMLResource) c.createResource("indexValue", XMLResource.RESOURCE_TYPE);
			document.setContentAsDOM(d);
			c.storeResource(document);
			String id = document.getId();
			assertEquals("The resource index was not set properly.","indexValue",id);
			Document dom = (Document) ((XMLResource) c.getResource(id)).getContentAsDOM();
            assertEquals("root",dom.getDocumentElement().getLocalName());
			c.close();
		} catch (ParserConfigurationException e) {
			fail("Unexpected parser configuration error when storing an XML Resource.");
		} catch (XBRLException e) {
			fail("Unexpected XBRL API exception when storing an XML Resource.");
		} catch (XMLDBException e) {
			fail("Unexpected XML DB exception when storing an XML Resource." + e.getMessage());
		} finally {
			try {
				connection.deleteCollection("/xmlNamespacesCollection");
			} catch (XBRLException e) {
				fail("Could not even clean up the test collection.");
			}
		}
	}
	
}
