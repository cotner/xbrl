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
	 * @see org.xbrlapi.Period#isFiniteDurationPeriod()
	 */
	public boolean isFiniteDurationPeriod() throws XBRLException {
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"instant").getLength() == 1) return false;
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"forever").getLength() == 1) return false;
		return true;
	}

	/**
	 * @see org.xbrlapi.Period#isInstantPeriod()
	 */
	public boolean isInstantPeriod() throws XBRLException {
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"instant").getLength() > 0) return true;
		return false;
	}

	/**
	 * @see org.xbrlapi.Period#isForeverPeriod()
	 */
	public boolean isForeverPeriod() throws XBRLException {
		return (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"forever").getLength() == 1);
	}

    /**
     * @see org.xbrlapi.Period#getInstant()
     */
	public String getInstant() throws XBRLException {
		if (! isInstantPeriod()) throw new XBRLException("The period is not an instant.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"instant").item(0).getTextContent().trim();
	}

    /**
     * @see org.xbrlapi.Period#getStart()
     */
	public String getStart() throws XBRLException {
		if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"startDate").item(0).getTextContent().trim();
	}

    /**
     * @see org.xbrlapi.Period#getEnd()
     */
	public String getEnd() throws XBRLException {
		if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"endDate").item(0).getTextContent().trim();
	}
}
