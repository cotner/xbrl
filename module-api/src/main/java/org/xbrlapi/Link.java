package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Link extends Xlink {

    /**
     * @return the link role (The xlink:role attribute value).
     * @throws XBRLException
     */
    public String getLinkRole() throws XBRLException;
    
}
