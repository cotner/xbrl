package org.xbrlapi.sax.identifiers;

import org.xbrlapi.loader.Loader;
import org.xbrlapi.sax.ContentHandler;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.ElementState;
import org.xml.sax.Attributes;

/**
 * The class implements the common methods for a fragment identifier.
 * This fragment identifier will never identify a fragment.
 * Customised fragment identifiers should extend this base
 * class to inherit the implementations of the common methods.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class BaseIdentifier implements Identifier {

    /**
     * The content handler that is using this fragment identifier
     */
    private ContentHandler contentHandler = null;
    /**
     * @see Identifier#getContentHandler()
     */
    public ContentHandler getContentHandler() {
        return contentHandler;
    }
    /**
     * @see Identifier#setContentHandler(ContentHandler)
     */
    public void setContentHandler(ContentHandler contentHandler) throws XBRLException {
        if (contentHandler == null) throw new XBRLException("The content handler must not be null.");
        this.contentHandler = contentHandler;
    }
    
    /**
     * @see Identifier#getLoader()
     */
    public Loader getLoader() {
        return getContentHandler().getLoader();
    }
    
    /**
     * @see Identifier#getElementState()
     */
    public ElementState getElementState() {
        return getContentHandler().getState();
    }
    
    /**
     * @param contentHandler The content handler using this fragment identifier.
     * @throws XBRLException if the XLink processor is null.
     */
    public BaseIdentifier(ContentHandler contentHandler) throws XBRLException {
        setContentHandler(contentHandler);
    }

    /**
     * @see Identifier#startElement(String, String, String, Attributes)
     */
    public void startElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {
        ;
    }
    
    
    /**
     * @see Identifier#endElement(String, String, String, Attributes)
     */
    public void endElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {
        ;
    }
    
}
