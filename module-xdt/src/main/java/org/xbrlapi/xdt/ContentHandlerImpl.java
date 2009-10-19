package org.xbrlapi.xdt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.SAXException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ContentHandlerImpl extends org.xbrlapi.sax.ContentHandlerImpl {

	protected static Logger logger = Logger.getLogger(ContentHandlerImpl.class);		
    
    /**
     * @see org.xbrlapi.sax.ContentHandlerImpl#ContentHandlerImpl(Loader, URI)
     */
    public ContentHandlerImpl(Loader loader, URI uri) throws XBRLException {
        super(loader,uri);
    }
    	
    /**
     * @see org.xbrlapi.sax.ContentHandlerImpl#ContentHandlerImpl(Loader, URI, String)
     */
    public ContentHandlerImpl(Loader loader, URI uri, String xml) throws XBRLException {
        super(loader, uri, xml);
    }

    /**
     * Use an XBRL Dimensions aware Schema fragment identifier
     * to pick out the XDT element declarations for hypercubes,
     * explicit dimensions and typed dimensions.
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException 
    {
        super.startDocument();
        
        try {
            removeIdentifier(1);  // Remove standard schema identifier
/*            removeIdentifier(1);  // Remove XBRL 2.1 identifier
            addIdentifier(1,new XBRLIdentifier(this));
*/            addIdentifier(1,new SchemaIdentifier(this));
        } catch (XBRLException e) {
            throw new SAXException("A fragment identifier could not be instantiated.",e);
        }
        
    }    
    
}
