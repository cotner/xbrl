package org.xbrlapi.aspects.alt;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * This is a generic aspect value implementation. All concrete aspect value
 * implementations should extend this generic implementation.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class Base implements StoreHandler {

    protected final static Logger logger = Logger
            .getLogger(Base.class);

    /**
     * The underlying store.
     */
    private Store store;
    
    public Base(Store store) throws XBRLException {
        setStore(store);
    }
    
    /**
     * @see StoreHandler#getStore()
     */
    public Store getStore() throws XBRLException {
        return this.store;
    }
    
    /**
     * @see StoreHandler#setStore(Store)
     */
    public void setStore(Store store) throws XBRLException {
        if (store == null) throw new XBRLException("The data store must not be null.");
        this.store = store;
    }

}
