/**
 * 
 */
package org.xbrlapi.xdt;

import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author geoff
 *
 */
public class DimensionValueImpl implements DimensionValue {

    private Element typedDimensionValue = null;
    private Concept explicitDimensionValue = null;

    /**
     * Construct a typed dimension value.
     * @param typedDimensionValue The value of the typed dimension.
     * @throws XBRLException if the value is null.
     */
    public DimensionValueImpl(Element typedDimensionValue) throws XBRLException {
        if (typedDimensionValue == null) throw new XBRLException("The typed dimension value must not be null.");
        this.typedDimensionValue = typedDimensionValue;
    }

    /**
     * Construct an explicit dimension value.
     * @param explicitDimensionValue The value of the explicit dimension.
     * @throws XBRLException if the value is null.
     */
    public DimensionValueImpl(Concept explicitDimensionValue) throws XBRLException {
        if (explicitDimensionValue == null) throw new XBRLException("The explicit dimension value must not be null.");
        this.explicitDimensionValue = explicitDimensionValue;
    }
    
    /**
     * @see org.xbrlapi.xdt.DimensionValue#getExplicitDimensionValue()
     */
    public Concept getExplicitDimensionValue() throws XBRLException {
        if (isExplicitDimension()) {
            return explicitDimensionValue;
        }
        throw new XBRLException("The dimension is typed, not explicit.");
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValue#getTypedDimensionValue()
     */
    public Element getTypedDimensionValue() throws XBRLException {
        if (isTypedDimension()) {
            return typedDimensionValue;
        }
        throw new XBRLException("The dimension is explicit, not typed.");
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValue#isExplicitDimension()
     */
    public boolean isExplicitDimension() throws XBRLException {
        if (explicitDimensionValue != null) return true;
        return false;
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValue#isTypedDimension()
     */
    public boolean isTypedDimension() throws XBRLException {
        if (typedDimensionValue != null) return true;
        return false;
    }

}
