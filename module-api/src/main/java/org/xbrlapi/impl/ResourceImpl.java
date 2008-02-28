package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Resource;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ResourceImpl extends ArcEndImpl implements Resource {

	/**
	 * Get the value of the resource role (xlink:role attribute) for the resource.
	 * @return the value of the resource role (xlink:role attribute) for the resource or 
	 * null if none is specified.
	 * @throws XBRLException.
	 * @see org.xbrlapi.Resource#getResourceRole()
	 */
	public String getResourceRole() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace,"role")) return null;
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"role");		
	}

}
