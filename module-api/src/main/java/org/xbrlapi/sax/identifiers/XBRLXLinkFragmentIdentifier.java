package org.xbrlapi.sax.identifiers;

import org.xbrlapi.loader.Loader;
import org.xbrlapi.sax.ContentHandler;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandler;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;
import org.xml.sax.Attributes;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XBRLXLinkFragmentIdentifier extends BaseIdentifier implements Identifier {

    /**
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#BaseFragmentIdentifierImpl(Loader)
     */
    public XBRLXLinkFragmentIdentifier(ContentHandler contentHandler) throws XBRLException {
        super(contentHandler);
    }

    private XLinkProcessor getXLinkProcessor() {
        return getLoader().getXlinkProcessor();
    }

    private XLinkHandler getXLinkHandler() {
        return getLoader().getXlinkProcessor().getXLinkHandler();
    }

    /**
     * Passes responsibility along to the XLink handler via the XLink Processor.
     * 
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#startElement(String,String,String,Attributes)
     */
    public void startElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {

        try {
            XBRLXLinkHandlerImpl xlinkHandler = (XBRLXLinkHandlerImpl) this.getXLinkHandler()
        } catch (ClassCastException e) {
            throw new XBRLException("The XBRLXLinkIdentifier must be used with an XBRLXLinkHandler.",e);
        }
            
            getXLinkProcessor().startElement(namespaceURI,lName,qName,attrs);
        } catch (XLinkException e) {
            throw new XBRLException("XLink processing of the start of an element failed.",e);
        }
        
    }
    
}
