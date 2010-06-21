package org.xbrlapi.fragment.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.xbrlapi.Context;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Period;
import org.xbrlapi.impl.ContextImpl;

/**
 * Tests the implementation of the org.xbrlapi.Period interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class PeriodTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.period.durations";

    private final String INSTANCE = "test.data.local.xbrl.period.interpretation";
    private final String DATE_ONLY = "date-only";
    private final String DATE_AND_TIME = "date-and-time";
    private final String DATE_AND_FRACTIONAL_TIME = "date-and-fractional-time";
    private final String UTC_DATE_AND_TIME = "utc-date-and-time";
    private final String UTC_OFFSET_DATE_AND_TIME = "utc-offset-date-and-time";
    private final String INFERRED_END = "inferred-end";
    private final String INFERRED_START_AND_END = "inferred-start-and-end";

	protected void setUp() throws Exception {
		super.setUp();
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
            loader.discover(this.getURI(STARTING_POINT));       
            List<Period> periods = store.<Period>getXMLResources("Period");
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
            loader.discover(this.getURI(STARTING_POINT));       
            List<Period> periods = store.<Period>getXMLResources("Period");
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
	
    public void testPeriodInterpretations() {
        try {

            // Load and retrieve the facts
            loader.discover(this.getURI(INSTANCE));
            Map<String,Context> contexts = new HashMap<String,Context>();
            for (Context context: store.<Context>getXMLResources(ContextImpl.class)) {
                contexts.put(context.getId(),context);
            }
            assertTrue(contexts.size() > 0);
            
            Period period = contexts.get(DATE_ONLY).getPeriod();
            XMLGregorianCalendar calendar = period.getInstantCalendar();
            assertEquals(2008,calendar.getYear());
            assertEquals(1,calendar.getMonth());
            assertEquals(1,calendar.getDay());
            assertTrue(period.instantIsDateOnly());
            assertFalse(period.instantHasTimezone());

            period = contexts.get(DATE_AND_TIME).getPeriod();
            calendar = period.getInstantCalendar();
            assertEquals(2007,calendar.getYear());
            assertEquals(12,calendar.getMonth());
            assertEquals(31,calendar.getDay());
            assertEquals(11, calendar.getHour());
            assertEquals(35, calendar.getMinute());
            assertEquals(01, calendar.getSecond());
            assertEquals(null, calendar.getFractionalSecond());
            assertFalse(period.instantIsDateOnly());
            assertFalse(period.instantHasTimezone());

            period = contexts.get(DATE_AND_FRACTIONAL_TIME).getPeriod();
            calendar = period.getInstantCalendar();
            assertEquals(2007,calendar.getYear());
            assertEquals(12,calendar.getMonth());
            assertEquals(31,calendar.getDay());
            assertEquals(11, calendar.getHour());
            assertEquals(35, calendar.getMinute());
            assertEquals(01, calendar.getSecond());
            assertEquals("0.0035", calendar.getFractionalSecond().toString());
            assertFalse(period.instantIsDateOnly());
            assertFalse(period.instantHasTimezone());
            
            period = contexts.get(UTC_DATE_AND_TIME).getPeriod();
            calendar = period.getInstantCalendar();
            assertEquals(2007,calendar.getYear());
            assertEquals(12,calendar.getMonth());
            assertEquals(31,calendar.getDay());
            assertEquals(11, calendar.getHour());
            assertEquals(35, calendar.getMinute());
            assertEquals(01, calendar.getSecond());
            assertNull(calendar.getFractionalSecond());
            assertFalse(period.instantIsDateOnly());
            assertTrue(period.instantHasTimezone());
            assertEquals(0, period.getInstantCalendar().getTimeZone(0).getRawOffset());

            period = contexts.get(UTC_OFFSET_DATE_AND_TIME).getPeriod();
            calendar = period.getInstantCalendar();
            assertEquals(2007,calendar.getYear());
            assertEquals(12,calendar.getMonth());
            assertEquals(31,calendar.getDay());
            assertEquals(11, calendar.getHour());
            assertEquals(00, calendar.getMinute());
            assertEquals(00, calendar.getSecond());
            assertNull(calendar.getFractionalSecond());
            assertFalse(period.instantIsDateOnly());
            assertTrue(period.instantHasTimezone());
            assertEquals(14400000, period.getInstantCalendar().getTimeZone(0).getRawOffset());
            
            period = contexts.get(this.INFERRED_END).getPeriod();
            calendar = period.getInstantCalendar();
            assertEquals(2008,calendar.getYear());
            assertEquals(1,calendar.getMonth());
            assertEquals(1,calendar.getDay());
            assertTrue(period.instantIsDateOnly());
            assertFalse(period.instantHasTimezone());
            
            period = contexts.get(this.INFERRED_START_AND_END).getPeriod();
            calendar = period.getEndCalendar();
            assertEquals(2008,calendar.getYear());
            assertEquals(1,calendar.getMonth());
            assertEquals(1,calendar.getDay());
            assertTrue(period.endIsDateOnly());
            assertFalse(period.endHasTimezone());
            calendar = period.getStartCalendar();
            assertEquals(2007,calendar.getYear());
            assertEquals(1,calendar.getMonth());
            assertEquals(1,calendar.getDay());
            assertTrue(period.startIsDateOnly());
            assertFalse(period.startHasTimezone());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    

}
