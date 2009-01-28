package org.xbrlapi.xdt;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Modified loader, adapted to use the XDT SAX content handler
 * to detect XDT fragments.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LoaderImpl extends org.xbrlapi.loader.LoaderImpl implements Loader {
    
    /**
     * @see org.xbrlapi.loader.LoaderImpl#LoaderImpl(Store, XLinkProcessor)
     */
    public LoaderImpl(Store store, XLinkProcessor xlinkProcessor) throws XBRLException {
        super(store,xlinkProcessor);
    }

    /**
     * @see org.xbrlapi.loader.LoaderImpl#LoaderImpl(Store, XLinkProcessor, List)
     */
    public LoaderImpl(Store store, XLinkProcessor xlinkProcessor, List<URI> uris)
            throws XBRLException {
        super(store,xlinkProcessor,uris);
    }


    

    static Logger logger = Logger.getLogger(LoaderImpl.class);
    
    /**
     * @see org.xbrlapi.loader.LoaderImpl#parse(URI)
     */
    protected boolean parse(URI uri) throws XBRLException {
        
        try {
            InputSource inputSource = this.getEntityResolver().resolveEntity("", uri.toString());
            ContentHandler contentHandler = new ContentHandlerImpl(this, uri);
            return parse(uri, inputSource, contentHandler);
        } catch (SAXException e) {
            logger.info("A SAX exception was thrown when resolving " + uri);
            getStore().deleteDocument(uri);
            getCache().purge(uri);
            logger.info("Purged " + uri + " from the data store and cache.");
            this.markDocumentAsCausingSAXExceptions(uri);
            return false;
        } catch (IOException e) {
            logger.info("An IO exception was thrown when resolving " + uri);
            getStore().deleteDocument(uri);
            getCache().purge(uri);
            logger.info("Purged " + uri + " from the data store and cache.");
            this.markDocumentAsCausingIOExceptions(uri);
            return false;
        }
    }

    /**
     * @see org.xbrlapi.loader.LoaderImpl#parse(URI, String)
     */
    protected boolean parse(URI uri, String xml) throws XBRLException {
        InputSource inputSource = new InputSource(new StringReader(xml));
        ContentHandler contentHandler = new ContentHandlerImpl(this, uri, xml);
        return parse(uri, inputSource, contentHandler);
    }
    
}
