package org.xbrlapi.SAXHandlers.FragmentIdentifiers;

import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface FragmentIdentifier {

    /**
     * @param namespaceURI the namespace of the element
     * @param lName the local name of the element 
     * @param qName the qname of the element
     * @param attrs the attributes of the element
     * @throws XBRLException
     */
    public void idFragment(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException;
    
}
