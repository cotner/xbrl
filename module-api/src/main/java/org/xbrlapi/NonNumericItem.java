package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface NonNumericItem extends Item {
	
	/** 
	 * Get the value of the fact.
	 * @return the value of fact with leading and trailing spaces deleted or null 
	 * if the fact is nill.
	 * @throws XBRLException
	 */
	public String getValue() throws XBRLException;
	
	
}