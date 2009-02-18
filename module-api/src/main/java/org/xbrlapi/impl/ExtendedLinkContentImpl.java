package org.xbrlapi.impl;

import org.xbrlapi.ExtendedLink;
import org.xbrlapi.ExtendedLinkContent;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ExtendedLinkContentImpl extends XlinkImpl implements ExtendedLinkContent {
    
    /**
     * Get the extended link containing the locator.
     * @return the extended link fragment containing the locator.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLinkContent#getExtendedLink()
     */
    public ExtendedLink getExtendedLink() throws XBRLException {
    	ExtendedLink link = (ExtendedLink) this.getAncestorOrSelf("org.xbrlapi.impl.ExtendedLinkImpl");
    	logger.debug("The element is in extended link " + link.getIndex());
    	return link;
    }

}
