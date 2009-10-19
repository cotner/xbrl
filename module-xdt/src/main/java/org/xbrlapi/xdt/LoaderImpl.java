package org.xbrlapi.xdt;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URI;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

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
public class LoaderImpl extends org.xbrlapi.loader.LoaderImpl implements Loader, Serializable {
    
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


    

    private final static Logger logger = Logger.getLogger(LoaderImpl.class);
    
    /**
     * Parse an XML Document supplied as a URI the next part of the DTS.
     * @param uri The URI of the document to parse.
     * @throws XBRLException IOException ParserConfigurationException SAXException
     */
    @Override
    protected void parse(URI uri) throws XBRLException, ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = null;
        inputSource = this.getEntityResolver().resolveEntity("", uri.toString());
        ContentHandler contentHandler = new ContentHandlerImpl(this, uri);
        parse(uri, inputSource, contentHandler);
    }


    /**
     * Parse an XML Document supplied as a string the next part of the DTS.
     * @param uri The URI to associate with the supplied XML.
     * @param xml The XML document as a string.
     * @throws XBRLException IOException SAXException ParserConfigurationException
     */
    @Override
    protected void parse(URI uri, String xml) throws XBRLException, ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = new InputSource(new StringReader(xml));
        ContentHandler contentHandler = new ContentHandlerImpl(this, uri, xml);
        parse(uri, inputSource, contentHandler);
    }
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
    }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }        

    
}
