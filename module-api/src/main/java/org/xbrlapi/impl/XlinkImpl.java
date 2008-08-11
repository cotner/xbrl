package org.xbrlapi.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.w3c.dom.Element;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Title;
import org.xbrlapi.Xlink;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xpointer.ParseException;
import org.xbrlapi.xpointer.PointerGrammar;
import org.xbrlapi.xpointer.PointerPart;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

class XlinkImpl extends FragmentImpl implements Xlink {

    /**
     * Get the xlink type (The xxlink:type attribute value).
     * @return the value of the xlink:type attribute.
     * @throws XBRLException
     * @see org.xbrlapi.Xlink#getXlinkType()
     */
    public String getXlinkType() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"type");
    }

    /**
     * Get the  title (The xlink:title attribute value).
     * @return the value of the xlink:title attribute or null if none is supplied. 
     * @throws XBRLException
     * @see org.xbrlapi.Xlink#getTitleAttribute()
     */
    public String getTitleAttribute() throws XBRLException {
    	Element e = getDataRootElement();
    	if (e.hasAttributeNS(Constants.XLinkNamespace,"title"))
    		return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"title");
    	return null;
    }
    


    /**
     * Get a list of titles (The xlink:title children elements).
     * @return a fragment list of title elements or null if there are none.
     * @throws XBRLException
     * @see org.xbrlapi.Xlink#getTitleElements()
     */
    public FragmentList<Title> getTitleElements() throws XBRLException {
    	return getChildren("org.xbrlapi.impl.TitleImpl");
    }

    /**
     * Get a specific title fragment 
     * @param index The index of the required title element
     * @return the title fragment or null if there are no title children elements.
     * @throws XBRLException if the index is out of bounds
     * @see org.xbrlapi.Xlink#getTitleElement(int)
     */
    public Title getTitleElement(int index) throws XBRLException {
    	return (Title) getChild("org.xbrlapi.impl.TitleImpl", index);
    }

    /**
     * @see org.xbrlapi.Xlink#getAttribute(String, String)
     */
    public String getAttribute(String namespaceURI, String localname) throws XBRLException {
    	if (namespaceURI.equals(Constants.XLinkNamespace)) throw new XBRLException("XLink attributes must not be accessed using the getAttribute method on XLink fragments.");
    	return getDataRootElement().getAttributeNS(namespaceURI,localname);
    }

    /**
     * @see org.xbrlapi.Xlink#getAttribute(String)
     */
    public String getAttribute(String name) throws XBRLException {
    	return getDataRootElement().getAttribute(name);
    }
    

    
	/** 
	 * Get the URL of the document containing the fragment targetted
	 * by the supplied URL.
	 * @param url The supplied URL for decomposition.
	 * @return The URL of the the document containing the fragment targetted
	 * by the supplied URL.
	 * @throws XBRLException
	 */
	protected URL getTargetDocumentURL(URL url) throws XBRLException {
		try {
			return new URL(url.getProtocol(),url.getHost(),url.getPort(),url.getFile());
		} catch (MalformedURLException e) {
			throw new XBRLException("This exception can never be thrown.");
		}
	}    
    
	/**
	 * Get the value of the XPointer that corresponds to the XPointer information
	 * stored in the metadata of all fragments.
	 * @param pointer The String value of the XPointer supplied in the URL.
	 * @return The value of the XPointer corresponding to the XPointer information
	 * stored in the metadata of all fragments.  Returns the empty string if the XPointer does
	 * not specify an element scheme or ID based shorthand pointer value.
	 * @throws XBRLException
	 */
	@SuppressWarnings("unchecked")
	protected String getTargetPointerValue(String pointer) throws XBRLException {
		
		if (pointer == null) return "";
		if (pointer.equals("")) return "";
		
	  	java.io.StringReader sr = new java.io.StringReader(pointer);
		java.io.Reader reader = new java.io.BufferedReader(sr);
		PointerGrammar parser = new PointerGrammar(reader);
		Vector pointerParts = null;
		try {
			pointerParts = parser.Pointer();
		} catch (ParseException e) {
			throw new XBRLException(pointer + " failed to parse as an XPointer for locator " + this.getFragmentIndex(), e);
		}
			
		for (int i=0; i<pointerParts.size(); i++) {
			
			PointerPart part = (PointerPart) pointerParts.get(i);
			String data = part.getUnescapedSchemeData();
			if (part.getSchemeNamespace().toString().equals(PointerPart.DefaultPointerNamespace) && part.getSchemeLocalName().equals("element")) {
				return data;
			}
		}

		return "";
		
	}    
}
