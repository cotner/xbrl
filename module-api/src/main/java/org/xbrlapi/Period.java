package org.xbrlapi;

import java.util.Calendar;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Period extends ContextComponent {

    /**
     * Returns true if the period is a duration.
     *
     * @throws XBRLException
     */
    public boolean isFiniteDurationPeriod() throws XBRLException;

    /**
     * Returns true if the period is an instant.
     *
     * @throws XBRLException
     */
    public boolean isInstantPeriod() throws XBRLException;

    /**
     * Returns true if the period is forever.
     *
     * @throws XBRLException
     */
    public boolean isForeverPeriod() throws XBRLException;

    /**
     * Get the instant.
     * @return the string value of the instant.
     * @throws XBRLException if the period is not an instant.
     */
    public String getInstant() throws XBRLException;    
    
    /**
     * @return the instant moment as a java.util.Calendar value.  Note that the
     * value is adjusted if necessary to reflect the AM/PM specifics of instant dates
     * in the XBRL 2.1 specification.
     * @throws XBRLException if the period is not a finite duration.
     * @see java.util.Calendar
     */
    public Calendar getInstantCalendar() throws XBRLException;
    
    
    
    /**
     * @return the start of the period.
     * @throws XBRLException if the period is not a finite duration.
     */
    public String getStart() throws XBRLException;

    /**
     * @return the start moment as a java.util.Calendar value.
     * @throws XBRLException if the period is not a finite duration.
     * @see java.util.Calendar
     */
    public Calendar getStartCalendar() throws XBRLException;
    
    /**
     * @return the end moment as a java.util.Calendar value.  Note that the
     * value is adjusted if necessary to reflect the AM/PM specifics of end dates
     * in the XBRL 2.1 specification.
     * @throws XBRLException if the period is not a finite duration.
     * @see java.util.Calendar
     */
    public Calendar getEndCalendar() throws XBRLException;
    
    /**
     * @return the end of the period.
     * @throws XBRLException if the period is not a finite duration.
     */
    public String getEnd() throws XBRLException;
    
}
