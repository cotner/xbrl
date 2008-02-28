package org.xbrlapi.data.bdbxml.tests;

import java.util.List;
/**
 * Tests of performance with larger data sets.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */
public class DTDDeclarationHandlerTestCase extends BaseTestCase {
	private final String STARTING_POINT = "real.data.schema.with.dtd";
	
	protected void setUp() throws Exception {
		try {
			super.setUp();
			loader.setSchemaLocationAttributeUsage(true);
			loader.discover(getURL(STARTING_POINT));		
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public DTDDeclarationHandlerTestCase(String arg0) {
		super(arg0);
	}


	public void testStorageOfDocumentsWithDTDs() {
		try {
			List<String> urls = store.getStoredURLs();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
