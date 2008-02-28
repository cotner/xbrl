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
 * 
 */

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;


public class SetContentAsDOMTestCase extends BaseTestCase {

	// The collection to hold the test fragments
	private Collection collection = null;
	
	private String scheme = configuration.getProperty("exist.scheme");
	private String domain = configuration.getProperty("exist.domain");
	private String port = configuration.getProperty("exist.port");
	private String db = configuration.getProperty("exist.database");
	private String username = configuration.getProperty("exist.username");
	private String password = configuration.getProperty("exist.password");	
	private String databaseURI = scheme + "://" + domain + ":" + port + "/" + db;

	private String childCollectionName = "testCollection";
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//Establish the connection to the database
		Database database = new org.exist.xmldb.DatabaseImpl();

		DatabaseManager.registerDatabase(database);
		
		//Create a collection to hold the resources
		Collection container = DatabaseManager.getCollection(databaseURI);
		
		CollectionManagementService service = (CollectionManagementService) container.getService("CollectionManagementService", "1.0");
    	
        service.createCollection(childCollectionName);
        collection = DatabaseManager.getCollection(databaseURI + "/testCollection",username,password);
		container.close();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		
		// Remove the temporary test collection
        collection.close();
		Collection container = DatabaseManager.getCollection(databaseURI);
		CollectionManagementService service = (CollectionManagementService) container.getService("CollectionManagementService", "1.0");
        service.removeCollection(childCollectionName);
        container.close();
	}
	
	
	public SetContentAsDOMTestCase(String arg0) {
		super(arg0);
	}	
	
	public final void testSetContentAsDOMWithoutNamespaces() {
		
		Document doc = null;
		System.setProperty(
        		"javax.xml.parsers.DocumentBuilderFactory", 
        		"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//factory.setValidating(false);
		factory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			fail("The DOM parser was not configured correctly to build an XML DOM.");
		}
		doc = builder.newDocument();
		Element rootElem = doc.createElement("element");
		rootElem.setAttribute("attribute", "value of the attribute");
		Element propertyElem = doc.createElement("property");
		propertyElem.setAttribute("key", "value");
		propertyElem.appendChild(doc.createTextNode("text"));
		propertyElem.setAttribute("a", "b");
		propertyElem.setAttribute("c", "d");
		rootElem.appendChild(propertyElem);
		doc.appendChild(rootElem);
		
		XMLResource document = null;
		String documentId = "1";
    	try {
			// Create the resource
    		
			document = (XMLResource) collection.createResource(documentId, XMLResource.RESOURCE_TYPE);
			document.setContentAsDOM(doc);
		} catch (XMLDBException e) {
			fail("XMLResource creation and population with the DOM object failed.");
		}
			
		// Store the document
		try {
			collection.storeResource(document);
		} catch (XMLDBException e) {
			fail("Document storage failed. " + e.getMessage());
		}

		XMLResource resource = null;
		try {
			resource = (XMLResource) collection.getResource(documentId);
		} catch (XMLDBException e) {
			fail("XML Resource retrieval failed. " + e.getMessage());
		}
			
		// Retrieve the content as a string
		String content = null;
		try {
			content = (String) resource.getContent();
		} catch (XMLDBException e) {
			fail("Getting content of resource as a string failed. " + e.getMessage());
		}
		
		// Retrieve the DOM
		doc = null;
		try {
			doc = (Document) resource.getContentAsDOM();
		} catch (XMLDBException e) {
			fail("Getting content of resource as a DOM failed. " + e.getMessage());
		}
		
		// Test that the document retrieved is what we expected
        assertEquals("element",doc.getDocumentElement().getLocalName());

	}
	
	public final void testSetContentAsDOMWithNamespaces() {
			
		Document doc = null;
		System.setProperty(
        		"javax.xml.parsers.DocumentBuilderFactory", 
        		"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//factory.setValidating(false);
		factory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			fail("The DOM parser was not configured correctly to build an XML DOM.");
		}
		doc = builder.newDocument();
		Element rootElem = doc.createElementNS("http://www.xbrlapi.org/","c:element");
		rootElem.setAttribute("attribute", "value of the attribute");
		Element propertyElem = doc.createElementNS("http://www.xbrlapi.org/","c:property");
		propertyElem.setAttribute("key", "value");
		propertyElem.appendChild(doc.createTextNode("text"));
		propertyElem.setAttribute("a", "b");
		propertyElem.setAttribute("c", "d");
		rootElem.appendChild(propertyElem);
		doc.appendChild(rootElem);
		
		XMLResource document = null;
		String documentId = "2";
    	try {
			// Create the resource
    		
			document = (XMLResource) collection.createResource(documentId, "XMLResource");//XMLResource.RESOURCE_TYPE
			document.setContentAsDOM(doc);
		} catch (XMLDBException e) {
			fail("XMLResource creation and population with the DOM object failed.");
		}
			
		// Store the document
		try {
			collection.storeResource(document);
		} catch (XMLDBException e) {
			fail("Document storage failed. " + e.getMessage());
		}

		XMLResource resource = null;
		try {
			resource = (XMLResource) collection.getResource(documentId);
		} catch (XMLDBException e) {
			fail("XML Resource retrieval failed. " + e.getMessage());
		}
			
		// Retrieve the content as a string
		String content = null;
		try {
			content = (String) resource.getContent();
		} catch (XMLDBException e) {
			fail("Getting content of resource as a string failed. " + e.getMessage());
		}
		
		// Retrieve the DOM
		doc = null;
		try {
			doc = (Document) resource.getContentAsDOM();
		} catch (XMLDBException e) {
			fail("Getting content of resource as a DOM failed. " + e.getMessage());
		}
		
		// Test that the document retrieved is what we expected
        assertEquals("element",doc.getDocumentElement().getLocalName());
	}
}
