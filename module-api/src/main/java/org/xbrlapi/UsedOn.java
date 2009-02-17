package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface UsedOn extends Fragment {

    /**
     * @return the namespace URI of the element that can
     * be used on.
     * @throws XBRLException
     */
    public URI getUsedOnNamespace() throws XBRLException;
    

	
    /**
     * @return the local name of the element that the 
     * custom XLINK link role, resource role, or arc role 
     * can be used on.
     * @throws XBRLException
     */
    public String getUsedOnLocalname() throws XBRLException;
    
    /**
     * Returns true only if the custom role type can be used on the specified element
     * based on this usedOn fragment.
     *
     * @param namespaceURI The namespace of the element being tested for
     * @param localname The local name of the element being tested for
     * @throws XBRLException
     */
    public boolean isUsedOn(URI namespaceURI, String localname) throws XBRLException;
    
}
