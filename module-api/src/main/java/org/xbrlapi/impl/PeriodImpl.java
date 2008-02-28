package org.xbrlapi.impl;

import org.xbrlapi.Period;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Implementation of context period fragments for instants and durations.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class PeriodImpl extends ContextComponentImpl implements Period {

	/**
	 * Returns true if the period describes a duration.
	 * @return true if the period describes a duration and false otherwise.
	 * @throws XBRLException.
	 * @see org.xbrlapi.Period#isFiniteDuration()
	 */
	public boolean isFiniteDuration() throws XBRLException {
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"instant").getLength() == 1) return false;
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"forever").getLength() == 1) return false;
		return true;
	}


	/**
	 * Returns true if the period describes an instant.
	 * @return true if the period describes an instant and false otherwise.
	 * @throws XBRLException.
	 * @see org.xbrlapi.Period#isInstant()
	 */
	public boolean isInstant() throws XBRLException {
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"instant").getLength() == 1) return true;
		return false;
	}

	/**
	 * Returns true if the period describes forever.
	 * @return true if the period describes forever and false otherwise.
	 * @throws XBRLException.
	 * @see org.xbrlapi.Period#isForever()
	 */
	public boolean isForever() throws XBRLException {
		return (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"forever").getLength() == 1);
	}

    /**
     * Set the period using a moment.
     * TODO The instant should be a date/time object rather than an string.
     * @param moment The string value of the moment.
     * @throws XBRLException
     * @see org.xbrlapi.Period#setInstant(String)
     */
	public void setInstant(String moment) throws XBRLException {
		throw new XBRLException("The data update methods are not yet implemented.");
	}

    /**
     * Set the period using a start date and end date.
     * TODO duration and instant creation should be done using actual date/time parameters rather than strings.  I have yet to determine the best date time class to use for this!!!
     * @param startDate The start date
     * @param endDate The end date
     * @throws XBRLException
     * @see org.xbrlapi.Period#setFiniteDuration(String,String)
     */
	public void setFiniteDuration(String startDate, String endDate) throws XBRLException {
		throw new XBRLException("The data update methods are not yet implemented.");
	}

	/**
	 * Sets the value of the period with an instant.
	 * @throws XBRLException.
	 * @see org.xbrlapi.Period#setForever()
	 */	
	public void setForever() throws XBRLException {
		throw new XBRLException("The data update methods are not yet implemented.");
	}

    /**
     * Get the instant.
     * @return the string value of the instant.
     * @throws XBRLException if the period is not an instant.
     * @see org.xbrlapi.Period#getInstant()
     */
	public String getInstant() throws XBRLException {
		if (! isInstant()) throw new XBRLException("The period is not an instant.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"instant").item(0).getTextContent().trim();
	}
	/**
	 * @author Geoffrey Shuetrim (geoff@galexy.net)
	 */


    /**
     * Get the start date.
     * @return the start date value.
     * @throws XBRLException if the date is not a duration that is not a finite duration.
     * @see org.xbrlapi.Period#getStartDate()
     */
	public String getStartDate() throws XBRLException {
		if (! isFiniteDuration()) throw new XBRLException("The period is not a finite duration.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"startDate").item(0).getTextContent().trim();
	}

    /**
     * Get the end date.
     * @return the end date value.
     * @throws XBRLException if the date is not a duration that is not a finite duration.
     * @see org.xbrlapi.Period#getEndDate()
     */
	public String getEndDate() throws XBRLException {
		if (! isFiniteDuration()) throw new XBRLException("The period is not a finite duration.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"endDate").item(0).getTextContent().trim();
	}
}
