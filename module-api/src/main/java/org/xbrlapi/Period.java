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
     * @return the start of the period.
     * @throws XBRLException if the period is not a finite duration.
     */
    public String getStart() throws XBRLException;

    /**
     * @return the end of the period.
     * @throws XBRLException if the period is not a finite duration.
     */
    public String getEnd() throws XBRLException;
    
}
