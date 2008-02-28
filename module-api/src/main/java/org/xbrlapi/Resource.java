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
}
