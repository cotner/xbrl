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
