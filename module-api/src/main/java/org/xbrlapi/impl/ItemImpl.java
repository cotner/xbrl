package org.xbrlapi.impl;

import java.util.List;

import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ItemImpl extends FactImpl implements Item {

	/**
     * 
     */
    private static final long serialVersionUID = 5737055509180629932L;

    /** 
	 * @see org.xbrlapi.Item#getContext()
	 */
	public Context getContext() throws XBRLException {
	    String query = "for $root in #roots#[@uri='"+this.getURI()+"' and @type='"+ContextImpl.class.getName()+"' and xbrlapi:data/xbrli:context/@id='"+this.getContextId()+"'] return $root";
	    List<Context> contexts = getStore().<Context>queryForXMLResources(query);
	    if (contexts.size() == 1) return contexts.get(0);
	    throw new XBRLException("There is not a unique matching context with ID "+this.getContextId()+" for this fact in instance " + this.getURI());
	}
	
    /** 
     * @see org.xbrlapi.Item#getContextId()
     */
    public String getContextId() throws XBRLException {
        Element root = getDataRootElement();
        if (root.hasAttribute("contextRef")) {
            return root.getAttribute("contextRef");
        }
        throw new XBRLException("The contextRef is missing on an item.");
    }

}
