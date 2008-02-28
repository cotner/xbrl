package org.xbrlapi.impl;

import org.w3c.dom.NodeList;
import org.xbrlapi.FractionItem;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * TODO Create fraction item examples in the conformance suite to test fraction items.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FractionItemImpl extends NumericItemImpl implements FractionItem {

	/**
	 * Get the fraction numerator.
	 * @return the string value of the fraction numerator with leading and
	 * trailing spaces removed.
	 * @throws XBRLException if the numerator is missing or is not unique.
	 * @see org.xbrlapi.FractionItem#getNumerator()
	 */
    public String getNumerator() throws XBRLException {
    	NodeList candidates = getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"numerator");
    	if (candidates.getLength() == 0) throw new XBRLException("The fraction numerator is missing.");
    	if (candidates.getLength() > 1) throw new XBRLException("The fraction numerator is not unique.");
    	return candidates.item(0).getTextContent().trim();
    }
	
	/**
	 * Set the fraction numerator
	 * @param numerator The fraction numerator
	 * @throws XBRLException
	 * @see org.xbrlapi.FractionItem#setNumerator(double)
	 */
    public void setNumerator(double numerator) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }
	
	/**
	 * Get the fraction denominator.
	 * @return the string value of the fraction denominator with leading and
	 * trailing spaces removed.
	 * @throws XBRLException if the denominator is missing or is not unique.
	 * @see org.xbrlapi.FractionItem#getDenominator()
	 */
    public String getDenominator() throws XBRLException {
    	NodeList candidates = getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"denominator");
    	if (candidates.getLength() == 0) throw new XBRLException("The fraction denominator is missing.");
    	if (candidates.getLength() > 1) throw new XBRLException("The fraction denominator is not unique.");
    	return candidates.item(0).getTextContent().trim();
    }
	
	/**
	 * Set the fraction denominator
	 * Throws an exception if the denominator is zero.
	 * @param denominator The fraction denominator
	 * @throws XBRLException
	 * @see org.xbrlapi.FractionItem#setDenominator(double)
	 */
    public void setDenominator(double denominator) throws XBRLException {
		throw new XBRLException("Data update methods are not yet implemented.");
    }
	
}
