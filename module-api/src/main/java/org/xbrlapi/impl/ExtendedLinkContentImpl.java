package org.xbrlapi.impl;

import org.xbrlapi.ExtendedLink;
import org.xbrlapi.ExtendedLinkContent;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ExtendedLinkContentImpl extends XlinkImpl implements ExtendedLinkContent {
    
    /**
     * @see org.xbrlapi.ExtendedLinkContent#getExtendedLink()
     */
    public ExtendedLink getExtendedLink() throws XBRLException {
    	ExtendedLink link = (ExtendedLink) this.getAncestorOrSelf("org.xbrlapi.impl.ExtendedLinkImpl");
    	return link;
    }

}
