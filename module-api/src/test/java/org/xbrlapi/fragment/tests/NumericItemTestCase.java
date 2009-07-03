package org.xbrlapi.fragment.tests;


import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.NumericItem;

/**
 * Tests the implementation of the org.xbrlapi.NumericItem interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NumericItemTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}


	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public NumericItemTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting units
	 */
	public void testGetUnits() {
        try {
            List<NumericItem> items = store.<NumericItem>getXMLResources("SimpleNumericItem");
            assertTrue(items.size() > 0);
            for (NumericItem item: items) {
                assertEquals("unit", item.getUnits().getLocalname());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}

	/**
	 * Test getting precision
	 */
	public void testGetPrecision() {
        try {
            List<NumericItem> items = store.<NumericItem>getXMLResources("SimpleNumericItem");
            assertTrue(items.size() > 0);
            for (NumericItem item: items) {
                assertEquals("2", item.getPrecision());
                assertEquals(true, item.hasPrecision());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test getting decimals
	 */
	public void testGetDecimals() {
        try {
            List<NumericItem> items = store.<NumericItem>getXMLResources("SimpleNumericItem");
            assertTrue(items.size() > 0);
            for (NumericItem item: items) {
                assertEquals(false, item.hasDecimals());
                try {
                    assertEquals("1", item.getDecimals());
                    fail("Exception expected when getting decimals for a fact without them");
                } catch (Exception e) {
                    ;
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}	
	
}
