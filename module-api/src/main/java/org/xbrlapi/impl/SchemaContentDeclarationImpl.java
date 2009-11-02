package org.xbrlapi.impl;

import java.net.URI;
import java.util.List;

import org.w3c.dom.Element;
import org.xbrlapi.SchemaContentDeclaration;
import org.xbrlapi.TypeDeclaration;
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
        TypeDeclaration result = this.getLocalType();
        if (result != null) return result;
        if (this.hasTypeReference()) {
            try {
                result = (TypeDeclaration) getStore().getSchemaContent(this.getTypeNamespace(),this.getTypeLocalname());
                if (result == null) throw new XBRLException("The type " + this.getTypeQName() + " is not declared in a schema contained in the data store.");
            } catch (ClassCastException cce) {
                throw new XBRLException("The XML Schema type declaration is  of the wrong fragment type.",cce);
            }
        }
        return result;
    }

    /**
     * @return The XQuery that will retrieve the local type fragments for this fragment.
     */
    private String localTypeQuery() {
        return "for $root in #roots#[@parentIndex='"+getIndex()+"'] where $root/@type='org.xbrlapi.impl.SimpleTypeDeclarationImpl' or $root/@type='org.xbrlapi.impl.ComplexTypeDeclarationImpl' return $root";
    }
    
    /**
     * @see SchemaContentDeclaration#hasLocalType()
     */
    public boolean hasLocalType() throws XBRLException {
        long localTypes = getStore().queryCount(localTypeQuery());
        if (localTypes > 1) throw new XBRLException("The Schema content declaration has too many local types.");
        return (localTypes == 1);
    }
    
    /**
     * @see org.xbrlapi.SchemaContentDeclaration#getLocalType()
     */
    public TypeDeclaration getLocalType() throws XBRLException {
        
        List<TypeDeclaration> types = getStore().<TypeDeclaration>queryForXMLResources(localTypeQuery());
        if (types.size() == 1) return types.get(0);
        if (types.size() > 1) throw new XBRLException("There are too many local type declarations for this content declaration.");
        return null;
    }    
    
}