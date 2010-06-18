package org.xbrlapi.impl;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.xbrlapi.Period;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Implementation of context period fragments for instants and durations.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class PeriodImpl extends ContextComponentImpl implements Period {

    /**
     * 
     */
    private static final long serialVersionUID = -6599852248292348434L;

    /**
	 * @see #isFiniteDurationPeriod()
	 */
	public boolean isFiniteDurationPeriod() throws XBRLException {
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"instant").getLength() == 1) return false;
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"forever").getLength() == 1) return false;
		return true;
	}

	/**
	 * @see #isInstantPeriod()
	 */
	public boolean isInstantPeriod() throws XBRLException {
		if (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"instant").getLength() > 0) return true;
		return false;
	}

	/**
	 * @see #isForeverPeriod()
	 */
	public boolean isForeverPeriod() throws XBRLException {
		return (this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"forever").getLength() == 1);
	}

    /**
     * @see #getInstant()
     */
	public String getInstant() throws XBRLException {
		if (! isInstantPeriod()) throw new XBRLException("The period is not an instant.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"instant").item(0).getTextContent().trim();
	}

    /**
     * @see #getStart()
     */
	public String getStart() throws XBRLException {
		if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"startDate").item(0).getTextContent().trim();
	}

    /**
     * @see #getEnd()
     */
	public String getEnd() throws XBRLException {
		if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
		return this.getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"endDate").item(0).getTextContent().trim();
	}

    /**
     * @see Period#getEndCalendar()
     */
    public XMLGregorianCalendar getEndCalendar() throws XBRLException {
        if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
        XMLGregorianCalendar date = this.getXMLGregorianCalendar(getEnd());
        if (date.getXMLSchemaType().getLocalPart().equals("date")) {
            date = roll(date);
            date = addTime(date);
        }
        return date;
    }
    
    /**
     * @see Period#getInstantCalendar()
     */
    public XMLGregorianCalendar getInstantCalendar() throws XBRLException {
        if (! isInstantPeriod()) throw new XBRLException("The period is not an instant.");
        XMLGregorianCalendar date = this.getXMLGregorianCalendar(getInstant());
        if (date.getXMLSchemaType().getLocalPart().equals("date")) {
            date = roll(date);
            date = addTime(date);
        }
        return date;
    }

    /**
     * @see Period#getStartCalendar()
     */
    public XMLGregorianCalendar getStartCalendar() throws XBRLException {
        if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
        XMLGregorianCalendar date = this.getXMLGregorianCalendar(getStart());
        if (date.getXMLSchemaType().getLocalPart().equals("date")) date = addTime(date);
        return date;
    }
    
    /**
     * @param value The XML date or dateTime string value
     * @return the XML Gregorian Calendar as a construct obtained from
     * a lexical representation of an XML Schema date or dateTime value value.
     */
    private XMLGregorianCalendar getXMLGregorianCalendar(String value) throws XBRLException {
        try {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            XMLGregorianCalendar calendar = factory.newXMLGregorianCalendar(value);
            return calendar;
        } catch (DatatypeConfigurationException e) {
            throw new XBRLException("The data type conversion factory could not be instantiated.",e);
        }
    }
    
    /**
     * @param date The date to convert to a dateTime.
     * @return the date time equivalent to the date, obtained by 
     * adding T00:00:00 to the lexical representation as per the XBRL Specification.
     */
    private XMLGregorianCalendar addTime(XMLGregorianCalendar date) throws XBRLException {
        try {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            return factory.newXMLGregorianCalendar(date.toXMLFormat() + "T00:00:00");
        } catch (DatatypeConfigurationException e) {
            throw new XBRLException("The data type conversion factory could not be instantiated.",e);
        }
    }
    
    /**
     * @param date The date to roll forward by 1 day.
     * @return the XML Gregorian Calendar rolled forward by one day.
     */
    private XMLGregorianCalendar roll(XMLGregorianCalendar date)  throws XBRLException {
        try {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            date.add(factory.newDuration("P1D"));
            return date;
        } catch (DatatypeConfigurationException e) {
            throw new XBRLException("The data type conversion factory could not be instantiated.",e);
        }
    }

    /**
     * @see #endHasTimezone()
     */
    public boolean endHasTimezone() throws XBRLException {
        if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
        return getXMLGregorianCalendar(getEnd()).getTimezone() != DatatypeConstants.FIELD_UNDEFINED;
    }

    /**
     * @see #endIsDateOnly()
     */
    public boolean endIsDateOnly() throws XBRLException {
        if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
        return getXMLGregorianCalendar(getEnd()).getXMLSchemaType().getLocalPart().equals("date");
    }

    /**
     * @see #instantHasTimezone()
     */
    public boolean instantHasTimezone() throws XBRLException {
        if (! isInstantPeriod()) throw new XBRLException("The period is not an instant.");
        return getXMLGregorianCalendar(getInstant()).getTimezone() != DatatypeConstants.FIELD_UNDEFINED;
    }

    /**
     * @see #instantIsDateOnly()
     */
    public boolean instantIsDateOnly() throws XBRLException {
        if (! isInstantPeriod()) throw new XBRLException("The period is not an instant.");
        return getXMLGregorianCalendar(getInstant()).getXMLSchemaType().getLocalPart().equals("date");
    }

    /**
     * @see #startHasTimezone()
     */
    public boolean startHasTimezone() throws XBRLException {
        if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
        return getXMLGregorianCalendar(getStart()).getTimezone() != DatatypeConstants.FIELD_UNDEFINED;
    }

    /**
     * @see #startIsDateOnly()
     */
    public boolean startIsDateOnly() throws XBRLException {
        if (! isFiniteDurationPeriod()) throw new XBRLException("The period is not a finite duration.");
        return getXMLGregorianCalendar(getStart()).getXMLSchemaType().getLocalPart().equals("date");
    }

}
