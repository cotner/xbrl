package org.xbrlapi.data.exist.framework.tests;

/**
 * There is a known problem with the creation of the remote XUpdate service.
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

import org.w3c.dom.Document;
import org.xbrlapi.utilities.BaseTestCase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XUpdateQueryService;


public class XUpdateTestCase extends BaseTestCase {

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
	
  // XUpdate to add an element to the root element
  private String xupdate = 
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
  "<xu:modifications version=\"1.0\" " + 
  "xmlns:xu=\"http://www.xmldb.org/xupdate\">" +
  "<xu:append select=\"/*\" child=\"1\">" +
  "<xu:element name=\"child\"><xu:attribute " + 
  "name=\"type\">home</xu:attribute>value</xu:element>" +
  "</xu:append>" +
  "</xu:modifications>";  
  
	protected void setUp() throws Exception {
		super.setUp();
		
		//Establish the connection to the database
		Database database = new org.exist.xmldb.DatabaseImpl();
		DatabaseManager.registerDatabase(database);
		
		//Create a collection to hold the resources
		Collection container = DatabaseManager.getCollection(databaseURI);
		CollectionManagementService service = (CollectionManagementService) container.getService("CollectionManagementService", "1.0");
  
        service.createCollection(childCollectionName);
        collection = DatabaseManager.getCollection(databaseURI + "/" + childCollectionName,username,password);        
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
	
	
	public XUpdateTestCase(String arg0) {
		super(arg0);
	}	
	
	public final void testXUpdateOnSingleFragment() {

		String name = "test.xml";
		String content = "<root attribute='value'>content</root>";
		XMLResource r;
		try {
			r = (XMLResource) collection.createResource(name, XMLResource.RESOURCE_TYPE);
			r.setContent(content);
			collection.storeResource(r);
		} catch (XMLDBException e) {
			fail("XMLResource creation or storage failed.  " + e.getMessage());
		}

        XUpdateQueryService service = null;
        try {
            service = (XUpdateQueryService) collection.getService("XUpdateQueryService", "1.0");
            long nodesChanged = service.updateResource(name, new String(xupdate));
            assertEquals(1, nodesChanged);
        } catch (XMLDBException e) {
        	fail("The XUpdate process failed.  " + e.getMessage());
        }        
        
		try {
			r = (XMLResource) collection.getResource(name);
			Document d = (Document) r.getContentAsDOM();
			assertEquals(1, d.getDocumentElement().getElementsByTagName("child").getLength());
		} catch (XMLDBException e) {
			fail("XML Resource retrieval failed. " + e.getMessage());
		}

	}
	
}
