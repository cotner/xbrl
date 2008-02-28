package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Title;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class TitleImpl extends XlinkImpl implements Title {

    /**
     * Get the documentation text, with the leading and trailing spaces trimmed off.
     * @return the documentation text, with the leading and trailing spaces trimmed off.
     * @throws XBRLException
     * @see org.xbrlapi.Title#getValue()
     */
    public String getValue() throws XBRLException {
    	return getDataRootElement().getTextContent().trim();
    }
    
    /**
     * Set the documentation text.
     * @param value
     * @throws XBRLException
     * @see org.xbrlapi.Title#setValue(String)
     */
    public void setValue(String value) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

    /**
     * Get the xlink title language code.
     * @return the string value of the XML language attribute on 
     * the XLink title element or null if none is available.
     * @throws XBRLException
     * @see org.xbrlapi.Title#getLanguage()
     */
    public String getLanguage() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XMLNamespace,"lang"))
    		return root.getAttributeNS(Constants.XMLNamespace,"lang");
    	if (root.hasAttribute("xml:lang"))
    		return root.getAttribute("xml:lang");
    	return null;
    }
    
    /**
     * TODO Ensure that the language string is a value coding.
     * Set the xlink title language code.
     * @param language The language code to use.
     * @throws XBRLException
     * @see org.xbrlapi.Title#setLanguage(String)
     */
    public void setLanguage(String language) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

}
