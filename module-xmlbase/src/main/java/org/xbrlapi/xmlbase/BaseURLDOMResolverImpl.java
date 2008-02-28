package org.xbrlapi.xmlbase;
/**
 * This class implements the XML Base
 * required by the XBRL API.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BaseURLDOMResolverImpl extends BaseURLResolverImpl implements BaseURLDOMResolver {

	final String XMLNamespace = "http://www.w3.org/XML/1998/namespace";
	
	/**
	 * The absolute URL of the document that this Base URL
	 * resolver will be used with.
	 */
	private URL documentURL;

    /**
     * Constructor takes the absolute URL of the
     * document to be processed using this BaseURLSAXResolver
     * Implementation as an input.  This is then used
     * as part of the Base URL resolution.
     * @param documentURL The absolute URL of
     * the base document.  If the base document does not
     * have a base URL then this parameter must be null if
     * this constructor is being used.
     */
    public BaseURLDOMResolverImpl(URL documentURL) {
    	super();
    	this.documentURL = documentURL;
    }

    /**
     * Constructor for processing documents without a 
     * base URL.  Sets the documentURL to null.
     */
    public BaseURLDOMResolverImpl() {
    	super();
    	this.documentURL = null;
    }
        
    /**
     * Returns the XML Base URL of the given element.
     * The Base URL is determined with reference to xml:base
     * attribute values and the Document URL etc as specified
     * in the XML Base 1.0 specification.
     * 
     * Step 1.
     * Works through the heirarchy of parent elements
     * for the given element, determining their Base URLs. 
     * This is recursive in the sense that the Base URL 
     * is determined for each parent element and that Base URL
     * is then used as part of determining the Base URL for
     * the element being analysed.
     * 
     * Step 2.
     * if the Base URL cannot be determined for the 
     * parent elements, then determine it from the document
     * that contains the element being analysed.
     * 
     * Steps 1 and 2 may give a URL that any xml:base
     * attribute on the element of interest can be resolved 
     * relative to, to get an absolute Base URL.
     * 
     * TODO work out when this URL will not be available.
     * 
     * Step 3.
     * If no Base URL is determinable for the parent element 
     * or for the containing document, then the Base URL is
     * just the value of the xml:base attribute on the element
     * of interest. This must be checked for validity as an
     * absolute URL.
     * If a Base URL is determinable for the parent element 
     * or for the containing document, then the Base URL is
     * determined by resolving the value of the xml:base
     * attribute relative to the Base URL of the parent
     * element or the containing document.
     * 
     * @param elt The XML DOM element that the Base URL is being
     * obtained for.
     * @return the Base URL.
     */
    public URL getBaseURL(Element elt) throws XMLBaseException {

    	// Begin by presuming nothing about the base URL
    	URL base = null;
    	
        Node n = elt.getParentNode();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                base = getBaseURL((Element) n);
                break;
            }
                n = n.getParentNode();
        }
        
        // If ancestor elements are silent, use the document base
        // but note that the document base URL can be null itself
        // if the document does not have a URL.
        if (base == null) {
        	base = getDocumentURL();
        }
        
        // Examine any XML Base attribute on the element itself
        Attr attr = elt.getAttributeNodeNS(XMLNamespace, "base");
        if (attr != null) {
            if (base == null) {
            	try {
            		base = new URL(attr.getNodeValue());
            	} catch (MalformedURLException e) {
            		throw new XMLBaseException("Base URL attribute contains a Malformed URL: " + attr.getNodeValue(),e);
            	}
            } else {
            	try {
                    base = new URL(base, attr.getNodeValue());
            	} catch (MalformedURLException e) {
            		throw new XMLBaseException("Base URL resolution of attribute value " + attr.getNodeValue() + " against " + base + "involved a Malformed URL.",e);
            	}
            }
        }
        return base;
    }

    /**
     * Get the document URL 
     * @return The document URL
     */
    public URL getDocumentURL() {
    	return documentURL;
    }
    
}
