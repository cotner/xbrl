package org.xbrlapi.data.dom.tests;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xbrlapi.utilities.XBRLException;

/**
 * Test the XBRLAPI Store implementation of DOM recovery methods.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class DocumentRecoveryFromStoreTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public DocumentRecoveryFromStoreTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test the retrieval of a list of URLs
	 */
	public void testGettingURLList() {
		try {
			List<String> urls = store.getStoredURLs();
			assertTrue(urls.size() > 10);
			
			Element e = store.getDocumentAsDOM(urls.get(0));
			assertNotNull(e);
			
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test whether the full store can be recovered as a DOM.
	 */
	public void testGetDOM() {
		try {
			Document d = store.getStoreAsDOM();
			assertNotNull(d);
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test recovery of a single document from a store.
	 */
	public void testGetDocument() {
		try {
			Element root = store.getDocumentAsDOM(this.getURL(STARTING_POINT));
			assertNotNull(root);
			assertEquals(root.getLocalName(),"schema");
		} catch (XBRLException e) {
			fail("Unexpected " + e.getMessage());
		}
	}

}
