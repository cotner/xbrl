package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Title extends Xlink {

    /**
     * Get the documentation text, with the leading and trailing spaces trimmed off.
     * @return the documentation text, with the leading and trailing spaces trimmed off.
     * @throws XBRLException
     */
    public String getValue() throws XBRLException;
    
    /**
     * Set the documentation text.
     *
     * @param value
     * @throws XBRLException
     */
    public void setValue(String value) throws XBRLException;

    /**
     * Get the documentation language code.
     *
     * @return the string value of the XML language attribute on 
     * the XLink title element or null if none is available.
     * @throws XBRLException
     */
    public String getLanguage() throws XBRLException;
    
    /**
     * Set the documentation language code.
     * @param language The language code to use.
     * @throws XBRLException
     */
    public void setLanguage(String language) throws XBRLException;

}
