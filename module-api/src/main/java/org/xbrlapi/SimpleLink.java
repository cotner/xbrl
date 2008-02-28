package org.xbrlapi;

import java.net.URL;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface SimpleLink extends Link {

	/**
	 * Set metadata about the target of the simple link.
	 * This includes, the absolute URL, the target document URL and the XPointer value
	 * used to identify part of the target document.
	 * @throws XBRLException
	 */
	public void setTarget(URL url) throws XBRLException;
	
    /**
     * Get the link HREF attribute value, before any resolution.
     *
     * @throws XBRLException
     */
    public String getHref() throws XBRLException;
    
    /**
     * Set the link HREF attribute value.
     *
     * @param href The value of the href attribute
     * @throws XBRLException
     */
    public void setHref(String href) throws XBRLException;

    /**
     * Get the link arcrole attribute value, before any resolution.
     *
     * @throws XBRLException
     */
    public String getArcrole() throws XBRLException;
    
    /**
     * Set the link arcrole attribute value.
     *
     * @param arcrole The value of the arcrole attribute
     * @throws XBRLException
     */
    public void setArcrole(String arcrole) throws XBRLException;
    
    /**
     * @return the single fragment referenced by the simple link 
     * null if none is found.
     * @throws XBRLException
     */
    public Fragment getTargetFragment() throws XBRLException;

    /**
     * Get the absolute value of the HREF to the metadata.
     * @return The absolute URL specified by the locator HREF attribute.
     * @throws XBRLException.
     */
    public URL getAbsoluteHref() throws XBRLException;    

}
