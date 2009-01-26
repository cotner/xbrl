package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface SimpleLink extends Link {

	/**
	 * Set metadata about the target of the simple link.
	 * This includes, the absolute URI, the target document URI and the XPointer value
	 * used to identify part of the target document.
	 * @throws XBRLException
	 */
	public void setTarget(URI uri) throws XBRLException;
	
    /**
     * Get the link HREF attribute value, before any resolution.
     *
     * @throws XBRLException
     */
    public String getHref() throws XBRLException;
    
    /**
     * Get the link arcrole attribute value, before any resolution.
     *
     * @throws XBRLException
     */
    public String getArcrole() throws XBRLException;
    
    /**
     * @return the single fragment referenced by the simple link 
     * null if none is found.
     * @throws XBRLException
     */
    public Fragment getTargetFragment() throws XBRLException;

    /**
     * Get the absolute value of the HREF to the metadata.
     * @return The absolute URI specified by the locator HREF attribute.
     * @throws XBRLException.
     */
    public URI getAbsoluteHref() throws XBRLException;    

}
