package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface FactDimensionContainer extends Fragment {
    
    /**
     * Get the id attribute value of the container
     * @return the value of the id attribute for the unit or context.
     * @throws XBRLException if the id attribute is missing.
     */
    public String getId() throws XBRLException;

    /**
     * Set the id attribute value.
     * @param id The new value for the id attribute.
     * @throws XBRLException if the id
     * is not unique in the XML document.
     */
    public void setId(String id) throws XBRLException;

}
