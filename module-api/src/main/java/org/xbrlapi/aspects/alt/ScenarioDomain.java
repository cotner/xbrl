package org.xbrlapi.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.utilities.XBRLException;

public class ScenarioDomain implements Domain<ScenarioAspectValue> {

    /**
     * 
     */
    private static final long serialVersionUID = -8165623420918270003L;

    /**
     * @see Domain#getAllAspectValues()
     */
    public List<ScenarioAspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<ScenarioAspectValue> getChildren(ScenarioAspectValue parent)
            throws XBRLException {
        return new Vector<ScenarioAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(ScenarioAspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public ScenarioAspectValue getParent(ScenarioAspectValue child)
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
    public boolean hasChildren(ScenarioAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(ScenarioAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(ScenarioAspectValue candidate)
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
