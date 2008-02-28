package org.xbrlapi.impl;

import java.util.LinkedList;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.SchemaDeclaration;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaDeclarationImpl extends SchemaContentImpl implements SchemaDeclaration {
	
    /**
     * Get the name of structure being declared.
     * @return the name of the structure being declared.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getName()
     */
    public String getName() throws XBRLException {
    	return getDataRootElement().getAttribute("name");
    }
    
    /**
     * Set the name of the concept.
     * @param name The name of the concept.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#setName(String)
     */
    public void setName(String name) throws XBRLException {
    	getDataRootElement().setAttribute("name",name);
    }

    /**
     * Determine if a concept is abstract.
     * @return true if the concept is abstract and false otherwise.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#isAbstract()
     */
    public boolean isAbstract() throws XBRLException {
    	if (getDataRootElement().getAttribute("abstract").equals("true")) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Sets the abstract attribute for the concept.
     * @param abstractProperty The abstract attribute value.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#setAbstract(boolean)
     */
    public void setAbstract(boolean abstractProperty) throws XBRLException {
		if (abstractProperty) {
			getDataRootElement().setAttribute("abstract","true");
		} else {
			getDataRootElement().setAttribute("abstract","false");
		}
    }


    /**
     * Get the block attribute value.
     * One of #all or a list of extension, restriction, substitution.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getBlock()
     */
    public String getBlock() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XMLSchemaNamespace,"block");
    }
    
    /**
     * Sets the block attribute for the concept
     * One of #all or a list of extension, restriction, substitution.
     * @param block The block attribute value
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#setBlock(String)
     */
    public void setBlock(String block) throws XBRLException {
    	getDataRootElement().setAttributeNS(Constants.XMLSchemaNamespace,Constants.XMLSchemaPrefix + ":block",block);
    }


    


    /**
     * Get the final attribute value.
     * One of #all or list of extension and restriction.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getFinal()
     */
    public String getFinal() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XMLSchemaNamespace,"final");
    }
    
    /**
     * Sets the default attribute for the concept.
     * One of #all or list of extension and restriction.
     * @param value The final attribute value
     * @throws XBRLException
     *  @see org.xbrlapi.SchemaDeclaration#setFinal(String)
     */
    public void setFinal(String value) throws XBRLException {
    	getDataRootElement().setAttributeNS(Constants.XMLSchemaNamespace,Constants.XMLSchemaPrefix + ":final",value);
    }

    /**
     * Get the id attribute value.
     * @throws XBRLException
     *  @see org.xbrlapi.SchemaDeclaration#getSchemaDeclarationId()
     */
    public String getSchemaDeclarationId() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XMLSchemaNamespace,"id");
    }
    
    /**
     * Sets the id attribute for the concept.
     * @param id The id attribute value
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#setSchemaDeclarationId(String)
     */
    public void setSchemaDeclarationId(String id) throws XBRLException {
    	getDataRootElement().setAttributeNS(Constants.XMLSchemaNamespace,Constants.XMLSchemaPrefix + ":id",id);
    }
    
    /**
     * Get the collection of other non-schema non-xbrl attributes.
     * @return a linked list of other attributes
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getOtherAttributes()
     */
    public LinkedList<Node> getOtherAttributes() throws XBRLException {
    	NamedNodeMap attributes = getDataRootElement().getAttributes();
    	LinkedList<Node> otherAttributes = new LinkedList<Node>();
    	for (int i=0; i<attributes.getLength(); i++) {
    		String ns = attributes.item(i).getNamespaceURI();
    		if (! ns.equals(Constants.XMLSchemaNamespace) && ! ns.equals(Constants.XBRL21Namespace)) {
    			otherAttributes.add(attributes.item(i));
    		}
    	}
    	return otherAttributes;
    }
    
    /**
     * Set a non-schema non-xbrl attribute value.
     * Mirrors the setAttributeNS method of the org.w3c.dom.Element class.
     * @param namespaceURI
     * @param qualifiedName
     * @param value
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#setOtherAttribute(String, String, String)
     */
    public void setOtherAttribute(String namespaceURI, String qualifiedName, String value) throws XBRLException {
    	getDataRootElement().setAttributeNS(namespaceURI,qualifiedName,value);
    }

    /**
     * Set a non-schema non-xbrl attribute value.
     * Mirrors the setAttributeNode method of the org.w3c.dom.Element class.
     * @param attribute
     * @throws XBRLException
     * @see org.w3c.dom.Element#setAttributeNode(Attr)
     * @see org.xbrlapi.SchemaDeclaration#setOtherAttribute(Attr)
     */
    public void setOtherAttribute(Attr attribute) throws XBRLException {
    	getDataRootElement().appendChild(attribute);
    }    
    
    /**
     * Determines if concept has a non-schema non-xbrl attribute value.
     * Mirrors the hasAttributeNS method of the org.w3c.dom.Element class.
     * @param namespaceURI The namespace of the attribute being tested for
     * @param localname The local name of the attribute being tested for
     * @return true if the attribute exists
     * @throws XBRLException
     * @see org.w3c.dom.Element#hasAttributeNS(String, String)
     * @see org.xbrlapi.SchemaDeclaration#setOtherAttribute(Attr)
     */
    public boolean hasOtherAttribute(String namespaceURI, String localname) throws XBRLException {
    	if (getDataRootElement().hasAttributeNS(namespaceURI, localname)) {
    		return true;
    	}
    	return false;
    }

    /**
     * Removes a non-schema non-xbrl attribute value.
     * Mirrors the removeAttributeNS method of the org.w3c.dom.Element class.
     * @param namespaceURI The namespace of the attribute to be removed
     * @param localname The local name of the attribute to be removed
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#removeOtherAttribute(String, String)
     */
    public void removeOtherAttribute(String namespaceURI, String localname) throws XBRLException {
    	getDataRootElement().removeAttributeNS(namespaceURI,localname);
    }

    /**
     * Removes a non-schema non-xbrl attribute value.
     * Mirrors the removeAttributeNode method of the org.w3c.dom.Element class.
     * @param attribute The attribute to be removed.
     * @throws XBRLException
     * @see org.w3c.dom.Element#removeAttributeNode(Attr)
     * @see org.xbrlapi.SchemaDeclaration#removeOtherAttribute(Attr)
     */
    public void removeOtherAttribute(Attr attribute) throws XBRLException {
    	getDataRootElement().removeAttributeNode(attribute);
    }
    
    /**
     * TODO Implement the getAnnotations Method for schema declarations.
     * Retrieves an array of annotation objects associated with the concept
     * The array is in document order for the XML Schema document containing
     * the concept definition.
     * Returns null if the annotations do not exist.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getAnnotations()
     */
    public FragmentList<Fragment> getAnnotations() throws XBRLException {
    	throw new XBRLException("The getAnnotations method is not yet implemented.");
    }
    
    /**
     * Gets the complex content fragment.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getComplexContent()
     */
    public Element getComplexContent() throws XBRLException {
    	try {
	    	NodeList complexContent = getDataRootElement().getElementsByTagNameNS(Constants.XMLSchemaNamespace,"complexContent");
	    	return (Element) complexContent.item(0);

    	} catch (Exception e) {
    		throw new XBRLException("The complex content could not be retrieved for the specified concept.");
    	}
    }

    /**
     * @see org.xbrlapi.SchemaDeclaration#setComplexContent(Element)
     */
    public void setComplexContent(Element complexContent) throws XBRLException {
    	if (getDataRootElement().getElementsByTagNameNS(Constants.XMLSchemaNamespace,"complexContent").getLength() > 0) {
    		throw new XBRLException("The element already has complexContent");
    	}
    	getDataRootElement().appendChild(complexContent);
    }

}