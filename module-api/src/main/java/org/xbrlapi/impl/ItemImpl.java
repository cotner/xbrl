package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ItemImpl extends FactImpl implements Item {

	/**
     * 
     */
    private static final long serialVersionUID = 5737055509180629932L;

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
    

    
    
	

	
	
	
}
