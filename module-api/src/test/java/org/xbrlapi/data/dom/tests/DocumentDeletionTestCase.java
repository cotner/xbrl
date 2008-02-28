package org.xbrlapi.data.dom.tests;

import org.xbrlapi.utilities.XBRLException;

/**
 * Test the XML DOM XBRLAPI Store implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class DocumentDeletionTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.label.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public DocumentDeletionTestCase(String arg0) {
		super(arg0);
	}

	public void testDeleteSingleDocument() {
		try {
			store.deleteDocument(this.getURL(STARTING_POINT));
			assertFalse(store.getStoredURLs().contains(this.getURL(STARTING_POINT)));
		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testDeleteRelatedDocuments() {
		try {
			int initialSize = store.getStoredURLs().size();
			store.deleteRelatedDocuments(this.getURL(STARTING_POINT));
			assertFalse(store.getStoredURLs().contains(this.getURL(STARTING_POINT)));
			assertEquals(initialSize-3,store.getStoredURLs().size());
		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
}
