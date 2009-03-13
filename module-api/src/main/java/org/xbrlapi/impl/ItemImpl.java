package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Item;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ItemImpl extends FactImpl implements Item {

	/** 
	 * @see org.xbrlapi.Item#getContext()
	 */
	public Context getContext() throws XBRLException {
	    return getInstance().getContext(this.getContextId());
	}
	
    /** 
     * @see org.xbrlapi.Item#getContextId()
     */
    public String getContextId() throws XBRLException {
        Element root = getDataRootElement();
        if (root.hasAttribute("contextRef")) {
            return root.getAttribute("contextRef");
        }
        throw new XBRLException("The contextRef is missing on an item.");
    }
    
    /** 
     * @see org.xbrlapi.Item#getUnit()
     */
    public Unit getUnit() throws XBRLException {
        return getInstance().getUnit(this.getUnitId());
    }
    
    /** 
     * @see org.xbrlapi.Item#getUnitId()
     */
    public String getUnitId() throws XBRLException {
        Element root = getDataRootElement();
        if (root.hasAttribute("unitRef")) {
            return root.getAttribute("unitRef");
        }
        throw new XBRLException("The unitRef is missing on an item.");
    }    
	

	
	
	
}
