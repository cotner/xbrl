package org.xbrlapi.xmlbase;
/**
 * This class implements the XML Base
 * required by the XBRL API.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

public class BaseURLSAXResolverImpl extends BaseURLResolverImpl implements BaseURLSAXResolver {
	
	/**
	 * The base URL stack as constructed during the SAX
	 * parsing process
	 * resolver will be used with.
	 */
	private Stack<URL> baseURLs = new Stack<URL>();
	
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
    public BaseURLSAXResolverImpl(URL documentURL) {
    	baseURLs.push(documentURL);
    }

    /**
     * Constructor for processing documents without a 
     * base URL.  Sets the documentURL to null.
     */
    public BaseURLSAXResolverImpl()  {
    	baseURLs.push(null);
    }

    public URL getBaseURL() throws XMLBaseException {
    	return baseURLs.peek();
    }
    
    /**
     * Updates the XML Base given new base information
     * Used on the start of element XML base event.
     * @param xmlBase the value of the xmlBase attribute if supplied.
     */
    public void addBaseURL(String xmlBase) throws XMLBaseException {

    	// If no xmlBase information then just use the current one
    	if ((xmlBase == "") || (xmlBase == null)) {
    		baseURLs.push(getBaseURL());
    		return;
    	}
    	
		URL base = null;
		if (getBaseURL() == null) {
        	try {
        		base = new URL(xmlBase);
        	} catch (MalformedURLException e) {
        		throw new XMLBaseException("Base URL attribute contains a Malformed URL: " + xmlBase,e);
        	}
        } else {
        	try {
                base = new URL(getBaseURL(), xmlBase);
        	} catch (MalformedURLException e) {
        		throw new XMLBaseException("Base URL resolution of attribute value " + xmlBase + " against " + getBaseURL() + "involved a Malformed URL.",e);
        	}
        }
		baseURLs.push(base);
    }

    /**
     * Reverts to the previous Base URL - used on the end of 
     * element XML base event.
     */
    public void removeBaseURL() throws XMLBaseException {
		baseURLs.pop();
    }
}
