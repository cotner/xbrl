package org.xbrlapi.fragment.tests;


import org.xbrlapi.DOMLoadingTestCase;
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
		loader.discover(this.getURL(STARTING_POINT));		
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
			NumericItem fact = (NumericItem) store.getFragment("13");
			assertEquals("unit", fact.getUnits().getLocalname());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting precision
	 */
	public void testGetPrecision() {

		try {
			NumericItem fact = (NumericItem) store.getFragment("13");
			assertEquals("2", fact.getPrecision());
			assertEquals(true, fact.hasPrecision());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting decimals
	 */
	public void testGetDecimals() {

		try {
			NumericItem fact = (NumericItem) store.getFragment("13");
			assertEquals(false, fact.hasDecimals());
			try {
				assertEquals("1", fact.getDecimals());
				fail("Exception expected when getting decimals for a fact without them");
			} catch (Exception e) {
				;
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
}
