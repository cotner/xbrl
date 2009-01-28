package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Node;
import org.xbrlapi.UsedOn;
import org.xbrlapi.utilities.XBRLException;

/**
 * TODO Eliminate the usedOn fragment
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class UsedOnImpl extends FragmentImpl implements UsedOn {

    /**
     * Get the namespace URI of the element that can
     * be used on.
     * @throws XBRLException
     * @see org.xbrlapi.UsedOn#getURI()
     */
    public URI getURI() throws XBRLException {
    	Node rootNode = getDataRootElement();
    	String u = rootNode.getTextContent().trim();
    	if (u.equals(""))
			throw new XBRLException("The used on declaration does not declare the element that usage is allowed on.");
    	try {
    	    return new URI(this.getNamespaceFromQName(u, rootNode));
    	} catch (URISyntaxException e) {
    	    throw new XBRLException(getNamespaceFromQName(u, rootNode) + " has an invalid URI syntax.");
    	}
    }
    


    /**
     * Get the localname for the element that can be used on.
     * @return the local name of the element that the custom role or arcrole URI can be used on.
     * @throws XBRLException
     * @see org.xbrlapi.UsedOn#getLocalname()
     */
    public String getLocalname() throws XBRLException {
    	Node rootNode = getDataRootElement();
    	String u = rootNode.getTextContent().trim();
    	if (u.equals(""))
			throw new XBRLException("The used on declaration does not declare the element that usage is allowed on.");
    	return this.getLocalnameFromQName(u);
    }
    
    /**
     * Returns true only if the custom role type can be used on the specified element
     * based on this usedOn fragment.
     * @param namespaceURI The namespace of the element being tested for
     * @param localname The local name of the element being tested for
     * @throws XBRLException
     * @see org.xbrlapi.UsedOn#isUsedOn(String, String)
     */
    public boolean isUsedOn(String namespaceURI, String localname) throws XBRLException {
    	if (! getURI().equals(namespaceURI))
    		return false;
    	if (! getLocalname().equals(localname))
    		return false;
    	return true;
    }

}
