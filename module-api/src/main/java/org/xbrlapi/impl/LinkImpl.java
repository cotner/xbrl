package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;
import org.xbrlapi.Link;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class LinkImpl extends XlinkImpl implements Link {

    /**
     * @see org.xbrlapi.Link#getLinkRole()
     */
    public URI getLinkRole() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XLinkNamespace.toString(),"role")) {
    	    String role = root.getAttributeNS(Constants.XLinkNamespace.toString(),"role");
    	    try {
    	        return new URI(role);
    	    } catch (URISyntaxException e) {
    	        throw new XBRLException("The link role, " + role + ", is not a valid URI.",e);
    	    }
    	}
    	return null;
    }
    
}
