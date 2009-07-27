package org.xbrlapi.networks;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.xbrlapi.Fragment;
import org.xbrlapi.PersistedRelationship;
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
	public URI getArcrole();

	/**
	 * @param arcrole the arc role to set.
	 * @throws XBRLException if the arc role is null.
	 */
	public void setArcrole(URI arcrole) throws XBRLException;

	/**
	 * @return the link role.
	 */
	public URI getLinkRole();

	/**
	 * @param linkRole the linkRole to set.
	 * @throws XBRLException if the link role is null.
	 */
	public void setLinkRole(URI linkRole) throws XBRLException;

	/** 
	 * @param index The index of the fragment to check.
	 * @return true if the fragment with the supplied index
	 * is already included in the set of fragments participating
	 * in the network.
	 */
	public boolean hasFragment(String index) throws XBRLException;
	
    /** 
     * @param index The index of the fragment to check.
     * @return true if the network contains active relationships 
     * from the specified fragment and false otherwise.
     */
    public boolean hasActiveRelationshipsFrom(String index);
    
    /** 
     * @param index the index of the fragment.
     * @return true if the fragment with the given index
     * has a single parent fragment in the network and 
     * false otherwise.
     * @throws XBRLException
     */
    public boolean hasSingleParent(String index) throws XBRLException;
    
    /** 
     * @param index the index of the fragment.
     * @return true if the fragment with the given index
     * has no parent fragments and false otherwise.
     * @throws XBRLException
     */
    public boolean isRoot(String index) throws XBRLException;
    
    /** 
     * @param index the index of the fragment.
     * @return true if the fragment with the given index
     * has no child fragments in the network and false otherwise.
     * @throws XBRLException
     */
    public boolean isLeaf(String index) throws XBRLException;    
    
    
	
    /** 
     * @param index The index of the fragment to check.
     * @return true if the network contains active relationships 
     * to the specified fragment and false otherwise.
     */
    public boolean hasActiveRelationshipsTo(String index);
        

	/** 
	 * @param index The index of the fragment to get from the set
	 * of fragments already participating in the network.
	 * @return The requested fragment or null if the fragment is not
	 * already included in the set of fragments stored by the network.
	 */
	public Fragment get(String index) throws XBRLException;
	
	/**
	 * @param <F> The type of fragment to use for the fragments in the list.
	 * @return a list of the fragments in the network 
	 * that are roots in the sense that they are sources of relationships 
	 * but not targets of relationships.
	 */
	public <F extends Fragment> List<F> getRootFragments();
	
	/**
	 * @return a list of the indexes of the fragments in the network 
	 * that are roots in the sense that they are sources of relationships 
	 * but not targets of relationships.
	 */
	public Set<String> getRootFragmentIndexes();	
	
	/**
	 * Adds the relationship to the network making it discoverable
	 * given the index of the source fragment and the target fragment.
	 * If the relationship has already been recorded in the network, then
	 * it is not added again because that would be redundant and potentially
	 * confusing.
	 * @param relationship The relationship to add.
	 * @throws XBRLException
	 */
	public void addRelationship(PersistedRelationship relationship) throws XBRLException;

	/**
	 * @param index The index of the source fragment.
	 * @return the relationships that override or prohibit all other equivalent
	 * relationships in the network and that run from the same source fragment.
	 * The list is ordered by the order attributes on the relationships from lowest to highest.
	 * @throws XBRLException
	 */
	public SortedSet<PersistedRelationship> getActiveRelationshipsFrom(String index) throws XBRLException;

	/**
	 * @param index The index of the target fragment.
	 * @return the relationships that override or prohibit all other equivalent
	 * relationships in the network and that run to the same target fragment.
	 * @throws XBRLException
	 */
	public SortedSet<PersistedRelationship> getActiveRelationshipsTo(String index) throws XBRLException;
	
    /**
     * @return the list of all relationships (active and inactive)
     * in the network.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getAllRelationships() throws XBRLException;
    
    /**
     * @return the list of all active relationships in the network.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getAllActiveRelationships() throws XBRLException;    
	

	/**
	 * @param <F> The type of org.xbrlapi.Fragment
	 * @param index The parent fragment index 
	 * @return the list of children fragments for the specified
	 * parent fragment, or an empty list if there are no children fragments.
	 * @throws XBRLException
	 */
	public <F extends Fragment> List<F> getChildren(String index) throws XBRLException;
	
    /**
     * Note that a fragment can have more than one parent fragment given
     * that the networks are not just trees.
     * @param <F> The type of org.xbrlapi.Fragment
     * @param index The child fragment index 
     * @return the list of parent fragments for the specified
     * child fragment, or an empty list if there are no parent fragments.
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getParents(String index) throws XBRLException;
    
    /**
     * @return the number of relationships (active or otherwise) in the network.
     */
    public int getNumberOfRelationships();

    /**
     * @return the number of active relationships in the network.
     */
    public int getNumberOfActiveRelationships();
    
    /**
     * Complete the network, finding all active relationships in the data store
     * that participate in the network.
     * @throws XBRLException
     */
    public void complete() throws XBRLException;
    
    
    /**
     * @param index The index of the fragment to check for in the
     * network.
     * @return true if the fragment is in the network and false 
     * otherwise.
     */
    public boolean contains(String index);

    /**
     * Adds relationships from the supplied network
     * to this network where they are not already included.
     * @param network The network to be merged into this
     * network.
     * @throws XBRLException
     */
    public void add(Network network) throws XBRLException;
    
}
