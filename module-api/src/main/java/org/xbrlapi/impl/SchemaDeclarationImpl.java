package org.xbrlapi.impl;

import java.util.LinkedList;

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
     * Get the block attribute value.
     * One of #all or a list of extension, restriction, substitution.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getBlock()
     */
    public String getBlock() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XMLSchemaNamespace,"block");
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
     * Get the id attribute value.
     * @throws XBRLException
     *  @see org.xbrlapi.SchemaDeclaration#getSchemaDeclarationId()
     */
    public String getSchemaDeclarationId() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XMLSchemaNamespace,"id");
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
     * Determines if concept has a non-schema non-xbrl attribute value.
     * Mirrors the hasAttributeNS method of the org.w3c.dom.Element class.
     * @param namespaceURI The namespace of the attribute being tested for
     * @param localname The local name of the attribute being tested for
     * @return true if the attribute exists
     * @throws XBRLException
     * @see org.w3c.dom.Element#hasAttributeNS(String, String)
     * @see org.xbrlapi.SchemaDeclaration#hasOtherAttribute(String,String)
     */
    public boolean hasOtherAttribute(String namespaceURI, String localname) throws XBRLException {
    	if (getDataRootElement().hasAttributeNS(namespaceURI, localname)) {
    		return true;
    	}
    	return false;
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

}