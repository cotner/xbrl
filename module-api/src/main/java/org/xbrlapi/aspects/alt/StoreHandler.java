package org.xbrlapi.aspects.alt;

import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public interface StoreHandler {

    /**
     * @return the underlying data store.
     * @throws XBRLException
     */
    public Store getStore() throws XBRLException;
    
}
