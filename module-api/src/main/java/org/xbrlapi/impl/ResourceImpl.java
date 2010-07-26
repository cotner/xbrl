package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Resource;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ResourceImpl extends ArcEndImpl implements Resource {

    /**
     * 
     */
    private static final long serialVersionUID = -3515868937307864773L;

    /**
	 * @see org.xbrlapi.Resource#getResourceRole()
	 */
	public URI getResourceRole() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace.toString(),"role")) return null;
        String value = getDataRootElement().getAttributeNS(Constants.XLinkNamespace.toString(),"role");
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new XBRLException(value + " has an invalid URI syntax for the resource XLink role",e);
        }
	}

    /**
     * @see org.xbrlapi.ExtendedLinkContent#getExtendedLink()
     */
    public ExtendedLink getExtendedLink() throws XBRLException {
        Fragment parent = this.getParent();
        if (! parent.isa(ExtendedLinkImpl.class)) throw new XBRLException("The parent of resource " + this.getIndex() + " is not an extended link.");
        return (ExtendedLink) parent;
    }	
}
