package org.xbrlapi.impl;

import org.xbrlapi.NumericItem;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class NumericItemImpl extends ItemImpl implements NumericItem {

	/** 
	 * Get the unit fragment for this item
	 * @return the unit fragment associated with this item
	 * @throws XBRLException
	 * @see org.xbrlapi.NumericItem#getUnits()
	 */
	public Unit getUnits() throws XBRLException {
		String unitRef = getDataRootElement().getAttribute("unitRef");
		return getInstance().getUnit(unitRef);
	}
	
	/** 
	 * Set the units for this item
	 * @param units The unit fragment that the  item
	 * is being associated with.
	 * @throws XBRLException if the unit is not in the XBRL instance
	 * containing the item.
	 * @see org.xbrlapi.NumericItem#setUnits(Unit)
	 */
	public void setUnits(Unit units) throws XBRLException {
		throw new XBRLException("Data update methods are not yet implemented.");
	}

	/** 
	 * Get the decimals attribute for this item.
	 * @return the value of the decimals attribute
	 * @throws XBRLException
	 * @see org.xbrlapi.NumericItem#getDecimals()
	 */
	public String getDecimals() throws XBRLException {
		
		if (getType().equals("org.xbrlapi.impl.FractionItemImpl")) return "INF";
		if (hasDecimals()) return getDataRootElement().getAttribute("decimals").trim();
		throw new XBRLException("The numeric item does not explicitly specify decimals");
	}
	
	/** 
	 * Set the decimals attribute on the item.
	 * @param decimals The decimals attribute on the item
	 * @throws XBRLException
	 * @see org.xbrlapi.NumericItem#setDecimals(int)
	 */
	public void setDecimals(int decimals) throws XBRLException {
		throw new XBRLException("Data update methods are not yet implemented.");
	}

	/** 
	 * Get the precision attribute for this item.
	 * @return the value of the precision attribute
	 * @throws XBRLException
	 * @see org.xbrlapi.NumericItem#getPrecision()
	 */
	public String getPrecision() throws XBRLException {
		if (getType().equals("org.xbrlapi.impl.FractionItemImpl")) return "INF";		
		if (hasPrecision()) return getDataRootElement().getAttribute("precision").trim();
		throw new XBRLException("The precision attribute is not explicitly specified.");
	}
	
	/** 
	 * Returns true if the fact has a precision attribute.
	 * @return true if the fact has a precision attribute and false otherwise.
	 * @throws XBRLException
	 * @see org.xbrlapi.NumericItem#hasPrecision()
	 */
	public boolean hasPrecision() throws XBRLException {
		if (getType().equals("org.xbrlapi.impl.FractionItemImpl")) return true;		
		if (getDataRootElement().hasAttribute("precision")) {
			return true;
		}
		return false;
	}
	
	/** 
	 * Returns true if the fact has a decimals attribute.
	 * @return true if the fact has a decimals attribute and false otherwise.
	 * @throws XBRLException
	 * @see org.xbrlapi.NumericItem#hasDecimals()
	 */
	public boolean hasDecimals() throws XBRLException {
		if (getType().equals("org.xbrlapi.impl.FractionItemImpl")) return true;		
		if (getDataRootElement().hasAttribute("decimals")) {
			return true;
		}
		return false;
	}	
	
	/** 
	 * Set the precision attribute on the item.
	 * @param precision The precision attribute on the item
	 * @throws XBRLException
	 * @see org.xbrlapi.NumericItem#setPrecision(int)
	 */
	public void setPrecision(int precision) throws XBRLException {
		throw new XBRLException("Data update methods are not yet implemented.");
	}
	
}
