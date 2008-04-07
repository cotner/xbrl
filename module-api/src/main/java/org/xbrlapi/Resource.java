package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Resource extends ArcEnd {

	/**
	 * Get the value of the resource role (xlink:role attribute) for the resource.
	 * @return the value of the resource role (xlink:role attribute) for the resource or 
	 * null if none is specified.
	 * @throws XBRLException.
	 */
	public String getResourceRole() throws XBRLException;
	
	/**
	 * @return the XML language code for the resource or null
	 * if none is specified.
	 * @throws XBRLException
	 */
    public String getLanguage() throws XBRLException;
    
    /**
     * @return the Language for the specified language encoding
     * for the resource or null if none is specified or none is 
     * available.
     * @throws XBRLException
     */
    public String getLanguage(String language) throws XBRLException;    
}
