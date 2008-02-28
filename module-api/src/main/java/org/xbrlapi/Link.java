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
    
    /**
     * Set the link role (The xlink:role attribute value).
     * @param role The URI value of the link role.
     * @throws XBRLException if the link is contained in a linkbase
     * an uses a custom role but the custom role is not linked
     * to using a roleRef element in the same linkbase.
     */
    public void setLinkRole(String role) throws XBRLException;

    
}
