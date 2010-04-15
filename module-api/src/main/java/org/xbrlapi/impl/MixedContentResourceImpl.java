package org.xbrlapi.impl;

import org.w3c.dom.NodeList;
import org.xbrlapi.MixedContentResource;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MixedContentResourceImpl extends ResourceImpl implements MixedContentResource {
	
    /**
     * 
     */
    private static final long serialVersionUID = 4954206459559699372L;

    /**
     * Get the value of the resource as a NodeList containing all of the children 
     * elements and text nodes nested in the resource.
     * @return a list of child nodes of the resource including attributes, 
     * XHTML elements, and text nodes.
     * @throws XBRLException
     * @see org.xbrlapi.MixedContentResource#getContent()
     */
    public NodeList getContent() throws XBRLException {
    	return getDataRootElement().getChildNodes();
    }
}
