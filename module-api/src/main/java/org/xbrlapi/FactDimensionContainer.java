package org.xbrlapi;

import java.util.List;

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
     * @return the list of items in the instance that reference this context.
     * The list is empty if no facts in the instance reference this context.
     * @throws XBRLException
     */
    public <I extends Item> List<I> getReferencingItems() throws XBRLException;

}
