package org.xbrlapi.data.xindice.tests;

import java.util.List;

/**
 * Tests the query for strings method.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class QueryForStringsTestCase extends BaseTestCase {
	private final String START = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public QueryForStringsTestCase(String arg0) {
		super(arg0);
	}
	
	public void testQueryForFragmentIndices() {
		try {
	        loader.discover(this.getURI(START));
		    
	        String query = "/*/@index";
	        List<String> results = store.queryForStrings(query);
	        assertTrue(results.size() > 1);
	        for (String result: results) {
	            logger.info(result);
	        }
		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
