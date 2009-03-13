package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.NonNumericItem;

/**
 * Tests the implementation of the org.xbrlapi.NonNumericItem interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NonNumericItemTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public NonNumericItemTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting all child facts.
	 */
	public void testGetAllChildFacts() {
		try {
		    List<NonNumericItem> items = store.<NonNumericItem>getFragments("NonNumericItem");
		    assertTrue(items.size() > 0);
		    for (NonNumericItem fact: items) {
		        if (fact.getLocalname().equals("managementTitle") ) {
		            assertEquals("My Title", fact.getValue().substring(0,fact.getValue().length()-1));
		        }
		    }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
}
