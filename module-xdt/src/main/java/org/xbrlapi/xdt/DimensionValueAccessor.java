package org.xbrlapi.xdt;

import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * This inteface defines the methods available to 
 * access XDT dimension values.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface DimensionValueAccessor {
    
    /**
     * @param item The item to get the dimension value for.
     * @param namespace The namespace of the dimension to get the value for.
     * @param localname The local name of the dimension to get the value for.
     * @return the value of the named dimension for the specified fact or null
     * if the fact does not have a value for the specified dimension.
     * @throws XBRLException if the dimension namespace and local name do not
     * identify a typed or explicit dimension.
     */
    public DimensionValue getValue(Item item, String namespace, String localname) throws XBRLException;

    /**
     * @param first The first item.
     * @param second The second item.
     * @param namespace The dimension namespace.
     * @param localname The dimension local name.
     * @return true if the first and second items have equal values for the 
     * specified dimension and false otherwise.
     * @throws XBRLException
     */
    public boolean equalValues(Item first, Item second, String namespace, String localname) throws XBRLException;
    
}
