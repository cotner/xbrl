package org.xbrlapi.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.utilities.XBRLException;

public class SegmentDomain implements Domain<SegmentAspectValue> {

    /**
     * 
     */
    private static final long serialVersionUID = -3587267449887282318L;

    /**
     * @see Domain#getAllAspectValues()
     */
    public List<SegmentAspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<SegmentAspectValue> getChildren(SegmentAspectValue parent)
            throws XBRLException {
        return new Vector<SegmentAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(SegmentAspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public SegmentAspectValue getParent(SegmentAspectValue child)
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
    public boolean hasChildren(SegmentAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(SegmentAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(SegmentAspectValue candidate)
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
