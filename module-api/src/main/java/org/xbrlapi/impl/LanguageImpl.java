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
     * 
     */
    private static final long serialVersionUID = 1549365928850844441L;

    /**
	 * @see org.xbrlapi.Language#getEncoding()
	 */
	public String getEncoding() throws XBRLException {
    	Element root = getDataRootElement();
    	NodeList nodes = root.getElementsByTagNameNS(Constants.XBRLAPILanguagesNamespace.toString(),"encoding");
    	Element encoding = (Element) nodes.item(0);
    	return encoding.getTextContent();		
	}
	
	/**
	 * @see org.xbrlapi.Language#getCode()
	 */
	public String getCode() throws XBRLException {
    	Element root = getDataRootElement();
    	NodeList nodes = root.getElementsByTagNameNS(Constants.XBRLAPILanguagesNamespace.toString(),"code");
    	Element code = (Element) nodes.item(0);
    	return code.getTextContent();		
	}	
	
	/**
	 * @see org.xbrlapi.Language#getName()
	 */
	public String getName() throws XBRLException {
    	Element root = getDataRootElement();
    	NodeList nodes = root.getElementsByTagNameNS(Constants.XBRLAPILanguagesNamespace.toString(),"name");
    	Element name = (Element) nodes.item(0);
    	return name.getTextContent();		
	}

}
