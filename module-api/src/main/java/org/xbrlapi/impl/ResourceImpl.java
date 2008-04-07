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
	 * @see org.xbrlapi.Resource#getResourceRole()
	 */
	public String getResourceRole() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace,"role")) return null;
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"role");		
	}

    /**
     * @see org.xbrlapi.Resource#getLanguage()
     */
    public String getLanguage() throws XBRLException {
        Element root = getDataRootElement();
        if (! root.hasAttributeNS(Constants.XMLNamespace,"lang")) return null;
        return getDataRootElement().getAttributeNS(Constants.XMLNamespace,"lang");        
    }
	
	
}
