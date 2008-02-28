package org.xbrlapi.impl;


import org.w3c.dom.Element;
import org.xbrlapi.ElementDeclaration;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ElementDeclarationImpl extends SchemaDeclarationImpl implements ElementDeclaration {

    /**
     * Determine if a concept is nillable
     * @return true if the concept is nillable and false otherwise.
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#isNillable()
     */
    public boolean isNillable() throws XBRLException {
    	if (getDataRootElement().getAttribute("nillable").equals("true")) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Sets the nillable attribute for the concept.
     * @param nillable The nillable attribute value.
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#setNillable(boolean)
     */
    public void setNillable(boolean nillable) throws XBRLException {
    	throw new XBRLException("Data update methods are not implemented."); 
	}


    /**
     * Retrieve the type namespace.
     * @return the namespace for the data type.
     * @throws XBRLException if the datatype is not declared by a type attribute.
     * @see org.xbrlapi.ElementDeclaration#getTypeNamespace()
     */
    public String getTypeNamespace() throws XBRLException {
    	String type = getTypeQName();
    	if (type.equals("") || (type == null))
			throw new XBRLException("The element declaration does not declare its XML Schema data type via a type attribute.");
    	return this.getNamespaceFromQName(type, getDataRootElement());
    }
    
    /**
     * Retrieve the type namespace alias.
     * @return the namespace alias (prefix) or the empty string if there 
     * is no namespace prefix used in the QName.
     * @throws XBRLException if the datatype is not declared.
     * @see org.xbrlapi.ElementDeclaration#getTypeNamespaceAlias()
     */
    public String getTypeNamespaceAlias() throws XBRLException {
    	String type = getTypeQName();
    	if (type.equals("") || (type == null))
			throw new XBRLException("The element declaration does not declare its XML Schema data type via a type attribute.");    	
    	return getPrefixFromQName(type);
    }

    /**
     * Retrieve the type QName.
     * @return the QName used to specify the data type or null
     * if the element has no type attribute.
     * @throws XBRLException if the data root element is not available.
     * @see org.xbrlapi.ElementDeclaration#getTypeQName()
     */
    public String getTypeQName() throws XBRLException {
    	if (getDataRootElement().hasAttribute("type"))
    		return getDataRootElement().getAttribute("type");
    	else return null;
    }

    /**
     * Retrieve the type local name.
     * @return the local name for the datatype.
     * @throws XBRLException if the datatype is not declared.
     * @see org.xbrlapi.ElementDeclaration#getTypeLocalname()
     */  
    public String getTypeLocalname() throws XBRLException {
    	String type = getTypeQName();
    	if (type.equals("") || (type == null))
			throw new XBRLException("The element declaration does not declare its XML Schema data type via a type attribute.");
    	return getLocalnameFromQName(type);
    }

    /**
     * Set the datatype attribute on the element declaration.
     * TODO Decide if a third attribute is required to specify the namespace prefix on XML Schema datatypes.
     * @param namespaceURI The namespace in which the type has been defined
     * @param localname The localname of the type
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#setType(String,String)
     */
    public void setType(String namespaceURI, String localname) throws XBRLException {
    	throw new XBRLException("Data update methods are not implemented.");
    }

    /**
     * Retrieve the substitution group namespace.
     * @return the namespace for the element substitution group.
     * @throws XBRLException if the substitution group is not declared by a substitution group attribute.
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupNamespace()
     */
    public String getSubstitutionGroupNamespace() throws XBRLException {
    	String sg = getSubstitutionGroupQName();
    	if (sg.equals("") || (sg == null))
			throw new XBRLException("The element declaration does not declare its XML Schema substitution group via a substitutionGroup attribute.");   	
    	return this.getNamespaceFromQName(sg, getDataRootElement());
    }
    
    /**
     * Retrieve the substitution group namespace alias (also known as a namespace prefix).
     * @return the namespace alias for the element substitution group or the empty string if the default namespace prefix
     * is used.
     * @throws XBRLException if the substitution group is not declared by a substitution group attribute.
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupNamespaceAlias()
     */    
    public String getSubstitutionGroupNamespaceAlias() throws XBRLException {
    	String sg = getSubstitutionGroupQName();
    	if (sg.equals("") || (sg == null))
			throw new XBRLException("The element declaration does not declare its substitution group via a substitutionGroup attribute.");    	
    	return getPrefixFromQName(sg);
    }

    /**
     * Retrieve the substitution group QName.
     * @return the QName used to specify the substitution group or null
     * if the element has no substitutionGroup attribute.
     * @throws XBRLException if the data root element is not available.
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupQName()
     */  
    public String getSubstitutionGroupQName() throws XBRLException {
    	if (getDataRootElement().hasAttribute("substitutionGroup"))
    		return getDataRootElement().getAttribute("substitutionGroup");
    	else return null;
    }

    /**
     * Retrieve the substitution group local name.
     * @return the local name for the substitution group.
     * @throws XBRLException if the substitution group is not declared.
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupLocalname()
     */  
    public String getSubstitutionGroupLocalname() throws XBRLException {
    	String sg = getSubstitutionGroupQName();
    	if (sg.equals("") || (sg == null))
			throw new XBRLException("The element declaration does not declare its substitution group via a substitutionGroup attribute.");    	
    	return getLocalnameFromQName(sg);
    }

    /**
     * Set the substitution group
     * TODO Decide if a third attribute is required to specify the namespace prefix on XML Schema substitution groups.
     * @param namespaceURI The namespace in which the substitution group has been defined
     * @param localname The localname of the substitution group 
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#setSubstitutionGroup(String,String)
     */
    public void setSubstitutionGroup(String namespaceURI, String localname) throws XBRLException {
    	throw new XBRLException("Data update methods are not implemented.");    	
    }


    /**
     * Get the default attribute value for the element
     * @return null if there is no default attribute, otherwise return its value.
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#getDefault()
     */
    public String getDefault() throws XBRLException {
    	Element root =getDataRootElement(); 
    	if (! root.hasAttribute("default")) return null;
    	return root.getAttribute("default");
    }
    
    /**
     * Sets the default attribute for the concept.
     * @param value The default attribute value
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#setDefault(String)
     */
    public void setDefault(String value) throws XBRLException {
    	throw new XBRLException("Data update methods are not implemented."); 
    }

    /**
     * Get the fixed attribute value for the element
     * @return null if there is no fixed attribute, otherwise return its value.
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#getFixed()
     */
    public String getFixed() throws XBRLException {
    	Element root =getDataRootElement(); 
    	if (! root.hasAttribute("fixed")) return null;
    	return root.getAttribute("fixed");    	
    }
    
    /**
     * Sets the fixed attribute for the concept which specifies 
     * the fixed value of the concept element
     * @param fixed The fixed attribute value
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#setFixed(String)
     */
    public void setFixed(String fixed) throws XBRLException {
    	throw new XBRLException("Data update methods are not implemented."); 
    }


}