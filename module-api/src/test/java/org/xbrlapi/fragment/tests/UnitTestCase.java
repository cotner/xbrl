package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Unit;

/**
 * Tests the implementation of the org.xbrlapi.Unit interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class UnitTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.units.simple";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public UnitTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test checking for denopminator.
	 */
	public void testHasDenominator() {

		try {
			Unit fragment = (Unit) store.getFragment("9");
			assertFalse(fragment.hasDenominator());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting numerator measures.
	 */
	public void testGetNumeratorMeasures() {

		try {
			Unit fragment = (Unit) store.getFragment("9");
			assertEquals(2,fragment.getNumeratorMeasures().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting denominator measures.
	 */
	public void testGetDenominatorMeasures() {

		try {
			Unit fragment = (Unit) store.getFragment("9");
			assertNull(fragment.getDenominatorMeasures());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test checking equality.
	 */
	public void testCheckEquality() {

		try {
			Unit fragment = (Unit) store.getFragment("9");
			assertTrue(fragment.equals(fragment));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
