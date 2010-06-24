package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.utilities.XBRLException;

public class ScenarioRemainderDomain implements Domain {

    /**
     * 
     */
    private static final long serialVersionUID = -2458464836980529306L;

    public URI getAspectId() { return ScenarioRemainderAspect.ID; }
    
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
    public ScenarioRemainderAspectValue getParent(AspectValue child)
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
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(AspectValue candidate)
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
