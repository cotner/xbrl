package org.xbrlapi.xmlbase;

/**
 * This interface declares the functionality expected
 * of classes that provide the XML Base functionality 
 * required by XBRL API.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URL;

public interface BaseURLSAXResolver {

    /**
     * Returns the XML Base URL of the given element.
     * Used with the SAX API
     * @return the Base URL form.
     */
    public URL getBaseURL() throws XMLBaseException;
    
    /**
     * Updates the XML Base given new base information
     * Used on the start of element XML base event.
     * @param value the value of the xmlBase attribute if supplied.
     */
    public void addBaseURL(String value) throws XMLBaseException;
    
    /**
     * Reverts to the previous Base URL - used on the end of 
     * element XML base event.
     */
    public void removeBaseURL() throws XMLBaseException;

}
