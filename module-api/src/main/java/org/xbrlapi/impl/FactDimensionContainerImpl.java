package org.xbrlapi.impl;

import java.util.List;

import org.w3c.dom.Element;
import org.xbrlapi.FactDimensionContainer;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FactDimensionContainerImpl extends FragmentImpl implements FactDimensionContainer {

    /**
     * 
     */
    private static final long serialVersionUID = -6970469751478915472L;

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
     * @see org.xbrlapi.FactDimensionContainer#getReferencingItems()
     */
    public <I extends Item> List<I> getReferencingItems() throws XBRLException {
        String query = "#roots#[*/*/@*='" + this.getId() + "']";
        return getStore().<I>queryForXMLResources(query);
    }

}
