package org.xbrlapi.xdt.values;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Concept;
import org.xbrlapi.Context;
import org.xbrlapi.Item;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.Scenario;
import org.xbrlapi.Segment;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.XDTConstants;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DimensionValueAccessorImpl implements DimensionValueAccessor {

    protected static Logger logger = Logger.getLogger(DimensionValueAccessorImpl.class);        
    
    /**
     * Create a dimension value accessor.
     */
    public DimensionValueAccessorImpl() {
        ;
    }

    /**
     * @see org.xbrlapi.xdt.values.DimensionValueAccessor#getValue(org.xbrlapi.Item, org.xbrlapi.xdt.Dimension)
     */
    public DimensionValue getValue(Item item, Dimension dimension) throws XBRLException {
        if (dimension.getType().equals("org.xbrlapi.xdt.TypedDimensionImpl")) {
            return getTypedDimensionValue(item, dimension);
        } else if (dimension.getType().equals("org.xbrlapi.xdt.ExplicitDimensionImpl")) {
            return getExplicitDimensionValue(item, dimension);
        }
        throw new XBRLException("The dimension QName does not identify a typed or explicit dimension.");
    }    

    /**
     * @param item The fact to get the typed dimension value for.
     * @param dimension The dimension to get the value for.
     * @return the element containing the typed dimension value.  This is a child element of the segment
     * or scenario that contains the typed dimension value.
     * @throws XBRLException
     */
    private DimensionValue getTypedDimensionValue(Item item, Dimension dimension) throws XBRLException {

        Context context = item.getContext();

        Scenario scenario = context.getScenario();
        Element typedDimensionValue = this.getTypedDimensionContentFromOpenContextComponent(scenario, dimension);
        if (typedDimensionValue != null) {
            return new DimensionValueImpl(item, dimension, scenario, typedDimensionValue);
        }
        
        Segment segment = context.getEntity().getSegment();
        typedDimensionValue = this.getTypedDimensionContentFromOpenContextComponent(segment, dimension);
        if (typedDimensionValue != null) {
            return new DimensionValueImpl(item, dimension, segment, typedDimensionValue);
        }
        
        return null;
    }
    
    /**
     * @param item The fact to get the explicit dimension value for.
     * @param dimension The dimension to get the value for.
     * @return the value of the explicit dimension or null if there is none.
     * @throws XBRLException
     */
    private DimensionValue getExplicitDimensionValue(Item item, Dimension dimension) throws XBRLException {
        
        Context context = item.getContext();

        Scenario scenario = context.getScenario();
        Concept dimensionValue = this.getDomainMemberFromOpenContextComponent(scenario, dimension);
        if (dimensionValue != null) {
            return new DimensionValueImpl(item, dimension, scenario, dimensionValue);
        }
        
        Segment segment = context.getEntity().getSegment();
        dimensionValue = this.getDomainMemberFromOpenContextComponent(segment, dimension);
        if (dimensionValue != null) {
            return new DimensionValueImpl(item, dimension, segment, dimensionValue);
        }
        
        try {
            Concept def = ((ExplicitDimension) dimension).getDefaultDomainMember();
            if (def != null) {
                return new DimensionValueImpl(item, dimension, null, def);
            }
        } catch (XBRLException e) {
            return null;
        }
        return null;
        
    }
    
    /**
     * @param occ The open context component fragment (segment or scenario) to get the dimension value from
     * @param dimension The dimension
     * @return The domain member that is the dimension value for the item.
     * @throws XBRLException
     */
    private Concept getDomainMemberFromOpenContextComponent(OpenContextComponent occ, Dimension dimension) throws XBRLException {
        if (occ != null) {
            String namespace = dimension.getTargetNamespaceURI();
            String localname = dimension.getName();
            NodeList children = occ.getDataRootElement().getChildNodes();
            for (int i=0; i< children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) child;
                    if (childElement.getNamespaceURI().equals(XDTConstants.XBRLDINamespace)) {
                        String dimensionName = childElement.getAttribute("dimension");
                        if (! dimensionName.equals("")) {
                            String dimensionNamespace = occ.getNamespaceFromQName(dimensionName,child);
                            String dimensionLocalname = occ.getLocalnameFromQName(dimensionName);
                            if (dimensionNamespace.equals(namespace) && dimensionLocalname.equals(localname)) {                                
                                String memberName = childElement.getTextContent().trim();
                                String memberNamespace = occ.getNamespaceFromQName(memberName,child);
                                String memberLocalname = occ.getLocalnameFromQName(memberName);
                                return ((XBRLStore) occ.getStore()).getConcept(memberNamespace,memberLocalname);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * @param occ The open context component fragment (segment or scenario) to get the dimension value from
     * @param dimension The dimension
     * @return The child element of the OCC that contains the explicit dimension value or null if there is none.
     * @throws XBRLException
     */
    private Element getTypedDimensionContentFromOpenContextComponent(OpenContextComponent occ,Dimension dimension) throws XBRLException {
        if (occ != null) {
            String namespace = dimension.getTargetNamespaceURI();
            String localname = dimension.getName();
            NodeList children = occ.getDataRootElement().getChildNodes();
            for (int i=0; i< children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) child;
                    if (childElement.getNamespaceURI().equals(XDTConstants.XBRLDINamespace)) {
                        String dimensionName = childElement.getAttribute("dimension");
                        if (! dimensionName.equals("")) {
                            String dimensionNamespace = occ.getNamespaceFromQName(dimensionName,child);
                            String dimensionLocalname = occ.getLocalnameFromQName(dimensionName);
                            if (dimensionNamespace.equals(namespace) && dimensionLocalname.equals(localname)) {
                                return childElement;
                            }                                
                            
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @see org.xbrlapi.xdt.values.DimensionValueAccessor#equalValues(org.xbrlapi.Item, org.xbrlapi.Item, org.xbrlapi.xdt.Dimension)
     */
    public boolean equalValues(Item first, Item second, Dimension dimension) throws XBRLException {
        DimensionValue firstValue = this.getValue(first,dimension);
        if (firstValue == null) return false;
        DimensionValue secondValue = this.getValue(second,dimension);
        if (secondValue == null) return false;
        // TODO verify this comparison.
        return firstValue.equals(secondValue);
    }

    
    
}
