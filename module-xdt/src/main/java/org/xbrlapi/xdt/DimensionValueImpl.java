/**
 * 
 */
package org.xbrlapi.xdt;

import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Concept;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author geoff
 *
 */
public class DimensionValueImpl implements DimensionValue {

    private Element typedDimensionValue = null;
    private Item item = null;
    
    private Concept explicitDimensionValue = null;

    /**
     * Construct a typed dimension value.
     * @param item The item that has the typed dimension value.
     * @param typedDimensionValue The value of the typed dimension.
     * @throws XBRLException if the item or value is null.
     */
    public DimensionValueImpl(Item item, Element typedDimensionValue) throws XBRLException {
        if (item == null) throw new XBRLException("The item must not be null");
        this.item = item;
        if (typedDimensionValue == null) throw new XBRLException("The typed dimension value must not be null.");
        this.typedDimensionValue = typedDimensionValue;
    }

    /**
     * Construct an explicit dimension value.
     * @param item The item that has the typed dimension value.
     * @param explicitDimensionValue The value of the explicit dimension.
     * @throws XBRLException if the item or value is null.
     */
    public DimensionValueImpl(Item item, Concept explicitDimensionValue) throws XBRLException {
        if (item == null) throw new XBRLException("The item must not be null");
        this.item = item;
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

    /**
     * @see org.xbrlapi.xdt.DimensionValue#equals(org.xbrlapi.xdt.DimensionValue)
     */
    public boolean equals(DimensionValue other) throws XBRLException {

        if (this.isExplicitDimension() && other.isTypedDimension()) return false;
        
        if (other.isExplicitDimension() && this.isTypedDimension()) return false;
        
        if (this.isExplicitDimension()) {
            if (this.getExplicitDimensionValue().getFragmentIndex().equals(other.getExplicitDimensionValue().getFragmentIndex())) {
                return true;
            }
            return false;
        }
        
        if (this.isTypedDimension()) {
            
            List<Element> thisElements = this.getChildElementsOfTypedDimensionValue(this.getTypedDimensionValue());
            List<Element> otherElements = this.getChildElementsOfTypedDimensionValue(other.getTypedDimensionValue());

            if (thisElements.size() != otherElements.size()) return false;
            
            for (int i=0; i<thisElements.size(); i++) {
                Element thisElement = thisElements.get(i);
                Element otherElement = otherElements.get(i);
                // TODO Test that org.w3c.dom.Node#isEqualNode(Node) works for comparing nodes across DOM instances
                if (! thisElement.isEqualNode(otherElement)) {
                    return false;
                }
            }

            return true;
        }
        
        throw new XBRLException("The equality test failed because the dimension type could not be determined.");

    }
    
    private List<Element> getChildElementsOfTypedDimensionValue(Node value) throws XBRLException {
        NodeList nodes = this.getTypedDimensionValue().getChildNodes();
        List<Element> elements = new Vector<Element>();
        for (int i=0; i<nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) nodes.item(i));
            }
        }
        return elements;
    }

    
}
