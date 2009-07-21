package org.xbrlapi.data.resource;

import java.io.Serializable;
import java.util.List;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Signer extends Serializable {

    /**
     * @param lines The list of all lines in the resource.
     * @return the signature given the resource content.
     */
    public String getSignature(List<String> lines);
    
}
