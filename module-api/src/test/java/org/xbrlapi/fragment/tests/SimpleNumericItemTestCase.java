package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
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
		loader.discover(this.getURL(STARTING_POINT));		
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
			SimpleNumericItem fact = (SimpleNumericItem) store.getFragment("3");
			assertEquals("5.6", fact.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting inferred precision.
	 */
	public void testGetInferredPrecision() {

		try {
			SimpleNumericItem fact = (SimpleNumericItem) store.getFragment("3");
			assertEquals("5", fact.getInferredPrecision());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test adjusting value for precision.
	 */
	public void testGetPrecisionAdjustedValue() {

		try {
			SimpleNumericItem fact = (SimpleNumericItem) store.getFragment("3");
			assertEquals("5.6", fact.getPrecisionAdjustedValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
