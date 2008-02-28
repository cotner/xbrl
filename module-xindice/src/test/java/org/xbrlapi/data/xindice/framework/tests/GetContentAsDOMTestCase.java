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

import org.apache.xindice.client.xmldb.services.CollectionManager;
import org.apache.xindice.xml.dom.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;


public class GetContentAsDOMTestCase extends BaseTestCase {

	// The collection to hold the test fragments
	private Collection collection = null;
	
	private String scheme = configuration.getProperty("xindice.scheme");
	private String domain = configuration.getProperty("xindice.domain");
	private String port = configuration.getProperty("xindice.port");
	private String db = configuration.getProperty("xindice.database");
	private String databaseURI = scheme + "://" + domain + ":" + port + "/" + db;        		

	// Test data
	private String childCollectionName = "XMLResourceTestCollection";
	private String name = "test.xml";
	private String content = "<root attribute='value'>content</root>";
	
	protected void setUp() throws Exception {
		super.setUp();
		
		Collection container = null;
		CollectionManager service = null;
        Database database = new org.apache.xindice.client.xmldb.DatabaseImpl();
        try {
        	DatabaseManager.registerDatabase(database);
        	System.out.println(databaseURI);
            container = DatabaseManager.getCollection(databaseURI);
            service = (CollectionManager) container.getService("CollectionManager", "1.0");
        } catch (XMLDBException e) {
        	fail(e.getMessage());
        }
				
        String configuration =
            "<collection compressed=\"true\" name=\"" + childCollectionName + "\">"
          + "   <filer class=\"org.apache.xindice.core.filer.BTreeFiler\"/>"
          + "</collection>";
        collection = service.createCollection(childCollectionName,DOMParser.toDocument(configuration));
		container.close();
		
		assertNotNull(collection);
		
        // Insert the document to update
		try {
			XMLResource r = (XMLResource) collection.createResource(name, XMLResource.RESOURCE_TYPE);
			r.setContent(content);
			collection.storeResource(r);
		} catch (XMLDBException e) {
			fail("XMLResource creation or storage failed.  " + e.getMessage());
		}		
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		try {
			collection.removeResource(collection.getResource(name));
	        collection.close();
		} catch (XMLDBException e) {
			fail("Unexpected XMLDB exception when tearing down test data.");
		}
		
		// Remove the temporary test collection
		Collection container = DatabaseManager.getCollection(databaseURI);
		CollectionManagementService service = (CollectionManagementService) container.getService("CollectionManagementService", "1.0");
        service.removeCollection(childCollectionName);
        container.close();
	}
	
	
	public GetContentAsDOMTestCase(String arg0) {
		super(arg0);
	}	

	
	/**
	 * Test the features of the DOM retrieved from an XMLResource in eXist.
	 */
	public final void testGetContentAsDOM() {
		
		XMLResource resource = null;
		
		try {
			resource = (XMLResource) collection.getResource(name);
		} catch (XMLDBException e) {
			fail("XML Resource retrieval failed. " + e.getMessage());
		}
		
		if (resource == null) {
			fail("The XML resource that was retrieved is a null.");
		}
			
		try {
			String content = (String) resource.getContent();
			System.out.println(content);
		} catch (XMLDBException e) {
			fail("Getting content of resource as a string failed. " + e.getMessage());
		}
		
		Node node = null;
		try {
			node = resource.getContentAsDOM();
			if (node == null) {
				fail("The content could not be retrieved as a DOM.");
			}
		} catch (XMLDBException e) {
			fail("Getting content of resource as a DOM failed. " + e.getMessage());
		}
		
		Document document = null;
		boolean gotADocument = false;
		try {
			document = node.getOwnerDocument();
			gotADocument = true;
		} catch (ClassCastException e) {
			System.out.println("The node does not cast to document.");
		}
		if (! gotADocument) {
			try {
				document = node.getOwnerDocument();
				if (document == null) {
					fail("No obvious way to get a document from the DOM node.");
				}
			} catch (ClassCastException e) {
				fail("Exception thrown trying to get the document from the DOM node.");
			}			
		}
		
		// Test that the document retrieved is what we expected
        assertEquals("root",document.getDocumentElement().getLocalName());
	}
	
	/**
	 * Test the ability to modify the dom obtained from the resource
	 */
	public final void testChangesToDOMFromXMLResource() {
		try {
			XMLResource resource = (XMLResource) collection.getResource(name);			
			Node node = resource.getContentAsDOM();			
			Document document = node.getOwnerDocument();
			Element element = document.createElement("new");
			document.getDocumentElement().appendChild(element);
			resource.setContentAsDOM(document);
			collection.storeResource(resource);

			resource = (XMLResource) collection.getResource(name);			
			String content = (String) resource.getContent();			
			System.out.println(content);
			
		} catch (Exception e) {
			fail("An unexpected exception was thrown when trying to modify the DOM from an XML Resource.");
		}
	}	
	
}
