package org.xbrlapi.xmlbase;
/**
 * This class implements the XML Base required by the XBRL API.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BaseURIDOMResolverImpl extends BaseURIResolverImpl implements BaseURIDOMResolver {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = 8478951714108359135L;

    private static final Logger logger = Logger.getLogger(BaseURIDOMResolverImpl.class);
    
	/**
	 * The absolute URI of the document that this Base URI
	 * resolver will be used with.  This is set to null for
	 * documents without a URI.
	 */
	transient private URI documentURI;

    /**
     * Constructor takes the absolute URI of the
     * document to be processed using this BaseURISAXResolver
     * implementation as an input.  This is then used
     * in base URI resolution.
     * @param documentURI The absolute URI of
     * the base document. If the document does not
     * have a base URI then this parameter must be null if
     * this constructor is being used.
     * @see BaseURIDOMResolverImpl#BaseURIDOMResolverImpl()
     * @throws XMLBaseException if the document URI is not 
     * absolute or is opaque.
     */
    public BaseURIDOMResolverImpl(URI documentURI) throws XMLBaseException{
    	super();
    	if (documentURI.isOpaque())
            throw new XMLBaseException("The document URI must not be opaque. It is " + documentURI);
    	if (!documentURI.isAbsolute()) 
    	    throw new XMLBaseException("The document URI must be absolute. It is " + documentURI);
    	this.documentURI = documentURI;
    }

    /**
     * Constructor for processing documents without a 
     * base URI.  Sets the documentURI to null.
     */
    public BaseURIDOMResolverImpl() {
    	super();
    	this.documentURI = null;
    }
        
    /**
     * @see BaseURIDOMResolver#getBaseURI(Element)
     */
    public URI getBaseURI(Element elt) throws XMLBaseException {

        
    	// Begin by presuming nothing about the base URI
    	URI baseURI = null;

    	// Try to get the base URI for the parent element if one exists
    	Node n = elt.getParentNode();
        if (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                baseURI = getBaseURI((Element) n);
            } else {
                baseURI = getDocumentURI();
            }
        } else {
            baseURI = getDocumentURI();
        }
        
        logger.debug("Document URI is " + getDocumentURI());
        logger.debug("Inherited base URI is " + baseURI);
        
        if (baseURI != null) {
            if (baseURI.isOpaque()) throw new XMLBaseException("Opaque base URIs are not permitted.");
            if (!baseURI.isAbsolute()) throw new XMLBaseException("Relative base URIs are not permitted.");
        }
        
        // Examine any XML base attribute on the element itself
        Attr xmlBaseAttribute = elt.getAttributeNodeNS(BaseURIResolver.XML_NAMESPACE, "base");
        if (xmlBaseAttribute != null) {
            String value = xmlBaseAttribute.getNodeValue();
            logger.debug("XML Base attribute value is " + value);
            if (baseURI == null) {
            	try {
            		baseURI = new URI(xmlBaseAttribute.getNodeValue());
            		if (! baseURI.isAbsolute()) throw new XMLBaseException("The base URI must be absolute.");
                    if (baseURI.isOpaque()) throw new XMLBaseException("The base URI must not be opaque.");
            	} catch (URISyntaxException e) {
            		throw new XMLBaseException("XML Base attribute contains malformed URI: " + value,e);
            	}
            } else {
            	try {
                    baseURI = baseURI.resolve(new URI(value));
            	} catch (URISyntaxException e) {
            		throw new XMLBaseException("Failed to resolve " + value + " against " + baseURI,e);
            	}
            }
        }
        logger.debug("Adjusted base URI is " + baseURI);
        return baseURI;
    }

    /**
     * @see BaseURIDOMResolver#getDocumentURI()
     */
    public URI getDocumentURI() {
    	return documentURI;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
        
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }    
    

    
    
    
}
