package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Item extends Fact {

	/** 
	 * Get the context for this item.
	 * @return the context fragment associated with this item
	 * @throws XBRLException if the context reference is missing or the context is unavailable.
	 */
	public Context getContext() throws XBRLException;
	
	/** 
	 * Set the context for this item.
	 * @param context The context for the item.
	 * @throws XBRLException
	 */
	public void setContext(Context context) throws XBRLException;
	
	/** 
	 * Return true if the item has a nill value.
	 * @throws XBRLException
	 */
	public boolean isNill() throws XBRLException;	

}
