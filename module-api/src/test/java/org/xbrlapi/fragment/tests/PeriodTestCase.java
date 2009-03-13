package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
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
		loader.discover(this.getURI(STARTING_POINT));		
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
            List<Period> periods = store.<Period>getFragments("Period");
            assertTrue(periods.size() > 0);
            for (Period period: periods) {
                assertTrue(period.isFiniteDurationPeriod());
                assertFalse(period.isForeverPeriod());
                assertFalse(period.isInstantPeriod());
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
            List<Period> periods = store.<Period>getFragments("Period");
            assertTrue(periods.size() > 0);
            for (Period period: periods) {
                assertEquals("2001-08-01",period.getStart());
                assertEquals("2001-08-31",period.getEnd());
                try {
                    period.getInstant();
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
