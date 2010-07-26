package org.xbrlapi.impl;

import java.net.URI;
import java.util.List;

import org.w3c.dom.Element;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ArcEndImpl extends ExtendedLinkContentImpl implements ArcEnd {
	
    /**
     * 
     */
    private static final long serialVersionUID = 8789093562540648488L;

    /**
     * @see org.xbrlapi.ArcEnd#getLabel()
     */
    public String getLabel() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XLinkNamespace.toString(),"label"))
    		return root.getAttributeNS(Constants.XLinkNamespace.toString(),"label");
    	throw new XBRLException("XLink arc ends must have an xlink:label attribute");
    }

    /**
     * @see org.xbrlapi.ArcEnd#getRole()
     */
    public String getRole() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XLinkNamespace.toString(),"role"))
    		return root.getAttributeNS(Constants.XLinkNamespace.toString(),"role");
    	return null;
    }

    /**
     * @see org.xbrlapi.ArcEnd#getArcsFrom()
     */
    public List<Arc> getArcsFrom() throws XBRLException {
    	return getExtendedLink().getArcsWithFromLabel(this.getLabel());
    }

    /**
     * @see org.xbrlapi.ArcEnd#getArcsFromWithArcrole(URI)
     */
    public List<Arc> getArcsFromWithArcrole(URI arcrole) throws XBRLException {
        return getExtendedLink().getArcsWithFromLabelAndArcrole(this.getLabel(),arcrole);
    }

    /**
     * @see org.xbrlapi.ArcEnd#getArcsToWithArcrole(URI)
     */
    public List<Arc> getArcsToWithArcrole(URI arcrole) throws XBRLException {
        return getExtendedLink().getArcsWithToLabelAndArcrole(this.getLabel(),arcrole);
    }

    /**
     * @see org.xbrlapi.ArcEnd#getArcsTo()
     */
    public List<Arc> getArcsTo() throws XBRLException {
    	logger.debug("Getting the arcs to end with label " + getLabel());
    	return getExtendedLink().getArcsWithToLabel(this.getLabel());
    }

    /**
     * @see org.xbrlapi.ArcEnd#getArcEndId()
     */
    public String getArcEndId() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttribute("id"))
    		return root.getAttribute("id");
    	return null;
    }
    
}
