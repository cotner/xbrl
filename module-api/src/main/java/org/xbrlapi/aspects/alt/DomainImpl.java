package org.xbrlapi.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public abstract class DomainImpl implements Domain, StoreHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -5823499517209750972L;
    
    protected final static Logger logger = Logger.getLogger(DomainImpl.class);

    /**
     * The underlying store.
     */
    private Store store;
    
    public DomainImpl(Store store) throws XBRLException {
        setStore(store);
    }
    
    /**
     * @see StoreHandler#getStore()
     */
    public Store getStore() throws XBRLException {
        return this.store;
    }
    
    /**
     * @param store the data store to set.
     * @throws XBRLException if the data store is null.
     */
    private void setStore(Store store) throws XBRLException {
        if (store == null) throw new XBRLException("The data store must not be null.");
        this.store = store;
    }
    
    /**
     * @see Domain#getAllAspectValues()
     */
    public List<AspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite");
    }
    
    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<AspectValue> getChildren(AspectValue parent)
            throws XBRLException {
        return new Vector<AspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(AspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public AspectValue getParent(AspectValue child)
            throws XBRLException {
        throw new XBRLException("Aspect value " + child.getId() + " does not have a parent aspect value");
    }

    /**
     * @see Domain#getSize()
     */
    public long getSize() throws XBRLException {
        throw new XBRLException("The domain is not finite.");
    }

    /**
     * @see Domain#hasChildren(AspectValue)
     */
    public boolean hasChildren(AspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(AspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isFinite()
     */
    public boolean isFinite() {
        return false;
    }
    
    /**
     * @see Domain#isRoot(AspectValue)
     */
    public boolean isRoot(AspectValue aspectValue) throws XBRLException {
        return true;
    }

}
