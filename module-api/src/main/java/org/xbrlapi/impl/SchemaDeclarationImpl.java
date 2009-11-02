package org.xbrlapi.impl;

import java.net.URI;

import org.w3c.dom.Element;
import org.xbrlapi.SchemaDeclaration;
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
        Element element = getDataRootElement();
        if (element.hasAttribute("name")) return element.getAttribute("name");
        return null;
    }

    /**
     * @see org.xbrlapi.SchemaDeclaration#hasReference()
     */
    public boolean hasReference() throws XBRLException {
        return getDataRootElement().hasAttribute("ref");
    }

    /**
     * @see org.xbrlapi.SchemaDeclaration#getReferenceNamespace()
     */
    public URI getReferenceNamespace() throws XBRLException {
        return getNamespaceFromQName(getReferenceQName(), getDataRootElement());
    }
    
    /**
     * @see org.xbrlapi.SchemaDeclaration#getReferenceNamespaceAlias()
     */
    public String getReferenceNamespaceAlias() throws XBRLException {
        return getPrefixFromQName(getReferenceQName());
    }

    /**
     * @see org.xbrlapi.SchemaDeclaration#getReferenceQName()
     */
    public String getReferenceQName() throws XBRLException {
        if (! hasReference()) throw new XBRLException("The content declaration does not use a reference to another XML Schema declaration.");
        String qname = getDataRootElement().getAttribute("ref");
        if (qname.equals("") || (qname == null)) throw new XBRLException("The element declaration does not use a reference to another XML Schema declaration.");
        return qname;
    }

    /**
     * @see org.xbrlapi.SchemaDeclaration#getReferenceLocalname()
     */  
    public String getReferenceLocalname() throws XBRLException {
        return getLocalnameFromQName(getReferenceQName());
    }

    /**
     * @see org.xbrlapi.SchemaDeclaration#getReferencedSchemaDeclaration()
     */  
    @SuppressWarnings("unchecked")
    public <F extends SchemaDeclaration> F getReferencedSchemaDeclaration() throws XBRLException {
        try {
            F sd = (F) getStore().getSchemaContent(this.getReferenceNamespace(),this.getReferenceLocalname());
            if (sd == null) throw new XBRLException("The schema declaration is not in a schema contained in the data store.");
            return sd;
        } catch (ClassCastException e) {
            throw new XBRLException("The XML Schema declaration is  of the wrong fragment type.",e);
        }
    }
    
}