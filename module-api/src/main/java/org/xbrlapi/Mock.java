package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Mock extends Fragment {

    /**
     * @param namespace The namespace for the root element of the data in the fragment.
     * @param name The local name for the root element of the data in the fragment.
     * @param qName The QName for the root element of the data in the fragment.
     * @throws XBRLException
     */
    public void appendDataElement(URI namespace, String name, String qName) throws XBRLException;    
}
