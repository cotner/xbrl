package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import java.util.List;
import org.xbrlapi.Locator;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class LocatorImpl extends ArcEndImpl implements Locator {
	
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
    	if (! root.hasAttributeNS(Constants.XLinkNamespace,"href")) throw new XBRLException("Locators must have xlink:href attributes.");
    	return root.getAttributeNS(Constants.XLinkNamespace,"href");
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
     * Get the single fragment that this locator references.
     * @return the single fragment referenced by the locator.
     * @throws XBRLException if the locator does not reference exactly one fragment.
     * @see org.xbrlapi.Locator#getTarget()
     */
    public Fragment getTarget() throws XBRLException {

        long start = System.currentTimeMillis();
        
        URI uri = this.getStore().getMatcher().getMatch(getTargetDocumentURI());

        String pointerValue = getTargetPointerValue();
    	if (! pointerValue.equals("")) {
    	    String query = "#roots#[" + Constants.XBRLAPIPrefix+ ":" + "xptr/@value='" + pointerValue + "']";
            List<Fragment> fragments = getStore().<Fragment>query(query);
            for (Fragment fragment: fragments) {
                if (fragment.getURI().equals(uri)) {
                    logger.info("MS to get located fragment = " + (System.currentTimeMillis() - start));
                    return fragment;
                }
            }
            throw new XBRLException("The simple link does not reference a fragment.");
    	}
    	
    	Fragment fragment = getStore().getRootFragmentForDocument(uri);
        if (fragment == null) throw new XBRLException("The simple link does not reference a fragment.");
        return fragment;
    }
    
    /**
     * @see org.xbrlapi.ExtendedLinkContent#getExtendedLink()
     */
    public ExtendedLink getExtendedLink() throws XBRLException {
        Fragment parent = this.getParent();
        if (! parent.isa("org.xbrlapi.impl.ExtendedLinkImpl")) throw new XBRLException("The parent of locator " + this.getIndex() + " is not an extended link.");
        return (ExtendedLink) parent;
    }    
    
}
