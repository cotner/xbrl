package org.xbrlapi.sax;

import java.net.URI;
import java.util.List;

import org.xbrlapi.loader.Loader;
import org.xbrlapi.sax.identifiers.Identifier;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.ElementState;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 *
 */
public interface ContentHandler extends org.xml.sax.ContentHandler {

    /**
     * @return the loader using this content handler.
     */
    public Loader getLoader();
    
    /**
     * @param loader The loader that will use the content handler.
     * @throws XBRLException if the loader is null
     */
    public void setLoader(Loader loader) throws XBRLException;
    
    /**
     * @return the URI of the document being parsed.
     */
    public URI getURI();

    /**
     * @param uri The URI of the document being parsed.
     * @throws XBRLException if the URI is null.
     */
    public void setURI(URI uri) throws XBRLException;
    
    /**
     * @param state The element state
     */
    public void setElementState(ElementState state);
    
    /**
     * @return the state for the element currently being parsed.
     */
    public ElementState getElementState();

    /**
     * @return the list of fragment identifiers used by the content handler.
     */
    public List<Identifier> getIdentifiers();
    
    /**
     * Handles SAX parsing exceptions.
     * @param exception The SAX parsing exception
     * @throws SAXException
     */
    public void error(SAXParseException exception) throws SAXException;

    /**
     * Handles fatal SAX parsing exceptions.
     * @param exception The fatal SAX parsing exception.
     * @throws SAXException
     */
    public void fatalError(SAXParseException exception) throws SAXException;

    /**
     * Handles warnings based on SAX parsing exceptions.
     * @param exception the SAX parsing exception triggering the warning.
     * @throws SAXException
     */
    public void warning(SAXParseException exception) throws SAXException;
    
    
}
