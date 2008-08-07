package org.xbrlapi.xdt;

import java.net.URL;

import org.apache.log4j.Logger;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.sax.ContentHandlerImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XDTContentHandlerImpl extends ContentHandlerImpl {

	protected static Logger logger = Logger.getLogger(XDTContentHandlerImpl.class);		
    
    /**
     * @see org.xbrlapi.SaxHandlers.ContentHandlerImpl#ContentHandlerImpl(Loader, URL)
     */
    public XDTContentHandlerImpl(Loader loader, URL url) throws XBRLException {
        super(loader,url);
    }
    	
    /**
     * @see org.xbrlapi.SaxHandlers.ContentHandlerImpl#ContentHandlerImpl(Loader, URL, String)
     */
    public XDTContentHandlerImpl(Loader loader, URL url, String xml) throws XBRLException {
        super(loader, url, xml);
    }
    
}
