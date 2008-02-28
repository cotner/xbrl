package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface ExtendedLinkContent extends Xlink {

    /**
     * Get the extended link containing the extended link xlink content.
     * @return the extended link fragment containing the extended link xlink content.
     * @throws XBRLException
     */
    public ExtendedLink getExtendedLink() throws XBRLException;
    
}