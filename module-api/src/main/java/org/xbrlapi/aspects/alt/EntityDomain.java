package org.xbrlapi.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.utilities.XBRLException;

public class EntityDomain implements Domain<EntityAspectValue> {

    /**
     * 
     */
    private static final long serialVersionUID = -1180707610130423730L;

    /**
     * @see Domain#getAllAspectValues()
     */
    public List<EntityAspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<EntityAspectValue> getChildren(EntityAspectValue parent)
            throws XBRLException {
        return new Vector<EntityAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(EntityAspectValue aspectValue) throws XBRLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public EntityAspectValue getParent(EntityAspectValue child)
            throws XBRLException {
        return null;
    }

    /**
     * @see Domain#getSize()
     */
    public long getSize() throws XBRLException {
        throw new XBRLException("The domain is not finite");
    }

    /**
     * @see Domain#hasChildren(AspectValue)
     */
    public boolean hasChildren(EntityAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(EntityAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(EntityAspectValue candidate)
            throws XBRLException {
        return true;
    }

    /**
     * @return false always.
     * @see Domain#isFinite()
     */
    public boolean isFinite() {
        return false;
    }

    /**
     * Returns true to allow for tuples and nil facts.
     * @see Domain#allowsMissingValues()
     */
    public boolean allowsMissingValues() {
        return true;
    }

}
