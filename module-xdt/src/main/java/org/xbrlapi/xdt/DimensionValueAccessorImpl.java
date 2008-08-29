package org.xbrlapi.xdt;

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
     * @see org.xbrlapi.xdt.DimensionValueAccessor#getValue(org.xbrlapi.Item, java.lang.String, java.lang.String)
     */
    public DimensionValue getValue(Item item, String namespace, String localname) throws XBRLException {
        Concept concept = ((XBRLStore) item.getStore()).getConcept(namespace,localname);
        if (concept.getType().equals("org.xbrlapi.xdt.TypedDimensionImpl")) {
            return getTypedDimensionValue(item, namespace,localname);
        } else if (concept.getType().equals("org.xbrlapi.xdt.ExplicitDimensionImpl")) {
            return getExplicitDimensionValue(item, namespace,localname);
        }
        throw new XBRLException("The dimension QName does not identify a typed or explicit dimension.");
    }    

    /**
     * @param item The fact to get the typed dimension value for.
     * @param namespace The namespace of the dimension.
     * @param localname The local name of the dimension.
     * @return the element containing the typed dimension value.  This is a child element of the segment
     * or scenario that contains the typed dimension value.
     * @throws XBRLException
     */
    private DimensionValue getTypedDimensionValue(Item item, String namespace, String localname) throws XBRLException {

        Context context = item.getContext();

        Scenario scenario = context.getScenario();
        Element typedDimensionValue = this.getTypedDimensionContentFromOpenContextComponent(scenario, namespace, localname);
        if (typedDimensionValue != null) {
            return new DimensionValueImpl(typedDimensionValue);
        }
        
        Segment segment = context.getEntity().getSegment();
        typedDimensionValue = this.getTypedDimensionContentFromOpenContextComponent(segment, namespace, localname);
        if (typedDimensionValue != null) {
            return new DimensionValueImpl(typedDimensionValue);
        }
        
        return null;
    }
    
    /**
     * @param item The fact to get the explicit dimension value for.
     * @param namespace The namespace of the dimension.
     * @param localname The local name of the dimension.
     * @return the value of the explicit dimension or null if there is none.
     * @throws XBRLException
     */
    private DimensionValue getExplicitDimensionValue(Item item, String namespace, String localname) throws XBRLException {
        
        Context context = item.getContext();

        Concept dimensionValue = this.getDomainMemberFromOpenContextComponent(context.getScenario(), namespace, localname);
        if (dimensionValue != null) {
            return new DimensionValueImpl(dimensionValue);
        }
        
        dimensionValue = this.getDomainMemberFromOpenContextComponent(context.getEntity().getSegment(), namespace, localname);
        if (dimensionValue != null) {
            return new DimensionValueImpl(dimensionValue);
        }
        
        ExplicitDimension dimension = (ExplicitDimension) ((XBRLStore) item.getStore()).getConcept(namespace,localname);
        try {
            Concept def = dimension.getDefaultDomainMember();
            if (def != null) {
                return new DimensionValueImpl(def);
            }
        } catch (XBRLException e) {
            return null;
        }
        return null;
        
    }
    
    /**
     * @param occ The open context component fragment (segment or scenario) to get the dimension value from
     * @param namespace The namespace of the dimension
     * @param localname The local name of the dimension
     * @return The domain member that is the dimension value for the item.
     * @throws XBRLException
     */
    private Concept getDomainMemberFromOpenContextComponent(OpenContextComponent occ,String namespace, String localname) throws XBRLException {
        if (occ != null) {
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
     * @param namespace The namespace of the dimension
     * @param localname The local name of the dimension
     * @return The child element of the OCC that contains the explicit dimension value or null if there is none.
     * @throws XBRLException
     */
    private Element getTypedDimensionContentFromOpenContextComponent(OpenContextComponent occ,String namespace, String localname) throws XBRLException {
        if (occ != null) {
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
     * @see org.xbrlapi.xdt.DimensionValueAccessor#equalValues(org.xbrlapi.Item, org.xbrlapi.Item, java.lang.String, java.lang.String)
     */
    public boolean equalValues(Item first, Item second, String namespace, String localname) throws XBRLException {
        DimensionValue firstValue = this.getValue(first,namespace,localname);
        if (firstValue == null) return false;
        DimensionValue secondValue = this.getValue(second,namespace,localname);
        if (secondValue == null) return false;
        return firstValue.equals(secondValue);
    }

    
    
}
