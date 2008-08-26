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
     * @param item The item to check for a value for the given dimension.
     * @param namespace The namespace of the dimension to check the value for.
     * @param localname The local name of the dimension to check the value for.
     * @return true if the item does have a value for the specified dimension and false otherwise.
     */
    public boolean hasValue(Item item, String namespace, String localname) throws XBRLException;

    /**
     * @param item The item to check for a value for the given dimension.
     * @param namespace The namespace of the dimension to check the value for.
     * @param localname The local name of the dimension to check the value for.
     * @return true if the item does have an explicit value for the specified dimension 
     * and false otherwise (no value or a default value).
     */
    public boolean hasExplicitValue(Item item, String namespace, String localname) throws XBRLException;

}
