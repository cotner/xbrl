package org.xbrlapi.impl;

import java.net.URI;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.SchemaContentDeclaration;
import org.xbrlapi.SchemaDeclaration;
import org.xbrlapi.TypeDeclaration;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Base interface for XML Schema element and attribute declaration fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaContentDeclarationImpl extends SchemaDeclarationImpl implements SchemaContentDeclaration {

    /**
     * @see SchemaContentDeclaration#isGlobal()
     */
    public boolean isGlobal() throws XBRLException {
        return getSchema().getIndex().equals(this.getParentIndex());
    }
    
    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getForm()
     */
    public String getForm() throws XBRLException {
        if (! getDataRootElement().hasAttribute("form")) return null;
        return getDataRootElement().getAttribute("form");
    }    
    
    /**
     * @see SchemaContentDeclaration#getDefault()
     */
    public String getDefault() throws XBRLException {
        Element root = getDataRootElement(); 
        if (! root.hasAttribute("default")) return null;
        return root.getAttribute("default");
    }
    
    /**
     * @see SchemaContentDeclaration#getFixed()
     */
    public String getFixed() throws XBRLException {
        Element root = getDataRootElement(); 
        if (! root.hasAttribute("fixed")) return null;
        return root.getAttribute("fixed");      
    }
    
    /**
     * @see SchemaContentDeclaration#isFixed()
     */
    public boolean isFixed() throws XBRLException {
        return getDataRootElement().hasAttribute("fixed");
    }    
    
    /**
     * @see SchemaContentDeclaration#hasTypeReference()
     */
    public boolean hasTypeReference() throws XBRLException {
        return getDataRootElement().hasAttribute("type");
    }

    /**
     * @see SchemaContentDeclaration#getTypeNamespace()
     */
    public URI getTypeNamespace() throws XBRLException {
        return getNamespaceFromQName(getTypeQName(), getDataRootElement());
    }
    
    /**
     * @see SchemaContentDeclaration#getTypeNamespaceAlias()
     */
    public String getTypeNamespaceAlias() throws XBRLException {
        return getPrefixFromQName(getTypeQName());
    }

    /**
     * @see SchemaContentDeclaration#getTypeQName()
     */
    public String getTypeQName() throws XBRLException {
        if (! hasTypeReference()) throw new XBRLException("The content declaration does not have a named type.");
        String qname = getDataRootElement().getAttribute("type");
        if (qname.equals("") || (qname == null)) throw new XBRLException("The element declaration does not declare its XML Schema data type via a type attribute.");
        return qname;
    }

    /**
     * @see SchemaContentDeclaration#getTypeLocalname()
     */  
    public String getTypeLocalname() throws XBRLException {
        return getLocalnameFromQName(getTypeQName());
    }

    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getTypeDeclaration()
     */  
    public TypeDeclaration getTypeDeclaration() throws XBRLException {
        try {
            TypeDeclaration td = (TypeDeclaration) getStore().getSchemaContent(this.getTypeNamespace(),this.getTypeLocalname());
            if (td == null) throw new XBRLException("The type is not declared in a schema contained in the data store.");
            return td;
        } catch (ClassCastException e) {
            throw new XBRLException("The XML Schema type declaration is  of the wrong fragment type.",e);
        }
    }
    
    /**
     * @see org.xbrlapi.SchemaContentDeclaration#hasRef()
     */
    public boolean hasRef() throws XBRLException {
        return getDataRootElement().hasAttribute("ref");
    }

    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getRefNamespace()
     */
    public URI getRefNamespace() throws XBRLException {
        return getNamespaceFromQName(getRefQName(), getDataRootElement());
    }
    
    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getRefNamespaceAlias()
     */
    public String getRefNamespaceAlias() throws XBRLException {
        return getPrefixFromQName(getRefQName());
    }

    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getTypeQName()
     */
    public String getRefQName() throws XBRLException {
        if (! hasRef()) throw new XBRLException("The content declaration does not use a reference to another XML Schema declaration.");
        String qname = getDataRootElement().getAttribute("ref");
        if (qname.equals("") || (qname == null)) throw new XBRLException("The element declaration does not use a reference to another XML Schema declaration.");
        return qname;
    }

    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getRefLocalname()
     */  
    public String getRefLocalname() throws XBRLException {
        return getLocalnameFromQName(getRefQName());
    }

    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getReferencedSchemaDeclaration()
     */  
    public SchemaDeclaration getReferencedSchemaDeclaration() throws XBRLException {
        try {
            SchemaDeclaration sd = (SchemaDeclaration) getStore().getSchemaContent(this.getRefNamespace(),this.getRefLocalname());
            if (sd == null) throw new XBRLException("The schema declaration is not in a schema contained in the data store.");
            return sd;
        } catch (ClassCastException e) {
            throw new XBRLException("The XML Schema declaration is  of the wrong fragment type.",e);
        }
    }
    
    /**
     * @see SchemaContentDeclaration#hasLocalSimpleType()
     */
    public boolean hasLocalSimpleType() throws XBRLException {
        NodeList children = this.getDataRootElement().getElementsByTagNameNS(Constants.XMLSchemaNamespace.toString(),"simpleType");
        if (children.getLength() > 1) throw new XBRLException("The content declaration has too many type definitions.");
        return (children.getLength() == 1);
    }
    
    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getLocalSimpleType()
     */
    public Element getLocalSimpleType() throws XBRLException {
        try {
            NodeList simpleType = getDataRootElement().getElementsByTagNameNS(Constants.XMLSchemaNamespace.toString(),"simpleType");
            return (Element) simpleType.item(0);

        } catch (Exception e) {
            throw new XBRLException("The local simple type could not be retrieved for the specified concept.");
        }
    }    
    
    
        
    
}