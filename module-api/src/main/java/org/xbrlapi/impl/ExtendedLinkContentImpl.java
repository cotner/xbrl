package org.xbrlapi.impl;

import org.xbrlapi.ExtendedLink;
import org.xbrlapi.ExtendedLinkContent;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ExtendedLinkContentImpl extends XlinkImpl implements ExtendedLinkContent {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5152171927354071767L;

    /**
     * @see org.xbrlapi.ExtendedLinkContent#getExtendedLink()
     */
    public ExtendedLink getExtendedLink() throws XBRLException {
    	ExtendedLink link = (ExtendedLink) this.getAncestorOrSelf("org.xbrlapi.impl.ExtendedLinkImpl");
    	return link;
    }

}
