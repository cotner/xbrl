package org.xbrlapi.xdt.values;

import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.Item;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface DimensionValueAccessor {
    
    /**
     * @param item The item to get the dimension value for.
     * @param dimension The dimension to get the value for.
     * @return the value of the named dimension for the specified fact or null
     * if the fact does not have a value for the specified dimension.
     * @throws XBRLException
     */
    public DimensionValue getValue(Item item, Dimension dimension) throws XBRLException;


    /**
     * TODO move this equalValues method out of the dimension value accessor.
     * @param first The first item.
     * @param second The second item.
     * @param dimension The dimension.
     * @return true if the first and second items have equal values for the 
     * specified dimension and false otherwise.
     * @throws XBRLException
     */
    public boolean equalValues(Item first, Item second, Dimension dimension) throws XBRLException;
    
    
    /**
     * @param occ The open context component fragment (segment or scenario) to get the dimension value from
     * @param dimension The dimension
     * @return The child element of the OCC that contains the typed dimension value or null if there is none.
     * @throws XBRLException
     */
    public Element getTypedDimensionContentFromOpenContextComponent(OpenContextComponent occ,Dimension dimension) throws XBRLException;
    
    /**
     * @param occ The open context component fragment (segment or scenario) to get the dimension value from
     * @param dimension The dimension
     * @return The domain member that is the dimension value for the item.
     * @throws XBRLException
     */
    public Concept getDomainMemberFromOpenContextComponent(OpenContextComponent occ, Dimension dimension) throws XBRLException;

    /**
     * @param item The fact to get the typed dimension value for.
     * @param dimension The dimension to get the value for.
     * @return the element containing the typed dimension value.  This is a child element of the segment
     * or scenario that contains the typed dimension value.
     * @throws XBRLException
     */
    public DimensionValue getTypedDimensionValue(Item item, Dimension dimension) throws XBRLException;

    
    /**
     * @param item The fact to get the explicit dimension value for.
     * @param dimension The dimension to get the value for.
     * @return the value of the explicit dimension or null if there is none.
     * @throws XBRLException
     */
    public DimensionValue getExplicitDimensionValue(Item item, Dimension dimension) throws XBRLException;
    
    
}
