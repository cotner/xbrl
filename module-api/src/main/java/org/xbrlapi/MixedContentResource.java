package org.xbrlapi;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import org.w3c.dom.NodeList;
import org.xbrlapi.utilities.XBRLException;

public interface MixedContentResource extends Resource {
	
    /**
     * Get the value of the resource as a NodeList containing all of the children 
     * elements and text nodes nested in the resource.
     * @return a list of child nodes of the resource including attributes, 
     * XHTML elements, and text nodes.
     * @throws XBRLException
     */
    public NodeList getContent() throws XBRLException;
	

    
}
