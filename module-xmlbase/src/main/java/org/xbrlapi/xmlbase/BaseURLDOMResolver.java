package org.xbrlapi.xmlbase;

/**
 * This interface declares the functionality expected
 * of classes that provide the XML Base functionality 
 * required by XBRL API.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URL;

import org.w3c.dom.Element;

public interface BaseURLDOMResolver extends BaseURLResolver {

    /**
     * Returns the XML Base URL of the given element.
     * The Base URL is determined with reference to xml:base
     * attribute values and the Document URL etc as specified
     * in the XML Base 1.0 specification.
     * @param elt The XML DOM element that the Base URL is being
     * obtained for.
     * @return the Base URL for the element supplied.
     */
    public URL getBaseURL(Element elt) throws XMLBaseException;

    /**
     * Get the document URL 
     * @return The document URL
     */
    public URL getDocumentURL();
        
}
