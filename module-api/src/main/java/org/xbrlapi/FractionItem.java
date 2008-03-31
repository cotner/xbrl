package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface FractionItem extends NumericItem {
	
	/**
	 * Get the fraction numerator.
	 * @return the string value of the fraction numerator with leading and
	 * trailing spaces removed.
	 * @throws XBRLException if the numerator is missing or is not unique.
	 */
    public String getNumerator() throws XBRLException;
	

	
	/**
	 * Get the fraction denominator.
	 * @return the string value of the fraction denominator with leading and
	 * trailing spaces removed.
	 * @throws XBRLException if the denominator is missing or is not unique.
	 */
    public String getDenominator() throws XBRLException;
	


}