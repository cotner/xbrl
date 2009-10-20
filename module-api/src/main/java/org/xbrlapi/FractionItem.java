package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface FractionItem extends NumericItem {
	
	/**
	 * Get the fraction numerator.
	 * @return the decimal value of the fraction numerator with leading and
	 * trailing spaces removed.
	 * @throws XBRLException if the numerator is missing.
	 */
    public double getNumerator() throws XBRLException;
	
	/**
	 * Get the fraction denominator.
	 * @return the decimal value of the fraction denominator with leading and
	 * trailing spaces removed.
	 * @throws XBRLException if the denominator is missing.
	 */
    public double getDenominator() throws XBRLException;
	
}