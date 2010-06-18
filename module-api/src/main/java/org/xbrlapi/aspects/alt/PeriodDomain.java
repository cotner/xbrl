package org.xbrlapi.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.utilities.XBRLException;

public class PeriodDomain implements Domain<PeriodAspectValue> {

    /**
     * 
     */
    private static final long serialVersionUID = 4798320671196789758L;

    /**
     * @see Domain#getAllAspectValues()
     */
    public List<PeriodAspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<PeriodAspectValue> getChildren(PeriodAspectValue parent)
            throws XBRLException {
        return new Vector<PeriodAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(PeriodAspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public PeriodAspectValue getParent(PeriodAspectValue child)
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
    public boolean hasChildren(PeriodAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(PeriodAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(PeriodAspectValue candidate)
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
