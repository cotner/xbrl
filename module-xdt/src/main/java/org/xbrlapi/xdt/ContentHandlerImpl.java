package org.xbrlapi.xdt;

import java.net.URL;

import org.apache.log4j.Logger;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.sax.identifiers.SchemaIdentifier;
import org.xbrlapi.sax.identifiers.XBRLIdentifier;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.SAXException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ContentHandlerImpl extends org.xbrlapi.sax.ContentHandlerImpl {

	protected static Logger logger = Logger.getLogger(ContentHandlerImpl.class);		
    
    /**
     * @see org.xbrlapi.SaxHandlers.ContentHandlerImpl#ContentHandlerImpl(Loader, URL)
     */
    public ContentHandlerImpl(Loader loader, URL url) throws XBRLException {
        super(loader,url);
    }
    	
    /**
     * @see org.xbrlapi.SaxHandlers.ContentHandlerImpl#ContentHandlerImpl(Loader, URL, String)
     */
    public ContentHandlerImpl(Loader loader, URL url, String xml) throws XBRLException {
        super(loader, url, xml);
    }

    /**
     * On starting to parse a document the Base URL resolver is 
     * set up with the documents absolute URL.  The fragment identifiers
     * are also instantiated and initialised.
     *
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException 
    {
        super.startDocument();
        
        // Detect XDT fragments first
        try {
            removeIdentifier(1);  // Remove standard schema identifier
            addIdentifier(1,new SchemaIdentifier(this));
            removeIdentifier(2);  // Remove XBRL 2.1 identifier
            addIdentifier(2,new XBRLIdentifier(this));
        } catch (XBRLException e) {
            throw new SAXException("A fragment identifier could not be instantiated.",e);
        }
    }    
    
}
