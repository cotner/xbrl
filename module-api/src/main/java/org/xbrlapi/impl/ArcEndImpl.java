package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.FragmentList;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ArcEndImpl extends ExtendedLinkContentImpl implements ArcEnd {
	
	/**
     * Get the xlink:label attribute value.
     * @return the xlink:label attribute value.
     * @throws XBRLException if the xlink:label attribute does not exist.
     * @see org.xbrlapi.ArcEnd#getLabel()
     */
    public String getLabel() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XLinkNamespace,"label"))
    		return root.getAttributeNS(Constants.XLinkNamespace,"label");
    	throw new XBRLException("XLink arc ends must have an xlink:label attribute");
    }
    


    /**
     * Get the xlink:role attribute value.
     * @return the value of the id attribute or null if no
     * such attribute exists.
     * @throws XBRLException
     * @see org.xbrlapi.ArcEnd#getRole()
     */
    public String getRole() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XLinkNamespace,"role"))
    		return root.getAttributeNS(Constants.XLinkNamespace,"role");
    	return null;
    }
    

    
    /**
     * Get the list of arcs that are from the arc end.
     * @return the list of arcs from the arc end.  The list is empty 
     * if there are no arcs from the arc end.
     * @see org.xbrlapi.ArcEnd#getArcsFrom()
     */
    public FragmentList<Arc> getArcsFrom() throws XBRLException {
    	logger.debug("Getting the arcs from arc-end with label " + getLabel());
    	return getExtendedLink().getArcsByFromLabel(this.getLabel());
    }

    /**
     * Get the list of arcs that are to the arc end.
     * @return the list of arcs to the arc end.  The list is empty 
     * if there are no arcs to the arc end.
     * @see org.xbrlapi.ArcEnd#getArcsTo()
     */
    public FragmentList<Arc> getArcsTo() throws XBRLException {
    	logger.debug("Getting the arcs to end with label " + getLabel());
    	return getExtendedLink().getArcsByToLabel(this.getLabel());
    }


        
    /**
     * Get the id attribute value.
     * @return the value of the id attribute or null if no
     * such attribute exists.
     * @throws XBRLException
     * @see org.xbrlapi.ArcEnd#getArcEndId()
     */
    public String getArcEndId() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttribute("id"))
    		return root.getAttribute("id");
    	return null;
    }
    

    

    
}
