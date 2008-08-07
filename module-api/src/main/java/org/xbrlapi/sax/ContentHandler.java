package org.xbrlapi.sax;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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
     * @return the URL of the document being parsed.
     */
    public URL getURL();

    /**
     * @param url The URL of the document being parsed.
     * @throws XBRLException if the URL is null.
     */
    public void setURL(URL url) throws XBRLException;
    
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
     * @return the stack of namespace maps necessary to 
     * decorate fragments with inherited namespaces.
     * Each map is from a namespace to a xmlns attribute name
     * (xmlns or xmlns:xyz).
     */
    public Stack<HashMap<String,String>> getNamespaceMaps();
    
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
