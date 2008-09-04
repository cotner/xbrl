package org.xbrlapi.xdt.values;

import org.xbrlapi.Item;
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
    
}
