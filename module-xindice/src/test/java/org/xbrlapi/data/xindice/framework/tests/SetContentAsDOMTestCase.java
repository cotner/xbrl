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
 * 
The setContentAsDOM method serialises the DOM node to obtain a text
string.  That serialisation is done with the TextWriter class, which
seems to be a roll-your-own serialisation class that largely ignores
namespace declarations in the serialisation process.  This means that
when the string needs to be parsed as XML, it is not valid XML.

You can get around this easily enough by using the Xerces XML serialiser
as per the method below.

     * Convert a DOM object to a string.
     * @param dom The DOM object to convert to a string.
     * @return The string that is the serialised content of the DOM
object.
     * @throws XBRLException if an IO exception occurs.
    private String DOM2String(Document node) throws XBRLException {
    try {
        StringWriter sw = new StringWriter();
org.apache.xml.serialize.OutputFormat format = new
org.apache.xml.serialize.OutputFormat("xml", "UTF-8", true);
org.apache.xml.serialize.XMLSerializer output = new
org.apache.xml.serialize.XMLSerializer(sw, format);
output.setNamespaces(true);
output.serialize(node);
        sw.flush();
        String out = sw.toString();
        sw.close();
        return out;
    } catch (IOException e) {
    ...
    }
    
    }

This seems to handle all of the namespace issues nicely enough (though it is
not tested passing in any DOM node rather than passing in a
Document node), leaving open the question: why not use this (or the
Xalan 2.7 serialiser [the 2.6 Xalan
serialiser has some namespace issues of its own]) instead of using the
TextWriter class? 
 */

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xindice.client.xmldb.services.CollectionManager;
import org.apache.xindice.xml.dom.DOMParser;
import org.w3c.dom.Document;
import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.modules.XMLResource;


public class SetContentAsDOMTestCase extends BaseTestCase {

	// The collection to hold the test fragments
	private Collection collection = null;
	
	// The root URI for the Xindice database	
	private String scheme = configuration.getProperty("xindice.scheme");
	private String domain = configuration.getProperty("xindice.domain");
	private String port = configuration.getProperty("xindice.port");
	private String db = configuration.getProperty("xindice.database");
	private String databaseURI = scheme + "://" + domain + ":" + port + "/" + db;        			

	private String childCollectionName = "testCollection";
	
	public SetContentAsDOMTestCase(String arg0) {
		super(arg0);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//Establish the connection to the database
		Database database = new org.apache.xindice.client.xmldb.DatabaseImpl();

		DatabaseManager.registerDatabase(database);
		
		//Create a collection to hold the resources
		Collection container = DatabaseManager.getCollection(databaseURI);
		
    	CollectionManager service = (CollectionManager) container.getService("CollectionManager", "1.0");
        
        String collectionConfig =
            "<collection compressed=\"true\" name=\"" + childCollectionName + "\">"
          + "    <filer class=\"org.apache.xindice.core.filer.BTreeFiler\"/>"
          + "</collection>";
    	
        service.createCollection(childCollectionName, DOMParser.toDocument(collectionConfig));
        collection = DatabaseManager.getCollection(databaseURI + "/testCollection");
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
    	CollectionManager service = (CollectionManager) container.getService("CollectionManager", "1.0");
        service.dropCollection(childCollectionName);
        container.close();
	}
	
	public final void testSetContentAsDOMWithoutNamespaces() {
		try {

			// Build the DOM document to be stored in the database
	        System.setProperty(
	        		"javax.xml.parsers.DocumentBuilderFactory", 
	        		"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");        
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document d = builder.newDocument();
			d.appendChild(d.createElement("root"));

			// Create the resource
			// TODO Adapt Xindice store etc to use XMLResource.RESOURCE_TYPE when creating resources instead of hard coded "XMLResource".			
			XMLResource document = (XMLResource) collection.createResource("1", XMLResource.RESOURCE_TYPE);
			String id = document.getId();
			document.setContentAsDOM(d);
			
			// Store the document
			try {
				collection.storeResource(document);
			} catch (Exception e) {
				fail("Document storage failed. " + e.getMessage());
			}

			// Retrieve the document
			Document dom = (Document) ((XMLResource) collection.getResource(id)).getContentAsDOM();
		
			// Test that the document retrieved is what we expected
            assertEquals("root",dom.getDocumentElement().getLocalName());
	
		} catch (Exception e) {
			fail("No luck with XML with no namespaces.");
		}
	}
	
	public final void testSetContentAsDOMWithNamespaces() {
		
		// TODO Fix setContentAsDOM in Xindice when namespaces are used.
/*		try {
			// Build the DOM document to be stored in the database
	        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			assertNotNull("Document builder factory is null.",factory);
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			assertNotNull("Document builder is null.",builder);
			Document d = builder.newDocument();
			assertNotNull("Document is null.",d);
            d.appendChild(d.createElementNS("http://www.xbrlapi.org/","c:root"));
            
            System.out.println(d.getDocumentElement().getNamespaceURI());
            
			// Create the resource
			XMLResource document = (XMLResource) collection.createResource("2", XMLResource.RESOURCE_TYPE);
			String id = document.getId();
			document.setContentAsDOM(d.getDocumentElement());
			//document.setContentAsDOM(d);
			
			// Store the document
			try {
				collection.storeResource(document);
			} catch (Exception e) {
				e.printStackTrace();
				fail("Document storage failed. " + e.getMessage());
			}
			
			// Retrieve the document
			Document dom = (Document) ((XMLResource) collection.getResource(id)).getContentAsDOM();
            assertEquals("root",dom.getDocumentElement().getLocalName());
		} catch (Exception e) {
			fail("No luck with resources involving namespaces.");
		}*/
	}
}
