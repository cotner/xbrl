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
	 * Set the units for this item
	 * @param unit The unit fragment that the numeric item
	 * is being associated with.
	 * @throws XBRLException
	 */
	public void setUnits(Unit unit) throws XBRLException;
	

	/** 
	 * Get the decimals attribute for this item.
	 *
	 * @return the value of the decimals attribute
	 * @throws XBRLException
	 */
	public String getDecimals() throws XBRLException;
	
	/** 
	 * Set the decimals attribute on the item.
	 * @param decimals The decimals attribute on the item
	 * @throws XBRLException
	 */
	public void setDecimals(int decimals) throws XBRLException;	

	/** 
	 * Get the precision attribute for this item.
	 *
	 * @return the value of the precision attribute
	 * @throws XBRLException
	 */
	public String getPrecision() throws XBRLException;
	
	/** 
	 * Set the precision attribute on the item.
	 * @param precision The precision attribute on the item
	 * @throws XBRLException
	 */
	public void setPrecision(int precision) throws XBRLException;
	
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