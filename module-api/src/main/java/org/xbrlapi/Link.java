package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Link extends Xlink {

    /**
     * @return the link role (The xlink:role attribute value).
     * @throws XBRLException
     */
    public URI getLinkRole() throws XBRLException;
    
}
