package org.xbrlapi.xdt;

import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

public class DimensionValueImpl implements DimensionValue {

    /**
     * The item with the value for the dimension.
     */
    private Item item = null;

    /**
     * The dimension with the value.
     */
    private Dimension dimension = null;

    /**
     * The explicit dimension value.
     */
    private Concept member = null;
    
    /**
     * The typed dimension value.
     */
    private Element value = null;
    
    /**
     * Construct a dimension value.
     * @param item The item that has the dimension value.
     * @param dimension The dimension with the value.
     * @param value The dimension value object (that must be one of
     * an org.xbrlapi.Concept or a org.w3c.dom.Element) where the 
     * class of the object matches the type of dimension.
     * @throws XBRLException.
     */
    public DimensionValueImpl(Item item, Dimension dimension, Object value) throws XBRLException {
        setItem(item);
        setDimension(dimension);
        setValue(value);
    }
    
    private void setItem(Item i) throws XBRLException {
        if (i == null) throw new XBRLException("The item must not be null.");
        item = i;
    }
    
    private void setDimension(Dimension d) throws XBRLException {
        if (d == null) throw new XBRLException("The dimension must not be null.");
        dimension = d;
    }

    private void setValue(Object o) throws XBRLException {
        if (o == null) throw new XBRLException("The dimension value must not be null.");
        try {
            member = org.xbrlapi.Concept.class.cast(o);
            if (isTypedDimensionValue()) throw new XBRLException("The dimension type and value type conflict.");
        } catch (ClassCastException e) {
            try {
                value = org.w3c.dom.Element.class.cast(o);
                if (isExplicitDimensionValue()) throw new XBRLException("The dimension type and value type conflict.");
            } catch (ClassCastException ee) {
                throw new XBRLException("The dimension value is not a domain member or a typed dimension value root element.",e);
            }
        }
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValue#getItem()
     */
    public Item getItem() throws XBRLException {
        if (item == null) throw new XBRLException("The item is null.");
        return item;
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValue#getDimension()
     */
    public Dimension getDimension() throws XBRLException {
        if (dimension == null) throw new XBRLException("The dimension is null.");
        return dimension;
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValue#getValue()
     */
    public Object getValue() throws XBRLException {
        if (isExplicitDimensionValue()) {
            if (member == null) throw new XBRLException("The explicit dimension value is null.");
            return member;
        }
        if (isTypedDimensionValue()) {
            if (value == null) throw new XBRLException("The typed dimension value is null.");
            return value;
        }
        throw new XBRLException("The type of dimension could not be determined so no value is available.");
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValue#isExplicitDimensionValue()
     */
    public boolean isExplicitDimensionValue() throws XBRLException {
        return (!isTypedDimensionValue());
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValue#isTypedDimensionValue()
     */
    public boolean isTypedDimensionValue() throws XBRLException {
        if (getDimension().getType().equals("org.xbrlapi.xdt.TypedDimensionImpl")) return true;
        return false;
    }
    
}
