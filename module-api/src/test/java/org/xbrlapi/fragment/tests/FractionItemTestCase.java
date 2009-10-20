package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FractionItem;
import org.xbrlapi.impl.FractionItemImpl;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FractionItemTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.local.xbrl.instance.tuples.with.units";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.stashURI(this.getURI(STARTING_POINT));
		loader.discoverNext();		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public FractionItemTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test adjusting value for precision.
	 */
	public void testGetNumerator() {

        try {
            List<FractionItem> items = store.<FractionItem>getXMLResources(FractionItemImpl.class);
            assertTrue(items.size() > 0);
            for (FractionItem item: items) {
                assertEquals(1.0, item.getNumerator());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
}
