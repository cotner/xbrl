package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.ArcroleType;
import org.xbrlapi.Networks;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ArcroleTypeImpl extends CustomTypeImpl implements ArcroleType {

    /**
     * Get the cyclesAllowed attribute value (one of any, directed or none).
     * @return the value of the cyclesAllowed attribute (even if it is not one of the valid values).
     * @throws XBRLException if no attribute value is provided.
     * @see org.xbrlapi.ArcroleType#getCyclesAllowed()
     */
    public String getCyclesAllowed() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttribute("cyclesAllowed")) {
    		return root.getAttribute("cyclesAllowed");
    	}
    	throw new XBRLException("A cyclesAllowed attribute must be specified.");
    }
    
    /**
     * Set the cycles allowed attribute value to one of three values:
     * any, none, or directed.
     * @param cyclesAllowed The cyclesAllowed attribute value
     * @throws XBRLException if the value to be set is not one of any, none or directed.
     * @see org.xbrlapi.ArcroleType#setCyclesAllowed(String)
     */
    public void setCyclesAllowed(String cyclesAllowed) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }
    
    /**
     * get the collection of networks expressed using arcs that involve this 
     * arc role.
     */
    public Networks getNetworks() throws XBRLException {
    	return getStore().getNetworks(this.getCustomURI());
    }
    

    
    
}
