package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Language;
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
     * @see org.xbrlapi.Resource#getLanguage()
     */
    public String getLanguage() throws XBRLException {
        Element root = getDataRootElement();
        if (! root.hasAttributeNS(Constants.XMLNamespace.toString(),"lang")) return null;
        return getDataRootElement().getAttributeNS(Constants.XMLNamespace.toString(),"lang");        
    }
    
    /**
     * @see org.xbrlapi.Resource#getLanguage(String)
     */
    public String getLanguage(String language) throws XBRLException {
        Language l  = getStore().getLanguage(language,this.getLanguage());
        if (l == null) return null;
        return l.getName();
    }    
	
    /**
     * @see org.xbrlapi.ExtendedLinkContent#getExtendedLink()
     */
    public ExtendedLink getExtendedLink() throws XBRLException {
        Fragment parent = this.getParent();
        if (! parent.isa("org.xbrlapi.impl.ExtendedLinkImpl")) throw new XBRLException("The parent of resource " + this.getIndex() + " is not an extended link.");
        return (ExtendedLink) parent;
    }	
}
