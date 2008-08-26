package org.xbrlapi.xdt;

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

        Scenario scenario = context.getScenario();
        Concept dimensionValue = this.getDomainMemberFromOpenContextComponent(scenario, namespace, localname);
        if (dimensionValue != null) {
            return new DimensionValueImpl(dimensionValue);
        }
        
        Segment segment = context.getEntity().getSegment();
        dimensionValue = this.getDomainMemberFromOpenContextComponent(segment, namespace, localname);
        if (dimensionValue != null) {
            return new DimensionValueImpl(dimensionValue);
        }
        
        ExplicitDimension dimension = (ExplicitDimension) ((XBRLStore) item.getStore()).getConcept(namespace,localname);
        if (dimension.hasDefaultDomainMember()) {
            // TODO make sure that the item can report the default domain member.
            return new DimensionValueImpl(dimension.getDefaultDomainMember());
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
     * @see org.xbrlapi.xdt.DimensionValueAccessor#hasExplicitValue(org.xbrlapi.Item, java.lang.String, java.lang.String)
     */
    public boolean hasExplicitValue(Item item, String namespace,
            String localname) throws XBRLException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.xbrlapi.xdt.DimensionValueAccessor#hasValue(org.xbrlapi.Item, java.lang.String, java.lang.String)
     */
    public boolean hasValue(Item item, String namespace, String localname)
            throws XBRLException {
        // TODO Auto-generated method stub
        return false;
    }

}
