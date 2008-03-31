package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ArcEnd extends ExtendedLinkContent {

	/**
     * Get the xlink:label attribute value.
     * @return the xlink:label attribute value.
     * @throws XBRLException if the xlink:label attribute does not exist.
     */
    public String getLabel() throws XBRLException;
    
    /**
     * Get the xlink:role attribute value.
     * @return the value of the id attribute or null if no
     * such attribute exists.
     * @throws XBRLException
     */
    public String getRole() throws XBRLException;
    
    /**
     * Get the id attribute value.
     * @return the value of the id attribute or null if no
     * such attribute exists.
     * @throws XBRLException
     */
    public String getArcEndId() throws XBRLException;
    
    /**
     * Get the list of arcs that are from the arc end.
     * @return the list of arcs from the arc end.  The list is empty 
     * if there are no arcs from the arc end.
     * @see org.xbrlapi.ArcEnd#getArcsFrom()
     */
    public FragmentList<Arc> getArcsFrom() throws XBRLException;

    /**
     * Get the list of arcs that are to the arc end.
     * @return the list of arcs to the arc end.  The list is empty 
     * if there are no arcs to the arc end.
     * @see org.xbrlapi.ArcEnd#getArcsTo()
     */
    public FragmentList<Arc> getArcsTo() throws XBRLException;
    
}
