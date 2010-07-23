package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface NumericItem extends Item {

	/** 
	 * Presumes that there is at most one XBRL instance in each document in the data store.
	 * @return the units fragment referenced by this numeric item.
	 * @throws XBRLException
	 */
	public Unit getUnit() throws XBRLException;

    /** 
     * @return the ID of the unit referenced by this item.
     * @throws XBRLException if the unit reference is missing.
     */
    public String getUnitId() throws XBRLException;     
	
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