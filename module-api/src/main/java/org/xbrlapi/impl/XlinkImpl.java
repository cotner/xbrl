package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import org.w3c.dom.Element;
import java.util.List;
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
     * 
     */
    private static final long serialVersionUID = 711390844077916793L;

    /**
     * @see org.xbrlapi.Xlink#getXlinkType()
     */
    public String getXlinkType() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace.toString(),"type");
    }

    /**
     * @see org.xbrlapi.Xlink#getTitleAttribute()
     */
    public String getTitleAttribute() throws XBRLException {
    	Element e = getDataRootElement();
    	if (e.hasAttributeNS(Constants.XLinkNamespace.toString(),"title"))
    		return getDataRootElement().getAttributeNS(Constants.XLinkNamespace.toString(),"title");
    	return null;
    }
    


    /**
     * @see org.xbrlapi.Xlink#getTitleElements()
     */
    public List<Title> getTitleElements() throws XBRLException {
    	return getChildren("org.xbrlapi.impl.TitleImpl");
    }

    /**
     * @see org.xbrlapi.Xlink#getTitleElement(int)
     */
    public Title getTitleElement(int index) throws XBRLException {
    	return (Title) getChild("org.xbrlapi.impl.TitleImpl", index);
    }

    /**
     * @see org.xbrlapi.Xlink#getAttribute(URI, String)
     */
    public String getAttribute(URI namespace, String localname) throws XBRLException {
    	if (namespace.equals(Constants.XLinkNamespace)) throw new XBRLException("XLink attributes must not be accessed using the getAttribute method on XLink fragments.");
    	return getDataRootElement().getAttributeNS(namespace.toString(),localname);
    }

    /**
     * @see org.xbrlapi.Xlink#getAttribute(String)
     */
    public String getAttribute(String name) throws XBRLException {
    	return getDataRootElement().getAttribute(name);
    }

    /**
     * Get the URI of the document containing the fragment targeted by the
     * supplied URI.
     * 
     * @param uri
     *            The supplied URI for decomposition.
     * @return The URI of the the document containing the fragment targeted by
     *         the supplied URI.
     * @throws XBRLException
     */
	protected URI getTargetDocumentURI(URI uri) throws XBRLException {
		try {
			return new URI(uri.getScheme(),null,uri.getHost(),uri.getPort(),uri.getPath(),null,null);
		} catch (URISyntaxException e) {
			throw new XBRLException("This exception can never be thrown.");
		}
	}    
    
	/**
	 * Get the value of the XPointer that corresponds to the XPointer information
	 * stored in the metadata of all fragments.
	 * @param pointer The String value of the XPointer supplied in the URI.
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
			throw new XBRLException("Parsing exception thrown for locator " + this.getIndex() + " with XPointer " + pointer, e);
        } catch (Throwable e) {
            throw new XBRLException("Other XPointer problem found for locator " + this.getIndex() + " with XPointer " + pointer, e);
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
