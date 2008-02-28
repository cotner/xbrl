package org.xbrlapi.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Network;
import org.xbrlapi.Networks;
import org.xbrlapi.Relationship;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NetworksImpl implements Networks {

	protected static Logger logger = Logger.getLogger(Loader.class);	
	
	// Map of networks: indexed by the arc role then the link role.
	private HashMap<String,HashMap<String,Network>> networks = new HashMap<String,HashMap<String,Network>>();
	
	public NetworksImpl() {
		super();
	}

	/**
	 * @see org.xbrlapi.Networks#addNetwork(Network)
	 */
	public void addNetwork(Network network) throws XBRLException {
		String arcRole = network.getArcRole();
		String linkRole = network.getLinkRole();
		
		HashMap<String,Network> map = null;
		
		if (networks.containsKey(arcRole)) {
			map = networks.get(arcRole);
			if (map.containsKey(linkRole)) {
				throw new XBRLException("The collection of networks already contains a network with the given arc and link roles.");
			}
			map.put(linkRole,network);
			return;
		}
		
		map = new HashMap<String,Network>();
		map.put(linkRole,network);
		networks.put(arcRole,map);
		this.getArcRoles();
	}

	/**
	 * @see org.xbrlapi.Networks#getNetwork(String, String)
	 */
	public Network getNetwork(String arcRole, String linkRole)
			throws XBRLException {
		if (networks.containsKey(arcRole)) {
			HashMap<String,Network> map = networks.get(arcRole);
			if (map.containsKey(linkRole))
				return map.get(linkRole);
		}
		return null;
	}
	
	/**
	 * @see org.xbrlapi.Networks#getNetworks(String)
	 */
	public List<Network> getNetworks(String arcRole) throws XBRLException {
		
		List<Network> selectedNetworks = new LinkedList<Network>();
		List<String> linkRoles = getLinkRoles(arcRole);
		if (linkRoles.isEmpty()) return selectedNetworks;
		for (String linkRole: linkRoles) {
			selectedNetworks.add(this.getNetwork(arcRole,linkRole));
		}
		return selectedNetworks;
	}
	
	/**
	 * @see org.xbrlapi.Networks#getSourceFragments(String, String)
	 */
	public <F extends Fragment> FragmentList<F>  getSourceFragments(String targetIndex, String arcRole) throws XBRLException {
		
		FragmentList<F> fragments = new FragmentListImpl<F>();
		
    	List<Network> selectedNetworks = this.getNetworks(arcRole);
    	for (Network network: selectedNetworks) {
    		List<Relationship> relationships = network.getActiveRelationshipsTo(targetIndex);
        	for (Relationship relationship: relationships) {
        		fragments.addFragment(relationship.<F>getSource());
        	}
    	}
		return fragments;
	}
	
	/**
	 * @see org.xbrlapi.Networks#getSourceFragments(String, String, String)
	 */
	public <F extends Fragment> FragmentList<F>  getSourceFragments(String targetIndex, String arcRole, String linkRole) throws XBRLException {
		FragmentList<F> fragments = new FragmentListImpl<F>();
		if (! hasNetwork(arcRole, linkRole)) return fragments;
    	Network network = this.getNetwork(arcRole, linkRole);
		List<Relationship> relationships = network.getActiveRelationshipsTo(targetIndex);
    	for (Relationship relationship: relationships) {
    		fragments.addFragment(relationship.<F>getSource());
    	}
		return fragments;
	}	
	
	/**
	 * @see org.xbrlapi.Networks#getTargetFragments(String, String)
	 */
	public <F extends Fragment> FragmentList<F>  getTargetFragments(String sourceIndex, String arcRole) throws XBRLException {
		
		FragmentList<F> fragments = new FragmentListImpl<F>();
		
    	List<Network> selectedNetworks = this.getNetworks(arcRole);
    	logger.info("There are " + selectedNetworks.size() + " networks with arcrole " + arcRole);
    	for (Network network: selectedNetworks) {
            logger.info("A network has linkrole " + network.getLinkRole());
    		List<Relationship> relationships = network.getActiveRelationshipsFrom(sourceIndex);
            logger.info("The network contains " + relationships.size() + " relationships from " + sourceIndex);
        	for (Relationship relationship: relationships) {
        		fragments.addFragment(relationship.<F>getTarget());
        	}
    	}
		return fragments;
	}
	
	/**
	 * @see org.xbrlapi.Networks#getTargetFragments(String, String, String)
	 */
	public <F extends Fragment> FragmentList<F>  getTargetFragments(String sourceIndex, String arcRole, String linkRole) throws XBRLException {
		
		FragmentList<F> fragments = new FragmentListImpl<F>();
		if (! hasNetwork(arcRole, linkRole)) return fragments;
    	Network network = this.getNetwork(arcRole, linkRole);
		List<Relationship> relationships = network.getActiveRelationshipsFrom(sourceIndex);
    	for (Relationship relationship: relationships) {
    		fragments.addFragment(relationship.<F>getTarget());
    	}
		return fragments;
	}	

	/**
	 * @see org.xbrlapi.Networks#hasNetwork(String, String)
	 */
	public boolean hasNetwork(String arcRole, String linkRole) {
		if (networks.containsKey(arcRole)) {
			HashMap<String,Network> map = networks.get(arcRole);
			if (map.containsKey(linkRole)) return true;
		}
		return false;
	}
	
	
	/**
	 * @see org.xbrlapi.Networks#addRelationship(Relationship)
	 */
	public void addRelationship(Relationship relationship) throws XBRLException {
		logger.info("Networks being augmented with relationship: " + relationship.toString());
		String arcRole = relationship.getArcRole();
		String linkRole = relationship.getLinkRole();
		
		if (hasNetwork(arcRole,linkRole)) {
			getNetwork(arcRole,linkRole).addRelationship(relationship);
			return;
		}
		logger.info("A new network is required for relationship with linkrole " + linkRole + " and arcrole " + arcRole);
		Network network = new NetworkImpl(linkRole,arcRole);
		network.addRelationship(relationship);
		addNetwork(network);
	}
	
	/**
	 * @see org.xbrlapi.Networks#getSize()
	 */
	public int getSize() throws XBRLException {
		int size = 0;
		List<String> arcroles = this.getArcRoles();
		for (String arcrole: arcroles) {
			List<String> linkroles = getLinkRoles(arcrole);
			size += linkroles.size();
		}
		return size;
	}
	
	
	/**
	 * @see org.xbrlapi.Networks#getArcRoles()
	 */
	public List<String> getArcRoles() throws XBRLException {
		List<String> roles = new LinkedList<String>();
		for (String value: networks.keySet()) {
			roles.add(value);
		}
		return roles;
	}
	
	/**
	 * @see org.xbrlapi.Networks#getLinkRoles(String)
	 */
	public List<String> getLinkRoles(String arcRole) throws XBRLException {
		List<String> roles = new LinkedList<String>();

		if (networks.containsKey(arcRole)) {
			HashMap<String,Network> map = networks.get(arcRole);
			if (map == null) return roles;
			for (String value: map.keySet()) {
				roles.add(value);
			}
		}
		return roles;
	}
}
