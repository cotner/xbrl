package org.xbrlapi.xdt.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.utilities.XBRLException;

public class ScenarioRemainderDomain implements Domain<ScenarioRemainderAspectValue> {

    /**
     * 
     */
    private static final long serialVersionUID = -2458464836980529306L;

    /**
     * @see Domain#getAllAspectValues()
     */
    public List<ScenarioRemainderAspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<ScenarioRemainderAspectValue> getChildren(ScenarioRemainderAspectValue parent)
            throws XBRLException {
        return new Vector<ScenarioRemainderAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(ScenarioRemainderAspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public ScenarioRemainderAspectValue getParent(ScenarioRemainderAspectValue child)
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
    public boolean hasChildren(ScenarioRemainderAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(ScenarioRemainderAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(ScenarioRemainderAspectValue candidate)
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
