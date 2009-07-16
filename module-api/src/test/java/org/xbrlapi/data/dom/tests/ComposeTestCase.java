package org.xbrlapi.data.dom.tests;

import org.w3c.dom.Document;
import org.xbrlapi.utilities.XBRLException;

/**
 * Test the XBRLAPI Store composition algorithm.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class ComposeTestCase extends BaseTestCase {
	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public ComposeTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test the composition of a Store.
	 */
	public final void testCompose() {
		try {
			Document d = store.getCompositeDocument();
			assertEquals("dts",d.getDocumentElement().getLocalName());			
		} catch (XBRLException e) {
			e.printStackTrace();
			fail("Unexpected exception thrown.");
		}
	}

}
