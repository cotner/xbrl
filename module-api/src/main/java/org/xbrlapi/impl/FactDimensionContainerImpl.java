package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.FactDimensionContainer;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FactDimensionContainerImpl extends FragmentImpl implements FactDimensionContainer {

    /**
     * Get the id attribute value of the container
     * @return the value of the id attribute for the unit or context.
     * @throws XBRLException if the id attribute is missing.
	 * @see org.xbrlapi.FactDimensionContainer#getId()
     */
    public String getId() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttribute("id")) return root.getAttribute("id");
    	throw new XBRLException("The id attribute is missing on a unit or context.");
    }

    /**
     * @see org.xbrlapi.FaceDimensionContainer#getReferencingItems()
     */
    public FragmentList<Item> getReferencingItems() throws XBRLException {
        String query = "/*[*/*/@*='" + this.getId() + "']";
        return getStore().<Item>query(query);
    }

}
