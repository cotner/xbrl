package org.xbrlapi.xmlbase;

import java.net.URI;

/**
 * This interface declares the functionality expected
 * of classes that provide the XML Base functionality 
 * required by XBRL API.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */


public interface BaseURISAXResolver {

    /**
     * Returns the XML Base URI of the given element.
     * Used with the SAX API
     * @return the Base URI form.
     */
    public URI getBaseURI() throws XMLBaseException;
    
    /**
     * Updates the XML Base given new base information
     * Used on the start of element XML base event.
     * @param value the value of the xmlBase attribute if supplied.
     */
    public void addBaseURI(String value) throws XMLBaseException;
    
    /**
     * Reverts to the previous Base URI - used on the end of 
     * element XML base event.
     */
    public void removeBaseURI() throws XMLBaseException;

}
