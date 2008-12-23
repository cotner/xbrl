package org.xbrlapi.networks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Locator;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NetworksImpl implements Networks {

	protected static Logger logger = Logger.getLogger(Loader.class);	
	
	private HashMap<String,Arc> arcs = new HashMap<String,Arc>();
	
	/**
	 * @param arcIndex The index to check for in testing for whether
	 * the relationships defined by the arc are already in the networks.
	 * @return true if the relationships defined by the arc are already 
	 * in the networks.
	 */
	private boolean hasArc(String arcIndex) {
	    return arcs.containsKey(arcIndex);
	}
	
	/**
	 * The data store containing the information defining the networks.
	 */
	private Store store = null;
	
	// Map of networks: indexed by the arc role then the link role.
	private HashMap<String,HashMap<String,Network>> networks = new HashMap<String,HashMap<String,Network>>();
	
	/**
	 * @param store The data store containing the information defining the networks.
	 * @throws XBRLException if the data store is null.
	 */
	public NetworksImpl(Store store) throws XBRLException {
		super();
		if (store == null) throw new XBRLException("The data store must not be null.");
		this.store = store;
	}

	/**
	 * @see org.xbrlapi.networks.Networks#addNetwork(Network)
	 */
	public void addNetwork(Network network) throws XBRLException {
		String arcRole = network.getArcRole();
		String linkRole = network.getLinkRole();
		
		HashMap<String,Network> map = null;
		
		if (networks.containsKey(arcRole)) {
			map = networks.get(arcRole);
			if (map.containsKey(linkRole)) {
			    Network existingNetwork = this.getNetwork(arcRole,linkRole);
			    existingNetwork.add(network);
			}
			map.put(linkRole,network);
			return;
		}
		
		map = new HashMap<String,Network>();
		map.put(linkRole,network);
		networks.put(arcRole,map);		
		
	}

	/**
	 * @see org.xbrlapi.networks.Networks#getNetwork(String, String)
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
	 * @see org.xbrlapi.networks.Networks#getNetworks(String)
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
	 * @see org.xbrlapi.networks.Networks#getSourceFragments(String, String)
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
	 * @see org.xbrlapi.networks.Networks#getSourceFragments(String, String, String)
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
	 * @see org.xbrlapi.networks.Networks#getTargetFragments(String, String)
	 */
	public <F extends Fragment> FragmentList<F>  getTargetFragments(String sourceIndex, String arcRole) throws XBRLException {
		
		FragmentList<F> fragments = new FragmentListImpl<F>();
		
    	List<Network> selectedNetworks = this.getNetworks(arcRole);
    	logger.debug("There are " + selectedNetworks.size() + " networks with arcrole " + arcRole);
    	for (Network network: selectedNetworks) {
            logger.debug("A network has linkrole " + network.getLinkRole());
    		List<Relationship> relationships = network.getActiveRelationshipsFrom(sourceIndex);
            logger.debug("The network contains " + relationships.size() + " relationships from " + sourceIndex);
        	for (Relationship relationship: relationships) {
        		fragments.addFragment(relationship.<F>getTarget());
        	}
    	}
		return fragments;
	}
	
	/**
	 * @see org.xbrlapi.networks.Networks#getTargetFragments(String, String, String)
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
	 * @see org.xbrlapi.networks.Networks#hasNetwork(String, String)
	 */
	public boolean hasNetwork(String arcRole, String linkRole) {
		if (networks.containsKey(arcRole)) {
			HashMap<String,Network> map = networks.get(arcRole);
			if (map.containsKey(linkRole)) return true;
		}
		return false;
	}
	
	
	/**
	 * @see Networks#addRelationship(Relationship)
	 */
	public void addRelationship(Relationship relationship) throws XBRLException {
		logger.debug("Networks being augmented with relationship: " + relationship.toString());
		String arcRole = relationship.getArcRole();
		String linkRole = relationship.getLinkRole();
		
		if (hasNetwork(arcRole,linkRole)) {
			getNetwork(arcRole,linkRole).addRelationship(relationship);
			return;
		}
		logger.debug("A new network is required for relationship with linkrole " + linkRole + " and arcrole " + arcRole);
		Network network = new NetworkImpl(getStore(),linkRole,arcRole);
		network.addRelationship(relationship);
		addNetwork(network);
	}
	
    /**
     * @see Networks#addRelationships(String)
     */
    public void addRelationships(String arcrole) throws XBRLException {

        String query = "/*[@type='org.xbrlapi.impl.ArcImpl' and */*[@xlink:type='arc' and @xlink:arcrole='"+ arcrole +"']]";
        FragmentList<Arc> arcs = this.getStore().<Arc>query(query);
        for (Arc arc: arcs) {
            FragmentList<ArcEnd> sources = arc.getSourceFragments();
            FragmentList<ArcEnd> targets = arc.getTargetFragments();
            for (Fragment source: sources) {
                Fragment s = source;
                if (source.isa("org.xbrlapi.impl.LocatorImpl")) s = ((Locator) source).getTargetFragment();
                for (Fragment target: targets) {
                    Fragment t = target;
                    if (target.isa("org.xbrlapi.impl.LocatorImpl")) t = ((Locator) target).getTargetFragment();
                    Relationship relationship = new RelationshipImpl(arc,s,t);
                    this.addRelationship(relationship);
                }
            }
        }

    }
	
	
	/**
	 * @see org.xbrlapi.networks.Networks#getSize()
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
	 * @see org.xbrlapi.networks.Networks#getArcRoles()
	 */
	public List<String> getArcRoles() throws XBRLException {
		List<String> roles = new LinkedList<String>();
		for (String value: networks.keySet()) {
			roles.add(value);
		}
		return roles;
	}
	
	/**
	 * @see org.xbrlapi.networks.Networks#getLinkRoles(String)
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
	
    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Network> iterator() {
        Set<Network> set = new HashSet<Network>();
        for (HashMap<String,Network> map: networks.values()) {
            set.addAll(map.values());
        }
        return set.iterator();
    }

    /**
     * @see Networks#getStore()
     */
    public Store getStore() {
        return this.store;
    }
    
}
