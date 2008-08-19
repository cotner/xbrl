package org.xbrlapi.xdt;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

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
    public LoaderImpl(Store store, XLinkProcessor xlinkProcessor, List<URL> urls)
            throws XBRLException {
        super(store,xlinkProcessor,urls);
    }


    
    /**
     * @see org.xbrlapi.loader.LoaderImpl#parse(URL)
     */
    protected void parse(URL url) throws XBRLException {
        
        try {
            InputSource inputSource = this.getEntityResolver().resolveEntity("", url.toString());
            ContentHandler contentHandler = new ContentHandlerImpl(this, url);
            parse(url, inputSource, contentHandler);
        } catch (SAXException e) {
            throw new XBRLException("SAX exception thrown when parsing " + url,e);
        } catch (IOException e) {
            throw new XBRLException("IO exception thrown when parsing " + url,e);
        }
    }

    /**
     * @see org.xbrlapi.loader.LoaderImpl#parse(URL, String)
     */
    protected void parse(URL url, String xml) throws XBRLException {
        InputSource inputSource = new InputSource(new StringReader(xml));
        ContentHandler contentHandler = new ContentHandlerImpl(this, url, xml);
        super.parse(url, inputSource, contentHandler);
    }
    
}
