package org.xbrlapi;

import java.net.URI;
import java.util.List;

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
     * @return the value of the XLink role attribute
     * on the ArcEnd fragment or null if no such attribute exists.
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
     * @return Get the list of arcs that are from the arc end.  
     * The list is empty if there are no arcs from the arc end.
     */
    public List<Arc> getArcsFrom() throws XBRLException;

    /**
     * @return Get the list of arcs that are to the arc end.  
     * The list is empty if there are no arcs to the arc end.
     */
    public List<Arc> getArcsTo() throws XBRLException;
    
    /**
     * @param arcrole the required arcrole.
     * @return Get the list of arcs with the given arcrole 
     * that are from the arc end.  
     * The list is empty if there are no matching arcs.
     */
    public List<Arc> getArcsFromWithArcrole(URI arcrole) throws XBRLException;
    


    /**
     * @param arcrole the required arcrole.
     * @return Get the list of arcs with the given arcrole 
     * that are to the arc end.  
     * The list is empty if there are no matching arcs.
     */
    public List<Arc> getArcsToWithArcrole(URI arcrole) throws XBRLException;
    
}
