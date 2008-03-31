package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface NumericItem extends Item {

	/** 
	 * Get the units for this item
	 *
	 * @return the units fragment associated with this item
	 * @throws XBRLException
	 */
	public Unit getUnits() throws XBRLException;
	

	

	/** 
	 * Get the decimals attribute for this item.
	 *
	 * @return the value of the decimals attribute
	 * @throws XBRLException
	 */
	public String getDecimals() throws XBRLException;
	
	

	/** 
	 * Get the precision attribute for this item.
	 *
	 * @return the value of the precision attribute
	 * @throws XBRLException
	 */
	public String getPrecision() throws XBRLException;
	

	
	/** 
	 * Returns true if the fact has a precision attribute.
	 * @return true if the fact has a precision attribute and false otherwise.
	 * @throws XBRLException
	 */
	public boolean hasPrecision() throws XBRLException;
	
	/** 
	 * Returns true if the fact has a decimals attribute.
	 * @return true if the fact has a decimals attribute and false otherwise.
	 * @throws XBRLException
	 */
	public boolean hasDecimals() throws XBRLException;

}