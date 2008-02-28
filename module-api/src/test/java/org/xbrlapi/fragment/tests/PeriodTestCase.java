package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Period;

/**
 * Tests the implementation of the org.xbrlapi.Period interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class PeriodTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.period.durations";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public PeriodTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test is period type determination
	 */
	public void testPeriodTypeDetermination() {

		try {
			Period fragment = (Period) store.getFragment("7");
			assertTrue(fragment.isFiniteDuration());
			assertFalse(fragment.isForever());
			assertFalse(fragment.isInstant());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the period information.
	 */
	public void testGetPeriodInformation() {

		try {
			Period fragment = (Period) store.getFragment("7");
			assertEquals("2001-08-01",fragment.getStartDate());
			assertEquals("2001-08-31",fragment.getEndDate());
			try {
				String instant = fragment.getInstant();
				fail("An exception should have been thrown because the period is not an instant.");
			} catch (Exception e) {
				;
			}		
			} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	

}
