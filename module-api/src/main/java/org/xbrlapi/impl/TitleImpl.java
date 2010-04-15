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
     * 
     */
    private static final long serialVersionUID = 3219710333476685455L;



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
     * Get the xlink title language code.
     * @return the string value of the XML language attribute on 
     * the XLink title element or null if none is available.
     * @throws XBRLException
     * @see org.xbrlapi.Title#getLanguage()
     */
    public String getLanguage() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XMLNamespace.toString(),"lang"))
    		return root.getAttributeNS(Constants.XMLNamespace.toString(),"lang");
    	if (root.hasAttribute("xml:lang"))
    		return root.getAttribute("xml:lang");
    	return null;
    }
    


}
