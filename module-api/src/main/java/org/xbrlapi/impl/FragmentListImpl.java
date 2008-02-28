package org.xbrlapi.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentListImpl<F extends Fragment> 
extends ArrayList<F> 
implements FragmentList<F> {
	
	protected static Logger logger = Logger.getLogger(Loader.class);

    /**
     * Get length of the list of fragments.
     * @return The length of the list of fragments.
     * @see org.xbrlapi.FragmentList#getLength()
     */
    public int getLength() {
    	return this.size();
    }

    /**
     * Returns the indexth fragment in the collection. If index 
     * is greater than or equal to the number of fragments in the list, 
     * this returns null.
     * 
     * @param index The <code>index</code> of the required fragment
     * @return The fragment at the <code>index</code>th position in the 
     * FragmentList, or null if that is not a valid index.
     * @see org.xbrlapi.FragmentList#getFragment(int)
     */
    public F getFragment(int index) {
    	try {
    		return this.get(index);
    	} catch (IndexOutOfBoundsException e) {
    		return null;
    	}
    }
    
    /**
     * Removes a specified fragment from the list
     * @param fragment The fragment to be removed
     * @throws XBRLException if the fragment could not be removed.
     */
    public void removeFragment(F fragment) throws XBRLException {
    	if (! remove(fragment)) {
    		throw new XBRLException("The fragment could not be removed from the list.");
    	}
    }

    /**
     * Adds a specified fragment to the end of the list of fragments.
     * @throws XBRLException if the fragment cannot be added to the list of fragments 
     * or if the fragment is null.
     */
    public void addFragment(F fragment) throws XBRLException {
    	if (fragment == null) throw new XBRLException("A null fragment cannot be added to a fragment list.");
    	if (! this.add(fragment)) {
    		throw new XBRLException("Failed to add a fragment to the fragment list.");
    	}
    }
    
    /**
     * Appends a list of fragments to this fragment list.
     * @param fragmentList The list of fragments to append to this fragment list.
     * @throws XBRLException
     * @see org.xbrlapi.FragmentList#addAll(FragmentList)
     */
    public void addAll(FragmentList<F> fragmentList) throws XBRLException {
    	super.addAll(fragmentList);
    }
}
