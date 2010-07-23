package org.xbrlapi.impl;

import java.util.List;

import org.w3c.dom.Element;
import org.xbrlapi.NumericItem;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class NumericItemImpl extends ItemImpl implements NumericItem {

	/**
     * 
     */
    private static final long serialVersionUID = -2601049145050399109L;

    /** 
	 * @see org.xbrlapi.NumericItem#getUnit()
	 */
	public Unit getUnit() throws XBRLException {
        String query = "for $root in #roots#[@uri='"+this.getURI()+"' and @type='"+UnitImpl.class.getName()+"' and xbrlapi:data/xbrli:unit/@id='"+this.getUnitId()+"'] return $root";
        List<Unit> units = getStore().<Unit>queryForXMLResources(query);
        if (units.size() == 1) return units.get(0);
        throw new XBRLException("There is not a unique matching context with ID "+this.getUnitId()+" for this fact in instance " + this.getURI());
	}

    /** 
     * @see org.xbrlapi.NumericItem#getUnitId()
     */
    public String getUnitId() throws XBRLException {
        Element root = getDataRootElement();
        if (root.hasAttribute("unitRef")) {
            return root.getAttribute("unitRef");
        }
        throw new XBRLException("The unit reference is missing on numeric item " + this.getIndex());
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
	

	
}
