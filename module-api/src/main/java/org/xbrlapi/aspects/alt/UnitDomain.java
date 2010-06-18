package org.xbrlapi.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.utilities.XBRLException;

public class UnitDomain implements Domain<UnitAspectValue> {

    /**
     * 
     */
    private static final long serialVersionUID = 6135757105592974444L;
    
    /**
     * @see Domain#getAllAspectValues()
     */
    public List<UnitAspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite.");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<UnitAspectValue> getChildren(UnitAspectValue parent)
            throws XBRLException {
        return new Vector<UnitAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(UnitAspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public UnitAspectValue getParent(UnitAspectValue child)
            throws XBRLException {
        return null;
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
    public boolean hasChildren(UnitAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(UnitAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(UnitAspectValue candidate)
            throws XBRLException {
        return true;
    }

    /**
     * @see Domain#isFinite()
     */
    public boolean isFinite() {
        return false;
    }

    /**
     * Returns true.
     * @see Domain#allowsMissingValues()
     */
    public boolean allowsMissingValues() {
        return true;
    }

}
