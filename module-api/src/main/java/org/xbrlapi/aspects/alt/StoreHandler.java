package org.xbrlapi.aspects.alt;

import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public interface StoreHandler {

    /**
     * @return the underlying data store.
     * @throws XBRLException
     */
    public Store getStore() throws XBRLException;
    
    /**
     * @param store
     *            The data store to use.
     * @throws XBRLException
     *             if the data store is null.
     */
    public void setStore(Store store) throws XBRLException;
}
