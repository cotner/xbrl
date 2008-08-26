package org.xbrlapi.xdt;

import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.utilities.XBRLException;

/**
 * This inteface defines the methods available to 
 * interact with a dimension value.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface DimensionValue {

    /**
     * @return true if the value is of an explicit dimension
     * and false otherwise.
     * @throws XBRLException
     */
    public boolean isExplicitDimension() throws XBRLException;
    
    /**
     * @return true if the value is of a typed dimension and
     * false otherwise.
     * @throws XBRLException
     */
    public boolean isTypedDimension() throws XBRLException;

    /**
     * @return the element that is the root of the dimension value.
     * @throws XBRLException if the dimension value is for an 
     * explicit dimension.
     */
    public Element getTypedDimensionValue() throws XBRLException;
    
    /**
     * @return the concept that is the domain member that is 
     * the explicit dimension value.
     * @throws XBRLException if the dimension value is for a
     * typed dimension.
     */
    public Concept getExplicitDimensionValue() throws XBRLException;    
    
}
