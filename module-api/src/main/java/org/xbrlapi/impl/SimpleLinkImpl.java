package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.xbrlapi.Fragment;
import java.util.List;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SimpleLinkImpl extends LinkImpl implements SimpleLink {
	
	/**
	 * @see org.xbrlapi.SimpleLink#setTarget(URI)
	 */
	public void setTarget(URI uri) throws XBRLException {
		setMetaAttribute("absoluteHref",uri.toString());
		setMetaAttribute("targetDocumentURI",this.getTargetDocumentURI(uri).toString());
		
		// TODO test that we can handle null URI fragments.
	    setMetaAttribute("targetPointerValue",this.getTargetPointerValue(uri.getFragment()));
	}

    /**
     * Get the link HREF attribute value, before any resolution.
     * @return the CacheURIImpl value of the XLink href attribute
     * @throws XBRLException
     * @see org.xbrlapi.SimpleLink#getHref()
     */
    public String getHref() throws XBRLException {
    	return getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"href");
    }
    


    /**
     * Get the absolute value of the HREF to the metadata.
     * @return The absolute URI specified by the locator HREF attribute.
     * @throws XBRLException.
     * @see org.xbrlapi.SimpleLink#getAbsoluteHref()
     */
    public URI getAbsoluteHref() throws XBRLException {
    	try {
    		return new URI(this.getMetadataRootElement().getAttribute("absoluteHref"));
    	} catch (URISyntaxException e) {
    		throw new XBRLException("Absolute URI in the HREF of the locator is malformed.",e);
    	}
    }        
    
    /**
     * @return the document URI for the target fragment.
     * @throws XBRLException.
     */
    private URI getTargetDocumentURI() throws XBRLException {
    	try {
    		return new URI(this.getMetadataRootElement().getAttribute("targetDocumentURI"));
    	} catch (URISyntaxException e) {
    		throw new XBRLException("Absolute URI in the HREF of the locator is malformed.",e);
    	}
    }
    
    /**
     * Get the string value of the XPointer element scheme expression from the xlink:href attribute.
     * @return the string value of the XPointer element scheme expression from the xlink:href attribute.
     */
    private String getTargetPointerValue() {
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
     * @see org.xbrlapi.SimpleLink#getTarget()
     */
    public Fragment getTarget() throws XBRLException {

    	String pointerCondition = " and @parentIndex=''";
    	String pointerValue = getTargetPointerValue();
    	if (! pointerValue.equals("")) {
    		pointerCondition = " and "+ Constants.XBRLAPIPrefix+ ":" + "xptr/@value='" + pointerValue + "'";
    	}
    	
    	String query = "#roots#[@uri='" + getTargetDocumentURI() + "'" + pointerCondition + "]";
    	logger.debug(query);
    	List<Fragment> fragments = getStore().queryForFragments(query);
    	if (fragments.size() == 0) return null;
    	if (fragments.size() > 1) throw new XBRLException("The simple link references more than one fragment.");
    	return fragments.get(0);
    	
    }

}
