package org.xbrlapi.xmlbase;
/**
 * This class implements the XML Base
 * required by the XBRL API.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

public class BaseURISAXResolverImpl extends BaseURIResolverImpl implements BaseURISAXResolver, Serializable {
	
    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -3463357951277162491L;

    /**
	 * The base URI stack as constructed during the SAX
	 * parsing process
	 * resolver will be used with.
	 */
	transient private Stack<URI> baseURIs = new Stack<URI>();
	
    /**
     * Constructor takes the absolute URI of the
     * document to be processed using this BaseURISAXResolver
     * Implementation as an input.  This is then used
     * as part of the Base URI resolution.
     * @param documentURI The absolute URI of
     * the base document.  If the base document does not
     * have a base URI then this parameter must be null if
     * this constructor is being used.
     */
    public BaseURISAXResolverImpl(URI documentURI) {
    	baseURIs.push(documentURI);
    }

    /**
     * Constructor for processing documents without a 
     * base URI.  Sets the documentURI to null.
     */
    public BaseURISAXResolverImpl()  {
    	baseURIs.push(null);
    }

    public URI getBaseURI() throws XMLBaseException {
    	return baseURIs.peek();
    }
    
    /**
     * Updates the XML Base given new base information
     * Used on the start of element XML base event.
     * @param xmlBase the value of the xmlBase attribute if supplied.
     */
    public void addBaseURI(String xmlBase) throws XMLBaseException {

    	// If no xmlBase information then just use the current one
    	if ((xmlBase == "") || (xmlBase == null)) {
    		baseURIs.push(getBaseURI());
    		return;
    	}
    	
		URI base = null;
		if (getBaseURI() == null) {
        	try {
        		base = new URI(xmlBase);
        	} catch (URISyntaxException e) {
        		throw new XMLBaseException("Base URI attribute contains a Malformed URI: " + xmlBase,e);
        	}
        } else {
        	try {
                base = getBaseURI().resolve(new URI(xmlBase));
        	} catch (URISyntaxException e) {
        		throw new XMLBaseException("Base URI resolution of attribute value " + xmlBase + " against " + getBaseURI() + "involved a Malformed URI.",e);
        	}
        }
		baseURIs.push(base);
    }

    /**
     * Reverts to the previous Base URI - used on the end of 
     * element XML base event.
     */
    public void removeBaseURI() throws XMLBaseException {
		baseURIs.pop();
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
