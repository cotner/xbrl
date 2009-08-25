package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Schema;
import org.xbrlapi.SchemaContent;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaContentImpl extends FragmentImpl implements SchemaContent {

    /**
     * @see org.xbrlapi.SchemaContent#getSchema()
     */
    public Schema getSchema() throws XBRLException {
    	return (Schema) getAncestorOrSelf("org.xbrlapi.impl.SchemaImpl");
    }
    
    /**
     * @see org.xbrlapi.SchemaContent#getTargetNamespace()
     */
    public URI getTargetNamespace() throws XBRLException {
    	Schema s = getSchema();
    	Element e = s.getDataRootElement();
    	if (e.hasAttribute("targetNamespace")) {
    		try {
    		    return new URI(e.getAttribute("targetNamespace"));
    		} catch (URISyntaxException exception) {
    		    throw new XBRLException("The target namespace is not a valid URI.",exception);
    		}
    	}
    	return null;
    }
    
    /**
     * @see org.xbrlapi.SchemaContent#getAnnotations()
     */
    public List<Element> getAnnotations() throws XBRLException {
        List<Element> result = new Vector<Element>();
        NodeList nodes = this.getDataRootElement().getElementsByTagNameNS(Constants.XMLSchemaNamespace.toString(),"annotation");
        for (int i=0; i<nodes.getLength(); i++) {
            result.add((Element) nodes.item(i));
        }
        return result;
    }    

    /**
     * @see org.w3c.dom.Element#hasAttributeNS(String, String)
     * @see org.xbrlapi.SchemaContent#hasOtherAttribute(String,String)
     */
    public boolean hasOtherAttribute(URI namespace, String localname) throws XBRLException {
        return getDataRootElement().hasAttributeNS(namespace.toString(), localname);
    }
    
    /**
     * @see org.xbrlapi.SchemaContent#getOtherAttributes()
     */
    public LinkedList<Node> getOtherAttributes() throws XBRLException {
        NamedNodeMap attributes = getDataRootElement().getAttributes();
        LinkedList<Node> otherAttributes = new LinkedList<Node>();
        for (int i=0; i<attributes.getLength(); i++) {
            String ns = attributes.item(i).getNamespaceURI();
            if (! ns.equals(Constants.XMLSchemaNamespace.toString()) && ! ns.equals(Constants.XBRL21Namespace.toString())) {
                otherAttributes.add(attributes.item(i));
            }
        }
        return otherAttributes;
    }    
    
    /**
     * @see org.w3c.dom.Element#getAttributeNS(String, String)
     * @see org.xbrlapi.SchemaContent#getOtherAttribute(String,String)
     */
    public String getOtherAttribute(URI namespace, String localname) throws XBRLException {
        if (this.hasOtherAttribute(namespace, localname)) 
            return getDataRootElement().getAttributeNS(namespace.toString(), localname);
        return null;
    }
    
    /**
     *  @see org.xbrlapi.SchemaDeclaration#getSchemaDeclarationId()
     */
    public String getSchemaId() throws XBRLException {
        if (! getDataRootElement().hasAttributeNS(Constants.XMLSchemaNamespace.toString(),"id")) return null;
        return getDataRootElement().getAttributeNS(Constants.XMLSchemaNamespace.toString(),"id");
    }    
    
}