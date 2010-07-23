package org.xbrlapi.impl;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.AspectValueLabel;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * An aspect value label XML resource stores information about
 * a label for an aspect value.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class AspectValueLabelImpl extends NonFragmentXMLImpl implements AspectValueLabel {
	
    /**
     * 
     */
    private static final long serialVersionUID = 4657502706837130397L;
    
    private static final Logger logger = Logger.getLogger(AspectValueLabelImpl.class);
    
    /**
	 * No argument constructor.
	 * @throws XBRLException
	 */
	public AspectValueLabelImpl() throws XBRLException {
		super();
        setBuilder(new BuilderImpl());
	}
	
	public AspectValueLabelImpl(String id, URI aspectId, String valueId, String locale, URI resourceRole, URI linkRole, String label) throws XBRLException {
		this();
        if (id == null) throw new XBRLException("The XML resource ID must not be null.");
        this.setIndex(id);
		this.setAspectId(aspectId);
        if (valueId == null) throw new XBRLException("The aspect value ID must not be null.");
        this.setValueId(valueId);
        this.setLocale(locale);
        this.setResourceRole(resourceRole);
        this.setLinkRole(linkRole);
        this.setLabel(label);
        
        // Up to here all of the properties have been stored in an XML DOM being
        // put together by the builder.
        
		this.finalizeBuilder();
	}

    private void setAspectId(URI aspectId) throws XBRLException {
        if (aspectId == null) throw new XBRLException("The aspectId must not be null.");
        this.setMetaAttribute("aspectId",aspectId.toString());
    }

    private void setValueId(String valueId) throws XBRLException {
        if (valueId == null) throw new XBRLException("The valueId must not be null.");
        this.setMetaAttribute("valueId",valueId);
    }

    private void setLocale(String locale) throws XBRLException {
        if (locale == null) this.setMetaAttribute("locale","null");
        else this.setMetaAttribute("locale",locale);
    }

    private void setResourceRole(URI resourceRole) throws XBRLException {
        if (resourceRole == null) this.setMetaAttribute("resourceRole","null");
        else this.setMetaAttribute("resourceRole",resourceRole.toString());
    }
    
    private void setLinkRole(URI linkRole) throws XBRLException {
        if (linkRole == null) setMetaAttribute("linkRole","null");
        else setMetaAttribute("linkRole",linkRole.toString());
    }
    
    private void setLabel(String label) throws XBRLException {
        if (label == null) throw new XBRLException("The label must not be null.");
        this.setMetaAttribute("label",label);
    }

    /**
     * @see AspectValueLabel#getAspectId()
     */
    public URI getAspectId() throws XBRLException {
        return URI.create(this.getMetaAttribute("aspectId"));
    }

    /**
     * @see AspectValueLabel#getLabel()
     */
    public String getLabel() throws XBRLException {
        return this.getMetaAttribute("label");
    }

    /**
     * @see AspectValueLabel#getLinkRole()
     */
    public URI getLinkRole() throws XBRLException {
        String value = this.getMetaAttribute("linkRole");
        if (value.equals("null")) return null;
        return URI.create(value);
    }

    /**
     * @see AspectValueLabel#getLocale()
     */
    public String getLocale() throws XBRLException {
        String value = this.getMetaAttribute("locale");
        if (value.equals("null")) return null;
        return value;
    }

    /**
     * @see AspectValueLabel#getResourceRole()
     */
    public URI getResourceRole() throws XBRLException {
        String value = this.getMetaAttribute("resourceRole");
        if (value.equals("null")) return null;
        return URI.create(value);
    }

    /**
     * @see AspectValueLabel#getValueId()
     */
    public URI getValueId() throws XBRLException {
        return URI.create(this.getMetaAttribute("resourceRole"));
    }

}
