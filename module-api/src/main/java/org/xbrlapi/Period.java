package org.xbrlapi;


import javax.xml.datatype.XMLGregorianCalendar;

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
    public XMLGregorianCalendar getInstantCalendar() throws XBRLException;
    
    
    
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
    public XMLGregorianCalendar getStartCalendar() throws XBRLException;
    
    /**
     * @return the end moment as a java.util.Calendar value.  Note that the
     * value is adjusted if necessary to reflect the AM/PM specifics of end dates
     * in the XBRL 2.1 specification.
     * @throws XBRLException if the period is not a finite duration.
     * @see java.util.Calendar
     */
    public XMLGregorianCalendar getEndCalendar() throws XBRLException;
    
    /**
     * @return the end of the period.
     * @throws XBRLException if the period is not a finite duration.
     */
    public String getEnd() throws XBRLException;
    
    /**
     * @return true if the end of a finite duration is specified as a date only,
     * with no time (or timezone information) and false otherwise.
     * @throws XBRLException if the period is not a finite duration.
     */
    public boolean endIsDateOnly() throws XBRLException;
 
    /**
     * @return true if the start of a finite duration is specified as a date only,
     * with no time (or timezone information) and false otherwise.
     * @throws XBRLException if the period is not a finite duration.
     */
    public boolean startIsDateOnly() throws XBRLException;
 
    /**
     * @return true if an instant is specified as a date only,
     * with no time (or timezone information) and false otherwise.
     * @throws XBRLException if the period is not an instant.
     */
    public boolean instantIsDateOnly() throws XBRLException;

    /**
     * @return true if the end of a finite duration specifies the timezone and
     *         false otherwise.
     * @throws XBRLException if the period is not a finite duration.
     */
    public boolean endHasTimezone() throws XBRLException;

    /**
     * @return true if the start of a finite duration specifies the timezone and
     *         false otherwise.
     * @throws XBRLException if the period is not a finite duration.
     */
    public boolean startHasTimezone() throws XBRLException;

    /**
     * @return true if an instant specifies the timezone and false otherwise.
     * @throws XBRLException if the period is not an instant.
     */
    public boolean instantHasTimezone() throws XBRLException;

}
