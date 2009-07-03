package org.xbrlapi.data.dom.tests;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * Test the XML DOM XBRLAPI Store implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class DocumentDeletionTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.label.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public DocumentDeletionTestCase(String arg0) {
		super(arg0);
	}

	public void testDeleteSingleDocument() {
		try {
		    URI documentToDelete = this.getURI(STARTING_POINT);
			store.deleteDocument(documentToDelete);
			assertFalse(store.getDocumentURIs().contains(documentToDelete));
		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testDeleteRelatedDocuments() {
		try {
			int initialSize = store.getDocumentURIs().size();
			store.deleteRelatedDocuments(this.getURI(STARTING_POINT));
			assertFalse(store.getDocumentURIs().contains(this.getURI(STARTING_POINT)));
			assertEquals(initialSize-3,store.getDocumentURIs().size());
		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
}
