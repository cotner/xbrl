package org.xbrlapi.aspects.alt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Generates semantic IDs based on element content of
 * segment/scenario/typed dimension structures.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class IDGenerator {

    /**
     * Convenience method to enable a list of elements and text nodes 
     * to be converted into something a bit more readable.
     * @param children The list of elements and text nodes.
     * @return the label.
     */
    public static String getLabelFromMixedNodes(List<Node> children) {
        String label = "{";
        if (children.size() == 0) return "none";
        for (int i=0; i<children.size(); i++) {
            if (i>0) {
                label += " ";
            }
            Node child = children.get(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
                label += IDGenerator.getLabel((Element) child);
            else 
                label += IDGenerator.getLabel((Text) child);
            
        }
        label += "}";
        return label;
    }        
    
    /**
     * Convenience method to enable a list of elements
     * to be converted into something a bit more readable.
     * @param children The list of elements.
     * @return the label.
     */
    public static String getLabel(List<Element> children) {
        String label = "";
        if (children.size() == 0) return "none";
        for (int i=0; i<children.size(); i++) {
            if (i>0) {
                label += " ";
            }
            label += IDGenerator.getLabel(children.get(i));
        }
        return label;
    }    
    
    /**
     * Convenience method to return a unique representation of a single element.
     * TODO change so that this will treat QName attribute and element content as QNames and not simple text.
     * 
     * @param child
     *            The single element.
     * @return the unique string representation.
     */
    public static String getLabel(Element child) {
        String localname = child.getLocalName();
        String namespace = child.getNamespaceURI();
        
        // Child elements and text nodes, in document order.
        List<Node> children = new Vector<Node>();
        Map<String,Attr> attributes = new HashMap<String,Attr>();
        NodeList nodes = child.getChildNodes();
        for (int i = 0; i< nodes.getLength(); i++) {
            Node node = nodes.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.TEXT_NODE) children.add(node);
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                Attr attribute = (Attr) node;
                attributes.put(attribute.getNamespaceURI() + "#" + attribute.getLocalName(), attribute);
            }
        }
        
        // Start the element representation with its name.
        String result = "{" + namespace + "#" + localname + "(";
        // Add in the attributes sorted by namespace then name.
        List<String> attributeNames = new Vector<String>();
        attributeNames.addAll(attributes.keySet());
        Collections.sort(attributeNames);
        for (String name: attributeNames) {
            Attr attr = attributes.get(name);
            if (name != attributeNames.get(0)) result += ",";
            result += name + "=" + attr.getValue();
        }
        result += ")";

        for (Node node: children) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                result += getLabel((Element) node);
            } else {
                result += ((Text) node).getTextContent();
            }
        }
        
        result += "}";

        return result;
    }    

    /**
     * Convenience method to return a unique representation of a single element.
     * TODO change so that this will treat QName attribute and element content as QNames and not simple text.
     * 
     * @param child
     *            The single text node.
     * @return the unique string representation.
     */
    public static String getLabel(Text child) {
        return child.getTextContent();
    }    
    
    
}
