package org.xbrlapi.networks;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Fragment;
import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * The networks interface defines the functionality
 * provided by a collection of individual network
 * objects.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface Networks extends Iterable<Network> {
	
    
    /**
     * Complete the networks, finding all active relationships in the data store
     * that participate in the networks.
     * @throws XBRLException
     */
    public void complete() throws XBRLException;
    
	/**
	 * If the collection of networks already has a network with the 
	 * same role and arcrole, then the relationships from the added
	 * network are added into the existing network.
	 * @param network The network to add to the collection of networks.
	 * @throws XBRLException
	 */
	public void addNetwork(Network network) throws XBRLException;
	
	/**
	 * @param linkRole The link role of the network to get.
     * @param arcrole The arc role of the network to get.
	 * @return The network corresponding to the given arc and link roles
	 * or null if no such network is available.
	 * @throws XBRLException
	 */
	public Network getNetwork(URI arcrole, URI linkRole) throws XBRLException;

	/**
	 * @param arcrole The arc role to get the networks for.
	 * @return a list of networks, one per link role that exists
	 * for the specified arc role.
	 * @throws XBRLException
	 */
	public List<Network> getNetworks(URI arcrole) throws XBRLException;
	
	/**
	 * @param targetIndex The index of the fragment that is the target for the sources
	 * being retrieved
	 * @param arcrole The arc role of the relationships to navigate from the given target
	 * to the sources being retrieved.
	 * @return The set of source fragments for active relationships in the collection
	 * of networks that are direct connections from the source fragments to the specified
	 * target fragment.
	 * @throws XBRLException
	 */
	public <F extends Fragment> List<F> getSourceFragments(String targetIndex, URI arcrole) throws XBRLException;
	
	/**
	 * @param targetIndex The index of the fragment that is the target for the sources
	 * being retrieved
	 * @param linkRole The arc role of the relationships to navigate from the given target
	 * to the sources being retrieved.
     * @param arcrole The arc role of the relationships to navigate from the given target
     * to the sources being retrieved.
	 * @return The set of source fragments for active relationships in the chosen network
	 * that are direct connections from the source fragments to the specified.
	 * target fragment.
	 * @throws XBRLException
	 */
	public <F extends Fragment> List<F>  getSourceFragments(String targetIndex, URI arcrole, URI linkRole) throws XBRLException;
	

	/**
	 * @param sourceIndex The index of the fragment that is the source for the targets
	 * being retrieved
	 * @param arcrole The arc role of the relationships to navigate from the given source
	 * to the targets being retrieved.
	 * @return The set of target fragments for active relationships in the collection
	 * of networks that are direct connections from the target fragments to the specified
	 * source fragment.
	 * @throws XBRLException
	 */
	public <F extends Fragment> List<F>  getTargets(String sourceIndex, URI arcrole) throws XBRLException;
	
	/**
	 * @param sourceIndex The index of the fragment that is the source for the targets
	 * being retrieved
	 * @param linkRole The arc role of the relationships to navigate from the given source
	 * to the targets being retrieved.
     * @param arcrole The arc role of the relationships to navigate from the given source
     * to the targets being retrieved.
	 * @return The set of target fragments for active relationships in the chosen network
	 * that are direct connections from the target fragments to the specified source fragment.
	 * @throws XBRLException
	 */
	public <F extends Fragment> List<F>  getTargets(String sourceIndex, URI linkRole, URI arcrole) throws XBRLException;
		
	/**
     * @param linkRole The link role of the network to get.
	 * @param arcrole The arc role of the network to get.
	 * @return true if there is a network in the collection for
	 * the given arc role and link role.
	 */
	public boolean hasNetwork(URI linkRole, URI arcrole);
	
	/**
	 * @param relationship The relationship to add to the collection of networks.
	 * @throws XBRLException
	 */
	public void addRelationship(PersistedRelationship relationship) throws XBRLException;
	
	/**
	 * Adds all relationships with the given arcrole to the set of networks.
	 * @param arcrole The required arcrole.
	 * @throws XBRLException
	 */
	public void addRelationships(URI arcrole) throws XBRLException;
	

	/**
	 * @return a list of arc role values for which there are networks in 
	 * the collection.  The list is empty if there are no 
	 * networks in the set of networks.
	 * @throws XBRLException
	 */
	public List<URI> getArcroles() throws XBRLException;
	
	/**
	 * @param arcrole The arc role for which the link roles are required.
	 * @return a list of link role values for which there are networks in the 
	 * collection with the given arc role.  The list is empty if there are no 
	 * networks with the given arc role.
	 * @throws XBRLException
	 */
	public List<URI> getLinkRoles(URI arcrole) throws XBRLException;
	
	/**
	 * @return The number of networks in the collection.
	 * @throws XBRLException
	 */
	public int getSize() throws XBRLException;

	/**
	 * @return the data store containing the information defining this 
	 * collection of networks.
	 */
	public Store getStore();

	/**
	 * Merges the specified set of networks into this set of networks.
	 * @param networks The networks to merge 
	 * into this collection of networks.
	 * @throws XBRLException
	 */
	public void addAll(Networks networks) throws XBRLException;
	
}
