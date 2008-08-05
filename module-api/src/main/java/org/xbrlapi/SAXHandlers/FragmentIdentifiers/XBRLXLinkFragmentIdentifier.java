package org.xbrlapi.SAXHandlers.FragmentIdentifiers;

import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandler;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xml.sax.Attributes;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XBRLXLinkFragmentIdentifier extends FragmentIdentifier {

    /**
     * @see org.xbrlapi.SAXHandlers.FragmentIdentifiers.FragmentIdentifier#BaseFragmentIdentifierImpl(Loader)
     */
    public XBRLXLinkFragmentIdentifier(Loader loader) throws XBRLException {
        super(loader);
    }

    private XLinkProcessor getXLinkProcessor() {
        return getLoader().getXlinkProcessor();
    }

    private XLinkHandler getXLinkHandler() {
        return getLoader().getXlinkProcessor().getXLinkHandler();
    }

    /**
     * @see org.xbrlapi.SAXHandlers.FragmentIdentifiers.FragmentIdentifier#idFragment(String,String,String,Attributes)
     */
    public void idFragment(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {

        try {

            // Next pass control to the XLink processor so it can recognise and respond to XLink elements
            getXLinkProcessor().startElement(namespaceURI,lName,qName,attrs);
            
        } catch (XLinkException e) {
            throw new XBRLException("XLink processing of the start of an element failed.",e);
        }
        
    }
    
}
