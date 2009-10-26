package org.xbrlapi.xdt.values;

import org.xbrlapi.Item;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface DimensionValue {

    /**
     * @return the item with this dimension value.
     * @throws XBRLException if the item is null.
     */
    public Item getItem() throws XBRLException;
    
    /**
     * @return the dimension that the value is for.
     * @throws XBRLException if the dimension cannot be retrieved.
     */
    public Dimension getDimension() throws XBRLException;

    /**
     * @see org.w3c.dom.Element
     * @see org.xbrlapi.Concept
     * @return an object that is either the XML DOM Element that is the child element
     * of the open context container (segment or scenario) 
     * that expresses the dimension value for typed dimensions; 
     * and that is the concept fragment that is the domain member, 
     * for explicit dimensions.
     * @throws XBRLException if the value cannot be retrieved.
     */
    public Object getValue() throws XBRLException;

    /**
     * @return The OCC containing the dimension value or null if
     * the dimension value is a default one.
     * @throws XBRLException
     */
    public OpenContextComponent getOpenContextComponent() throws XBRLException;
    
    /**
     * @return true if the dimension value is a typed dimension value and
     * false otherwise.
     * @throws XBRLException.
     */
    public boolean isTypedDimensionValue() throws XBRLException;

    /**
     * @return true if the dimension value is an explicit dimension value and
     * false otherwise.
     * @throws XBRLException.
     */
    public boolean isExplicitDimensionValue() throws XBRLException;

}
