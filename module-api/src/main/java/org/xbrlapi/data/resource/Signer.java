package org.xbrlapi.data.resource;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Signer {

    /**
     * @param lines The list of all lines in the resource.
     * @return the signature given the resource content.
     * @throws XBRLException if the signature cannot be constructed.
     */
    public String getSignature(List<String> lines) throws XBRLException;
    
}
