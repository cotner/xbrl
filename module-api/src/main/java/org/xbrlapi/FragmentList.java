package org.xbrlapi;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.List;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

public interface FragmentList<F extends Fragment> extends List<F> {

    /**
     * The number of fragments in the list. The range of 
     * valid child fragment indices is 0 to length-1 inclusive. 
     *
     * @return The number of fragments in the list.
     * @throws AnnotationException
     */
    public int getLength() throws XBRLException;
    
    /**
     * Returns the indexth fragment in the collection. If index 
     * is greater than or equal to the number of fragments in the list, 
     * this returns null.
     * 
     * @param index The <code>index</code> of the required fragment
     * @return The fragment at the <code>index</code>th position in the 
     * FragmentList, or null if that is not a valid index.
     */
    public F getFragment(int index) throws XBRLException;
    
    /**
     * Removes a specified fragment from the list
     * @param fragment The fragment to be removed
     * 
     */
    public void removeFragment(F fragment) throws XBRLException;
    
    /**
     * Adds a specified fragment to the end of the list of fragments.
     * @throws XBRLException if the fragment cannot be added to the list of fragments 
     * or if the fragment is null.
     */
    public void addFragment(F fragment) throws XBRLException;
    
    /**
     * Appends a list of fragments to this fragment list.
     * @param list The list of fragments to append to this fragment list.
     * @throws XBRLException
     */
    public void addAll(FragmentList<F> list) throws XBRLException;
}
