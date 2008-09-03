package org.xbrlapi.networks;

import java.util.List;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.utilities.XBRLException;

/**
 * The networks interface defines the functionality
 * provided by a collection of individual network
 * objects.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface Networks {
	
	/**
	 * @param network The network to add to the collection of networks.
	 * @throws XBRLException if the collection already contains a 
	 * network with the same arc and link role as the network being added.
	 */
	public void addNetwork(Network network) throws XBRLException;
	
	/**
	 * @param arcRole The arc role of the network to get.
	 * @param linkRole The link role of the network to get.
	 * @return The network corresponding to the given arc and link roles
	 * or null if no such network is available.
	 * @throws XBRLException
	 */
	public Network getNetwork(String arcRole, String linkRole) throws XBRLException;

	/**
	 * @param arcRole The arc role to get the networks for.
	 * @return a list of networks, one per link role that exists
	 * for the specified arc role.
	 * @throws XBRLException
	 */
	public List<Network> getNetworks(String arcRole) throws XBRLException;
	
	/**
	 * @param targetIndex The index of the fragment that is the target for the sources
	 * being retrieved
	 * @param arcRole The arc role of the relationships to navigate from the given target
	 * to the sources being retrieved.
	 * @return The set of source fragments for active relationships in the collection
	 * of networks that are direct connections from the source fragments to the specified
	 * target fragment.
	 * @throws XBRLException
	 */
	public <F extends Fragment> FragmentList<F> getSourceFragments(String targetIndex, String arcRole) throws XBRLException;
	
	/**
	 * @param targetIndex The index of the fragment that is the target for the sources
	 * being retrieved
	 * @param arcRole The arc role of the relationships to navigate from the given target
	 * to the sources being retrieved.
	 * @param linkRole The arc role of the relationships to navigate from the given target
	 * to the sources being retrieved.
	 * @return The set of source fragments for active relationships in the chosen network
	 * that are direct connections from the source fragments to the specified.
	 * target fragment.
	 * @throws XBRLException
	 */
	public <F extends Fragment> FragmentList<F>  getSourceFragments(String targetIndex, String arcRole, String linkRole) throws XBRLException;
	

	/**
	 * @param sourceIndex The index of the fragment that is the source for the targets
	 * being retrieved
	 * @param arcRole The arc role of the relationships to navigate from the given source
	 * to the targets being retrieved.
	 * @return The set of target fragments for active relationships in the collection
	 * of networks that are direct connections from the target fragments to the specified
	 * source fragment.
	 * @throws XBRLException
	 */
	public <F extends Fragment> FragmentList<F>  getTargetFragments(String sourceIndex, String arcRole) throws XBRLException;
	
	/**
	 * @param sourceIndex The index of the fragment that is the source for the targets
	 * being retrieved
	 * @param arcRole The arc role of the relationships to navigate from the given source
	 * to the targets being retrieved.
	 * @param linkRole The arc role of the relationships to navigate from the given source
	 * to the targets being retrieved.
	 * @return The set of target fragments for active relationships in the chosen network
	 * that are direct connections from the target fragments to the specified source fragment.
	 * @throws XBRLException
	 */
	public <F extends Fragment> FragmentList<F>  getTargetFragments(String sourceIndex, String arcRole, String linkRole) throws XBRLException;
		
	/**
	 * @param arcRole The arc role of the network to get.
	 * @param linkRole The link role of the network to get.
	 * @return true if there is a network in the collection for
	 * the given arc role and link role.
	 */
	public boolean hasNetwork(String arcRole, String linkRole);
	
	/**
	 * @param relationship The relationship to add to the collection of networks.
	 * @throws XBRLException
	 */
	public void addRelationship(Relationship relationship) throws XBRLException;	

	/**
	 * @return a list of arc role values for which there are networks in 
	 * the collection.  The list is empty if there are no 
	 * networks in the set of networks.
	 * @throws XBRLException
	 */
	public List<String> getArcRoles() throws XBRLException;
	
	/**
	 * @param arcRole The arc role for which the link roles are required.
	 * @return a list of link role values for which there are networks in the 
	 * collection with the given arc role.  The list is empty if there are no 
	 * networks with the given arc role.
	 * @throws XBRLException
	 */
	public List<String> getLinkRoles(String arcRole) throws XBRLException;
	
	/**
	 * @return The number of networks in the collection.
	 * @throws XBRLException
	 */
	public int getSize() throws XBRLException;
	
	
}
