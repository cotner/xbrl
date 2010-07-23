package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Item extends Fact {

	/** 
	 * Get the context for this item.
	 * Presumes that there is at most one XBRL instance in each document in the data store.
	 * @return the context fragment associated with this item
	 * @throws XBRLException if the context reference is missing or the context is unavailable.
	 */
	public Context getContext() throws XBRLException;
	
    /** 
     * @return the ID of the context referenced by this item.
     * @throws XBRLException if the context reference is missing.
     */
    public String getContextId() throws XBRLException;	

}
