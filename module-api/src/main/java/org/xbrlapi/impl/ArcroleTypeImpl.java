package org.xbrlapi.impl;


import org.xbrlapi.networks.Networks;

import org.w3c.dom.Element;
import org.xbrlapi.ArcroleType;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ArcroleTypeImpl extends CustomTypeImpl implements ArcroleType {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -3510310341148593799L;



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
     * get the collection of networks expressed using arcs that involve this 
     * arc role.
     */
    public Networks getNetworks() throws XBRLException {
    	return getStore().getNetworks(this.getCustomURI());
    }
    

    
    
}
