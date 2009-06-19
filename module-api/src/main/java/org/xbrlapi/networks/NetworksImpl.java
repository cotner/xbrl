package org.xbrlapi.networks;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NetworksImpl implements Networks {

	protected static Logger logger = Logger.getLogger(NetworksImpl.class);	
	
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
	private HashMap<URI,HashMap<URI,Network>> networks = new HashMap<URI,HashMap<URI,Network>>();
	
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
		URI arcrole = network.getArcrole();
		URI linkRole = network.getLinkRole();
		
		HashMap<URI,Network> map = null;
		
		if (networks.containsKey(arcrole)) {
			map = networks.get(arcrole);
			if (map.containsKey(linkRole)) {
			    Network existingNetwork = this.getNetwork(linkRole,arcrole);
			    existingNetwork.add(network);
			}
			map.put(linkRole,network);
			return;
		}
		
		map = new HashMap<URI,Network>();
		map.put(linkRole,network);
		networks.put(arcrole,map);		
		
	}

	/**
	 * @see org.xbrlapi.networks.Networks#getNetwork(URI, URI)
	 */
	public Network getNetwork(URI linkRole, URI arcrole)
			throws XBRLException {
		if (networks.containsKey(arcrole)) {
			HashMap<URI,Network> map = networks.get(arcrole);
			if (map.containsKey(linkRole))
				return map.get(linkRole);
		}
		return null;
	}
	
	/**
	 * @see org.xbrlapi.networks.Networks#getNetworks(URI)
	 */
	public List<Network> getNetworks(URI arcrole) throws XBRLException {
		
		List<Network> selectedNetworks = new LinkedList<Network>();
		List<URI> linkRoles = getLinkRoles(arcrole);
		if (linkRoles.isEmpty()) return selectedNetworks;
		for (URI linkRole: linkRoles) {
			selectedNetworks.add(this.getNetwork(linkRole,arcrole));
		}
		return selectedNetworks;
	}
	
	/**
	 * @see org.xbrlapi.networks.Networks#getSourceFragments(String, URI)
	 */
	public <F extends Fragment> List<F>  getSourceFragments(String targetIndex, URI arcrole) throws XBRLException {
		
		List<F> fragments = new Vector<F>();
		
    	List<Network> selectedNetworks = this.getNetworks(arcrole);
    	for (Network network: selectedNetworks) {
    		SortedSet<Relationship> relationships = network.getActiveRelationshipsTo(targetIndex);
        	for (Relationship relationship: relationships) {
        		fragments.add(relationship.<F>getSource());
        	}
    	}
		return fragments;
	}
	
	/**
	 * @see org.xbrlapi.networks.Networks#getSourceFragments(String, URI, URI)
	 */
	public <F extends Fragment> List<F>  getSourceFragments(String targetIndex, URI linkRole, URI arcrole) throws XBRLException {
		List<F> fragments = new Vector<F>();
		if (! hasNetwork(linkRole,arcrole)) return fragments;
    	Network network = this.getNetwork(linkRole,arcrole);
		SortedSet<Relationship> relationships = network.getActiveRelationshipsTo(targetIndex);
    	for (Relationship relationship: relationships) {
    		fragments.add(relationship.<F>getSource());
    	}
		return fragments;
	}	
	
	/**
	 * @see org.xbrlapi.networks.Networks#getTargets(String, URI)
	 */
	public <F extends Fragment> List<F>  getTargets(String sourceIndex, URI arcrole) throws XBRLException {
		
		List<F> fragments = new Vector<F>();
		
    	List<Network> selectedNetworks = this.getNetworks(arcrole);
    	logger.debug("There are " + selectedNetworks.size() + " networks with arcrole " + arcrole);
    	for (Network network: selectedNetworks) {
            logger.debug("A network has linkrole " + network.getLinkRole());
    		SortedSet<Relationship> relationships = network.getActiveRelationshipsFrom(sourceIndex);
            logger.debug("The network contains " + relationships.size() + " relationships from " + sourceIndex);
        	for (Relationship relationship: relationships) {
        		fragments.add(relationship.<F>getTarget());
        	}
    	}
		return fragments;
	}
	
	/**
	 * @see org.xbrlapi.networks.Networks#getTargets(String, URI, URI)
	 */
	public <F extends Fragment> List<F>  getTargets(String sourceIndex, URI arcRole, URI linkRole) throws XBRLException {
		
		List<F> fragments = new Vector<F>();
		if (! hasNetwork(linkRole, arcRole)) return fragments;
    	Network network = this.getNetwork(linkRole, arcRole);
		SortedSet<Relationship> relationships = network.getActiveRelationshipsFrom(sourceIndex);
    	for (Relationship relationship: relationships) {
    		fragments.add(relationship.<F>getTarget());
    	}
		return fragments;
	}	

	/**
	 * @see org.xbrlapi.networks.Networks#hasNetwork(URI, URI)
	 */
	public boolean hasNetwork(URI linkRole, URI arcrole) {
		if (networks.containsKey(arcrole)) {
			HashMap<URI,Network> map = networks.get(arcrole);
			if (map.containsKey(linkRole)) return true;
		}
		return false;
	}
	
	
	/**
	 * @see Networks#addRelationship(Relationship)
	 */
	public void addRelationship(Relationship relationship) throws XBRLException {
		logger.debug("Networks being augmented with relationship: " + relationship.toString());
		URI arcrole = relationship.getArcrole();
		URI linkRole = relationship.getLinkRole();
		
		if (hasNetwork(linkRole,arcrole)) {
			getNetwork(linkRole,arcrole).addRelationship(relationship);
			return;
		}
		logger.debug("A new network is required for relationship with linkrole " + linkRole + " and arcrole " + arcrole);
		Network network = new NetworkImpl(getStore(),linkRole,arcrole);
		network.addRelationship(relationship);
		addNetwork(network);
	}
	
    /**
     * @see Networks#addRelationships(String)
     */
    public void addRelationships(String arcrole) throws XBRLException {

        String query = "#roots#[@type='org.xbrlapi.impl.ArcImpl' and */*[@xlink:type='arc' and @xlink:arcrole='"+ arcrole +"']]";
        List<Arc> arcs = this.getStore().<Arc>query(query);
        for (Arc arc: arcs) {
            List<ArcEnd> sources = arc.getSourceFragments();
            List<ArcEnd> targets = arc.getTargetFragments();
            for (Fragment source: sources) {
                Fragment s = source;
                if (source.isa("org.xbrlapi.impl.LocatorImpl")) s = ((Locator) source).getTarget();
                for (Fragment target: targets) {
                    Fragment t = target;
                    if (target.isa("org.xbrlapi.impl.LocatorImpl")) t = ((Locator) target).getTarget();
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
		List<URI> arcroles = this.getArcroles();
		for (URI arcrole: arcroles) {
			List<URI> linkroles = getLinkRoles(arcrole);
			size += linkroles.size();
		}
		return size;
	}
	
	
	/**
	 * @see org.xbrlapi.networks.Networks#getArcroles()
	 */
	public List<URI> getArcroles() throws XBRLException {
		List<URI> roles = new Vector<URI>();
		for (URI value: networks.keySet()) {
			roles.add(value);
		}
		return roles;
	}
	
	/**
	 * @see org.xbrlapi.networks.Networks#getLinkRoles(URI)
	 */
	public List<URI> getLinkRoles(URI arcrole) throws XBRLException {
		List<URI> roles = new Vector<URI>();

		if (networks.containsKey(arcrole)) {
			HashMap<URI,Network> map = networks.get(arcrole);
			if (map == null) return roles;
			for (URI value: map.keySet()) {
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
        for (HashMap<URI,Network> map: networks.values()) {
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
 
    /**
     * @see Networks#addAll(Networks)
     */
    public void addAll(Networks networks) throws XBRLException {
        for (Network network: networks) {
            this.addNetwork(network);
        }
    }

    /**
     * @see org.xbrlapi.networks.Networks#complete()
     */
    public void complete() throws XBRLException {
        for (Network network: this) {
            network.complete();
        }
    }
    
}
