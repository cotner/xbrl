package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.Language;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class LanguageImpl extends FragmentImpl implements Language {

	/**
	 * @see org.xbrlapi.Language#getLanguage()
	 */
	public String getLanguage() throws XBRLException {
    	Element root = getDataRootElement();
    	NodeList nodes = root.getElementsByTagNameNS(Constants.XBRLAPILanguagesNamespace,"encoding");
    	Element encoding = (Element) nodes.item(0);
    	return encoding.getTextContent().toLowerCase();		
	}
	
	/**
	 * @see org.xbrlapi.Language#getCode()
	 */
	public String getCode() throws XBRLException {
    	Element root = getDataRootElement();
    	NodeList nodes = root.getElementsByTagNameNS(Constants.XBRLAPILanguagesNamespace,"code");
    	Element code = (Element) nodes.item(0);
    	return code.getTextContent().toLowerCase();		
	}	
	
	/**
	 * @see org.xbrlapi.Language#getName()
	 */
	public String getName() throws XBRLException {
    	Element root = getDataRootElement();
    	NodeList nodes = root.getElementsByTagNameNS(Constants.XBRLAPILanguagesNamespace,"name");
    	Element name = (Element) nodes.item(0);
    	return name.getTextContent();		
	}

}
