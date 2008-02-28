package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ArcroleType extends CustomType {

    /**
     * Get the cyclesAllowed attribute value.
     *
     * @throws XBRLException
     */
    public String getCyclesAllowed() throws XBRLException;
    
    /**
     * Set the cycles allowed attribute value to one of three values:
     * any, none, or undirected (I think).
     *
     * @param cyclesAllowed The cyclesAllowed attribute value
     * @throws XBRLException
     */
    public void setCyclesAllowed(String cyclesAllowed) throws XBRLException;
    

}
