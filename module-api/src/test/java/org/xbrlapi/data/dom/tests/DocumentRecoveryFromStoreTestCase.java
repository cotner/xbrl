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
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public DocumentRecoveryFromStoreTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test the retrieval of a list of URIs
	 */
	public void testGettingURIList() {
		try {
			List<String> uris = store.getStoredURIs();
			assertTrue(uris.size() > 0);
			
			boolean foundStartingPoint = false;
			for (String uri: uris) {
			    if (uri.equals(getURI(this.STARTING_POINT))) {
			        foundStartingPoint = true;
			    }
			}
			assertTrue(foundStartingPoint);
			
			Element e = store.getDocumentAsDOM(uris.get(0));
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
			Element root = store.getDocumentAsDOM(this.getURI(STARTING_POINT));
			assertNotNull(root);
			assertEquals(root.getLocalName(),"schema");
		} catch (XBRLException e) {
			fail("Unexpected " + e.getMessage());
		}
	}

}
