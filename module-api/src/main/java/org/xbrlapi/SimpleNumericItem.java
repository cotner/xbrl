package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface SimpleNumericItem extends NumericItem {

	/** 
	 * Get the value of the fact
	 *
	 * @return the value of fact
	 * @throws XBRLException
	 */
	public String getValue() throws XBRLException;
	


	/** 
	 * Get the value of the fact after adjusting for the specified precision.
	 * @return the value of the fact as a string, adjusted for the specified precision.
	 * @throws XBRLException
	 */
	public String getPrecisionAdjustedValue() throws XBRLException;	
	
	/**
	 * Get the inferred value for precision from the value for 
	 * decimals and the value of the fact.
	 * @return inferred value for precision.
	 * @throws XBRLException
	 */
	public String getInferredPrecision() throws XBRLException;
	
}