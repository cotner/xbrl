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
    	NodeList candidates = getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"numerator");
    	if (candidates.getLength() == 0) throw new XBRLException("The fraction numerator is missing.");
    	if (candidates.getLength() > 1) throw new XBRLException("The fraction numerator is not unique.");
    	return candidates.item(0).getTextContent().trim();
    }
	

	
	/**
	 * Get the fraction denominator.
	 * @return the string value of the fraction denominator with leading and
	 * trailing spaces removed.
	 * @throws XBRLException if the denominator is missing or is not unique.
	 * @see org.xbrlapi.FractionItem#getDenominator()
	 */
    public String getDenominator() throws XBRLException {
    	NodeList candidates = getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"denominator");
    	if (candidates.getLength() == 0) throw new XBRLException("The fraction denominator is missing.");
    	if (candidates.getLength() > 1) throw new XBRLException("The fraction denominator is not unique.");
    	return candidates.item(0).getTextContent().trim();
    }
	

	
}
