package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ItemImpl extends FactImpl implements Item {

	/** 
	 * Get the context for this item.
	 * @return the context fragment associated with this item
	 * @throws XBRLException if the context reference is missing or the context is unavailable.
	 * @see org.xbrlapi.Item#getContext()
	 */
	public Context getContext() throws XBRLException {
		Element root = getDataRootElement();
		if (root.hasAttribute("contextRef")) {
			return getInstance().getContext(root.getAttribute("contextRef"));
		}
		throw new XBRLException("The contextRef is missing on an item.");
	}
	

	
	/** 
	 * Return true if the item has a nill value.
	 * @throws XBRLException
	 * @see org.xbrlapi.Item#isNill()
	 */
	public boolean isNill() throws XBRLException {
		if (this.getDataRootElement().getAttributeNS(Constants.XMLSchemaInstanceNamespace,"nill").equals("true")) return true;
		return false;
	}	
	
}
