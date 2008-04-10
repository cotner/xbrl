package org.xbrlapi.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Element;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Locator;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class LocatorImpl extends ArcEndImpl implements Locator {
	
	/**
	 * @see org.xbrlapi.Locator#setTarget(URL)
	 */
	public void setTarget(URL url) throws XBRLException {
		setMetaAttribute("absoluteHref",url.toString());
		setMetaAttribute("targetDocumentURL",this.getTargetDocumentURL(url).toString());
		setMetaAttribute("targetPointerValue",this.getTargetPointerValue(url.getRef()));
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
     * @return The absolute URL specified by the locator HREF attribute.
     * @throws XBRLException.
     * @see org.xbrlapi.Locator#getAbsoluteHref()
     */
    public URL getAbsoluteHref() throws XBRLException {
    	try {
    		return new URL(this.getMetadataRootElement().getAttribute("absoluteHref"));
    	} catch (MalformedURLException e) {
    		throw new XBRLException("Absolute URL in the HREF of the locator is malformed.",e);
    	}
    }
    
    /**
     * @see org.xbrlapi.Locator#getTargetDocumentURL()
     */
    public URL getTargetDocumentURL() throws XBRLException {
    	try {
    	    URL originalURL = new URL(this.getMetadataRootElement().getAttribute("targetDocumentURL"));
    		return this.getStore().getMatcher().getMatch(originalURL);
    	} catch (MalformedURLException e) {
    		throw new XBRLException("Absolute URL in the HREF of the locator is malformed.",e);
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
     * @see org.xbrlapi.Locator#getTargetFragment()
     */
    public Fragment getTargetFragment() throws XBRLException {

    	String pointerCondition = "";
    	String pointerValue = getTargetPointerValue();
    	if (! pointerValue.equals("")) 
    		pointerCondition = " and "+ Constants.XBRLAPIPrefix+ ":" + "xptr/@value='" + pointerValue + "'";

    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + getTargetDocumentURL() + "'" + pointerCondition + "]";
    	FragmentList<Fragment> fragments = getStore().<Fragment>query(query);
    	if (fragments.getLength() == 0) throw new XBRLException("The simple link does not reference a fragment.");
    	if (fragments.getLength() > 1) throw new XBRLException("The simple link references more than one fragment.");
    	return fragments.getFragment(0);
    }
    
}
