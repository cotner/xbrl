package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.w3c.dom.Element;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LocatorImpl extends ArcEndImpl implements Locator {
	
	/**
     * 
     */
    private static final long serialVersionUID = 2954795492246579940L;

    /**
	 * @see org.xbrlapi.Locator#setTarget(URI)
	 */
	public void setTarget(URI uri) throws XBRLException {
		setMetaAttribute("absoluteHref",uri.toString());
		setMetaAttribute("targetDocumentURI",this.getTargetDocumentURI(uri).toString());
		setMetaAttribute("targetPointerValue",this.getTargetPointerValue(uri.getFragment()));
	}

    /**
     * Get the raw xlink:href attribute value (before any resolution).
     * @return the value of the xlink:href attribute on the locator.
     * @throws XBRLException if the attribute is missing.
     * @see org.xbrlapi.Locator#getHref()
     */
    public String getHref() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttributeNS(Constants.XLinkNamespace.toString(),"href")) throw new XBRLException("Locators must have xlink:href attributes.");
    	return root.getAttributeNS(Constants.XLinkNamespace.toString(),"href");
    }
    

    
    /**
     * Get the absolute value of the HREF to the metadata.
     * @return The absolute URI specified by the locator HREF attribute.
     * @throws XBRLException.
     * @see org.xbrlapi.Locator#getAbsoluteHref()
     */
    public URI getAbsoluteHref() throws XBRLException {
    	try {
    		return new URI(this.getMetadataRootElement().getAttribute("absoluteHref"));
    	} catch (URISyntaxException e) {
    		throw new XBRLException("Absolute URI in the HREF of the locator is malformed.",e);
    	}
    }
    
    /**
     * @see org.xbrlapi.Locator#getTargetDocumentURI()
     */
    public URI getTargetDocumentURI() throws XBRLException {
    	try {
    	    URI originalURI = new URI(this.getMetadataRootElement().getAttribute("targetDocumentURI"));
    		return this.getStore().getMatcher().getMatch(originalURI);
    	} catch (URISyntaxException e) {
    		throw new XBRLException("Absolute URI in the HREF of the locator is malformed.",e);
    	}
    }
    
    /**
     * @see org.xbrlapi.Locator#getTargetPointerValue()
     */
    public String getTargetPointerValue() throws XBRLException {
    	return this.getMetadataRootElement().getAttribute("targetPointerValue");
    }
    
    /**
     * @see org.xbrlapi.Locator#getTarget()
     */
    public Fragment getTarget() throws XBRLException {

        URI uri = this.getStore().getMatcher().getMatch(getTargetDocumentURI());
        logger.debug("The locator's target URI = " + uri);

        String pointerValue = getTargetPointerValue();
        
        logger.debug("The locator's XPointer value = " + pointerValue);
        
    	if (pointerValue.equals("")) {
            Fragment fragment = getStore().getRootFragmentForDocument(uri);
            if (fragment == null) throw new XBRLException("XBRL XLink locators with no XPointer expression must identify a single document root.");
            return fragment;
    	}

    	String query = "#roots#[@uri='" + uri.toString() + "' and " + Constants.XBRLAPIPrefix + ":" + "xptr/@value='" + pointerValue + "']";
        List<Fragment> fragments = getStore().<Fragment>queryForXMLResources(query);
        if (fragments.size() == 1) return fragments.get(0);
        if (fragments.size() == 0) throw new XBRLException("XBRL XLink locators must identify a single resource.");
        throw new XBRLException("XBRL XLink locators must not identify multiple resources.");

    }
    
    /**
     * @see org.xbrlapi.ExtendedLinkContent#getExtendedLink()
     */
    public ExtendedLink getExtendedLink() throws XBRLException {
        Fragment parent = this.getParent();
        if (! parent.isa(ExtendedLinkImpl.class)) throw new XBRLException("The parent of locator " + this.getIndex() + " is not an extended link.");
        return (ExtendedLink) parent;
    }
    
}
