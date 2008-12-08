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
     * @see DimensionValueAccessor#getValue(org.xbrlapi.Item, org.xbrlapi.xdt.Dimension)
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
     * @see DimensionValueAccessor#getTypedDimensionValue(Item, Dimension)
     */
    public DimensionValue getTypedDimensionValue(Item item, Dimension dimension) throws XBRLException {

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
     * @see DimensionValueAccessor#getExplicitDimensionValue(Item, Dimension)
     */
    public DimensionValue getExplicitDimensionValue(Item item, Dimension dimension) throws XBRLException {
        
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
     * @see DimensionValueAccessor#getDomainMemberFromOpenContextComponent(OpenContextComponent, Dimension)
     */
    public Concept getDomainMemberFromOpenContextComponent(OpenContextComponent occ, Dimension dimension) throws XBRLException {
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
     * @see DimensionValueAccessor#getTypedDimensionContentFromOpenContextComponent(OpenContextComponent, Dimension)
     */
    public Element getTypedDimensionContentFromOpenContextComponent(OpenContextComponent occ,Dimension dimension) throws XBRLException {
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
