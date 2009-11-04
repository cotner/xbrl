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
     * Get the documentation language code.
     *
     * @return the string value of the XML language attribute on 
     * the XLink title element or null if none is available.
     * @throws XBRLException
     */
    public String getLanguage() throws XBRLException;
    


}
