package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface UsedOn extends Fragment {

    /**
     * Get the namespace URI of the element that can
     * be used on.
     *
     * @throws XBRLException
     */
    public String getURI() throws XBRLException;
    

	
    /**
     * Get the localname for the element that can be used on.
     * @return the local name of the element that the custom role or arcrole URI can be used on.
     * @throws XBRLException
     */
    public String getLocalname() throws XBRLException;
    
    /**
     * Returns true only if the custom role type can be used on the specified element
     * based on this usedOn fragment.
     *
     * @param namespaceURI The namespace of the element being tested for
     * @param localname The local name of the element being tested for
     * @throws XBRLException
     */
    public boolean isUsedOn(String namespaceURI, String localname) throws XBRLException;
    
}
