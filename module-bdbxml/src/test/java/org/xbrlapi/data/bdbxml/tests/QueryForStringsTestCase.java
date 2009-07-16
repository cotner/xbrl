package org.xbrlapi.data.bdbxml.tests;

import java.util.Set;

import org.xbrlapi.data.bdbxml.BaseTestCase;

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
		    
	        String query = "for $root in #roots# return string($root/@index)";
	        Set<String> results = store.queryForStrings(query);
	        assertTrue(results.size() > 1);
/*	        for (String result: results) {
	            logger.info(result);
	            if (result.length() > 10) {
	                store.serialize(store.getXMLResource(result));
	            }
	        }
*/		} catch (Exception e) {
            e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
