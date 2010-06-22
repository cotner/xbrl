package org.xbrlapi.xdt.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.utilities.XBRLException;

public class SegmentRemainderDomain implements Domain<SegmentRemainderAspectValue> {

    /**
     * 
     */
    private static final long serialVersionUID = 2844552022220878846L;

    /**
     * @see Domain#getAllAspectValues()
     */
    public List<SegmentRemainderAspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<SegmentRemainderAspectValue> getChildren(SegmentRemainderAspectValue parent)
            throws XBRLException {
        return new Vector<SegmentRemainderAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(SegmentRemainderAspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public SegmentRemainderAspectValue getParent(SegmentRemainderAspectValue child)
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
    public boolean hasChildren(SegmentRemainderAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(SegmentRemainderAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(SegmentRemainderAspectValue candidate)
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
