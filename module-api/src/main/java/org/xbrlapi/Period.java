package org.xbrlapi;

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
    public boolean isFiniteDuration() throws XBRLException;

    /**
     * Returns true if the period is an instant.
     *
     * @throws XBRLException
     */
    public boolean isInstant() throws XBRLException;

    /**
     * Returns true if the period is forever.
     *
     * @throws XBRLException
     */
    public boolean isForever() throws XBRLException;

    /**
     * Set the period using a moment.
     * @param moment The string value of the moment.
     * @throws XBRLException
     */
    public void setInstant(String moment) throws XBRLException;    
    
    /**
     * Set the period using a start date and end date.
     * @param startDate The start date
     * @param endDate The end date
     * @throws XBRLException
     */
    public void setFiniteDuration(String startDate, String endDate) throws XBRLException;

    /**
     * Set the period to being forever.
     *
     * @throws XBRLException
     */
    public void setForever() throws XBRLException;
    
    /**
     * Get the instant.
     * @return the string value of the instant.
     * @throws XBRLException if the period is not an instant.
     */
    public String getInstant() throws XBRLException;    
    
    /**
     * Get the start date.
     * @return the start date value.
     * @throws XBRLException if the date is not a duration that is not a finite duration.
     */
    public String getStartDate() throws XBRLException;

    /**
     * Get the end date.
     * @return the end date value.
     * @throws XBRLException if the date is not a duration that is not a finite duration.
     */
    public String getEndDate() throws XBRLException;
    
}
