package org.xbrlapi.data.resource;

import java.util.List;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Signer {

    /**
     * @param lines The list of all lines in the resource.
     * @return the signature given the resource content.
     */
    public String getSignature(List<String> lines);
    
}
