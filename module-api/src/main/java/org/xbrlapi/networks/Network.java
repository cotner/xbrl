package org.xbrlapi.networks;

import java.util.List;
import java.util.Set;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.utilities.XBRLException;


/**
 * The network interface combines relationships that
 * are not prohibited and that are not over-ridden and
 * that:
 * <ul>
 * <li>have the same link role</li>
 * <li>have the same arc role</li>
 * </ul>
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Network {
	
	/**
	 * @return the arc role.
	 */
	public String getArcRole();

	/**
	 * @param arcRole the arc role to set.
	 * @throws XBRLException if the arc role is null.
	 */
	public void setArcRole(String arcRole) throws XBRLException;

	/**
	 * @return the link role.
	 */
	public String getLinkRole();

	/**
	 * @param linkRole the linkRole to set.
	 * @throws XBRLException if the link role is null.
	 */
	public void setLinkRole(String linkRole) throws XBRLException;

	/** 
	 * @param index The index of the fragment to check.
	 * @return true if the fragment with the supplied index
	 * is already included in the set of fragments participating
	 * in the network.
	 */
	public boolean hasFragment(String index) throws XBRLException;

	/** 
	 * @param index The index of the fragment to get from the set
	 * of fragments already participating in the network.
	 * @return The requested fragment or null if the fragment is not
	 * already included in the set of fragments stored by the network.
	 */
	public Fragment getFragment(String index)  throws XBRLException;
	
	/**
	 * @param <F> The type of fragment to use for the fragments in the list.
	 * @return a list of the fragments in the network 
	 * that are roots in the sense that they are sources of relationships 
	 * but not targets of relationships.
	 */
	public <F extends Fragment> FragmentList<F> getRootFragments();
	
	/**
	 * @return a list of the indexes of the fragments in the network 
	 * that are roots in the sense that they are sources of relationships 
	 * but not targets of relationships.
	 */
	public Set<String> getRootFragmentIndexes();	
	
	/**
	 * Adds the relationship to the network making it discoverable
	 * given the index of the source fragment and the target fragment.
	 * @param relationship The relationship to add.
	 * @throws XBRLException
	 */
	public void addRelationship(Relationship relationship) throws XBRLException;

	/**
	 * @param index The index of the source fragment.
	 * @return the relationships that override or prohibit all other equivalent
	 * relationships in the network and that run from the same source fragment.
	 * The list is ordered by the order attributes on the relationships from lowest to highest.
	 * @throws XBRLException
	 */
	public List<Relationship> getActiveRelationshipsFrom(String index) throws XBRLException;

	/**
	 * @param index The index of the target fragment.
	 * @return the relationships that override or prohibit all other equivalent
	 * relationships in the network and that run to the same target fragment.
	 * @throws XBRLException
	 */
	public List<Relationship> getActiveRelationshipsTo(String index) throws XBRLException;
	
}
