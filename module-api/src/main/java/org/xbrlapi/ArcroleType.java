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
    

    

}
