package org.xbrlapi.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SimpleLinkImpl extends LinkImpl implements SimpleLink {
	
	/**
	 * @see org.xbrlapi.SimpleLink#setTarget(URL)
	 */
	public void setTarget(URL url) throws XBRLException {
		setMetaAttribute("absoluteHref",url.toString());
		setMetaAttribute("targetDocumentURL",this.getTargetDocumentURL(url).toString());
		setMetaAttribute("targetPointerValue",this.getTargetPointerValue(url.getRef()));
	}

    /**
     * Get the link HREF attribute value, before any resolution.
     * @return the CacheURLImpl value of the XLink href attribute
     * @throws XBRLException
     * @see org.xbrlapi.SimpleLink#getHref()
     */
    public String getHref() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"href");
    }
    


    /**
     * Get the absolute value of the HREF to the metadata.
     * @return The absolute URL specified by the locator HREF attribute.
     * @throws XBRLException.
     * @see org.xbrlapi.SimpleLink#getAbsoluteHref()
     */
    public URL getAbsoluteHref() throws XBRLException {
    	try {
    		return new URL(this.getMetadataRootElement().getAttribute("absoluteHref"));
    	} catch (MalformedURLException e) {
    		throw new XBRLException("Absolute URL in the HREF of the locator is malformed.",e);
    	}
    }        
    
    /**
     * @return the document URL for the target fragment.
     * @throws XBRLException.
     */
    private URL getTargetDocumentURL() throws XBRLException {
    	try {
    		return new URL(this.getMetadataRootElement().getAttribute("targetDocumentURL"));
    	} catch (MalformedURLException e) {
    		throw new XBRLException("Absolute URL in the HREF of the locator is malformed.",e);
    	}
    }
    
    /**
     * Get the string value of the XPointer element scheme expression from the xlink:href attribute.
     * @return the string value of the XPointer element scheme expression from the xlink:href attribute.
     * @throws XBRLException.
     */
    private String getTargetPointerValue() throws XBRLException {
    	return this.getMetadataRootElement().getAttribute("targetPointerValue");
    }
    
    /**
     * Get the link arcrole attribute value, before any resolution.
     * @throws XBRLException
     * @see org.xbrlapi.SimpleLink#getArcrole()
     */
    public String getArcrole() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"arcrole");
    }
    

    
    /**
     * @return the single fragment referenced by the simple link.
     * @throws XBRLException if the simple link does not reference exactly one fragment.
     * @see org.xbrlapi.SimpleLink#getTargetFragment()
     */
    public Fragment getTargetFragment() throws XBRLException {

    	String pointerCondition = " and @parentIndex='none'";
    	String pointerValue = getTargetPointerValue();
    	if (! pointerValue.equals("")) {
    		pointerCondition = " and "+ Constants.XBRLAPIPrefix+ ":" + "xptr/@value='" + pointerValue + "'";
    	}
    	
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + getTargetDocumentURL() + "'" + pointerCondition + "]";
    	logger.debug(query);
    	FragmentList<Fragment> fragments = getStore().query(query);
    	if (fragments.getLength() == 0) return null;
    	if (fragments.getLength() > 1) throw new XBRLException("The simple link references more than one fragment.");
    	return fragments.getFragment(0);
    	
    }

}
