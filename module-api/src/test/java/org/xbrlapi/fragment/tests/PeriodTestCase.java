package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
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
            FragmentList<Period> periods = store.<Period>getFragments("Period");
            assertTrue(periods.getLength() > 0);
            for (Period period: periods) {
                assertTrue(period.isFiniteDuration());
                assertFalse(period.isForever());
                assertFalse(period.isInstant());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
	}
	
	/**
	 * Test getting the period information.
	 */
	public void testGetPeriodInformation() {

        try {
            FragmentList<Period> periods = store.<Period>getFragments("Period");
            assertTrue(periods.getLength() > 0);
            for (Period period: periods) {
                assertEquals("2001-08-01",period.getStartDate());
                assertEquals("2001-08-31",period.getEndDate());
                try {
                    String instant = period.getInstant();
                    fail("An exception should have been thrown because the period is not an instant.");
                } catch (Exception e) {
                    ;
                }       
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
}
