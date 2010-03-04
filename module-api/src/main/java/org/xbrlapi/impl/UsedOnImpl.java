package org.xbrlapi.impl;

import java.net.URI;

import org.w3c.dom.Node;
import org.xbrlapi.UsedOn;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class UsedOnImpl extends FragmentImpl implements UsedOn {

    /**
     * @see org.xbrlapi.UsedOn#getUsedOnNamespace()
     */
    public URI getUsedOnNamespace() throws XBRLException {
    	Node rootNode = getDataRootElement();
    	String u = rootNode.getTextContent().trim();
    	if (u.equals("")) throw new XBRLException("The used on declaration does not declare the element that usage is allowed on.");
	    return getNamespaceFromQName(u, rootNode);
    }
    
    /**
     * @see org.xbrlapi.UsedOn#getUsedOnLocalname()
     */
    public String getUsedOnLocalname() throws XBRLException {
        Node rootNode = getDataRootElement();
        String u = rootNode.getTextContent().trim();
        if (u.equals("")) throw new XBRLException("The used on declaration does not declare the element that usage is allowed on.");
        return this.getLocalnameFromQName(u);
    }    
    
    /**
     * @see org.xbrlapi.UsedOn#isUsedOn(URI, String)
     */
    public boolean isUsedOn(URI namespaceURI, String localname) throws XBRLException {
    	if (! getUsedOnNamespace().equals(namespaceURI))
    		return false;
    	if (! getUsedOnLocalname().equals(localname))
    		return false;
    	return true;
    }

}
