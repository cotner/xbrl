package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.SimpleNumericItem;

/**
 * Tests the implementation of the org.xbrlapi.SimpleNumericItem interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SimpleNumericItemTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.decimals";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public SimpleNumericItemTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting value
	 */
	public void testGetValue() {
        try {
            List<SimpleNumericItem> items = store.<SimpleNumericItem>gets("SimpleNumericItem");
            assertTrue(items.size() > 0);
            for (SimpleNumericItem item: items) {
                assertEquals("5.6", item.getValue());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test getting inferred precision.
	 */
	public void testGetInferredPrecision() {
        try {
            List<SimpleNumericItem> items = store.<SimpleNumericItem>gets("SimpleNumericItem");
            assertTrue(items.size() > 0);
            for (SimpleNumericItem item: items) {
                assertEquals("5", item.getInferredPrecision());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test adjusting value for precision.
	 */
	public void testGetPrecisionAdjustedValue() {

        try {
            List<SimpleNumericItem> items = store.<SimpleNumericItem>gets("SimpleNumericItem");
            assertTrue(items.size() > 0);
            for (SimpleNumericItem item: items) {
                assertEquals("5.6", item.getPrecisionAdjustedValue());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
}
