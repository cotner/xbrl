package org.xbrlapi.networks;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.Relationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.RelationshipImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NetworksImpl implements Networks, Serializable {

	private final static Logger logger = Logger.getLogger(NetworksImpl.class);	
	
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
			Map<URI,Network> map = networks.get(arcrole);
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
	 * @see org.xbrlapi.networks.Networks#getSources(String, URI)
	 */
	public <F extends Fragment> List<F>  getSources(String targetIndex, URI arcrole) throws XBRLException {
		
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
	 * @see org.xbrlapi.networks.Networks#getSources(String, URI, URI)
	 */
	public <F extends Fragment> List<F>  getSources(String targetIndex, URI linkRole, URI arcrole) throws XBRLException {
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
	public <F extends Fragment> List<F>  getTargets(String sourceIndex, URI linkRole, URI arcrole) throws XBRLException {
		
		List<F> fragments = new Vector<F>();
		if (! hasNetwork(linkRole, arcrole)) return fragments;
    	Network network = this.getNetwork(linkRole, arcrole);
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
			Map<URI,Network> map = networks.get(arcrole);
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
		logger.debug("A new network is required for relationship with link role " + linkRole + " and arcrole " + arcrole);
		Network network = new NetworkImpl(getStore(),linkRole,arcrole);
		network.addRelationship(relationship);
		addNetwork(network);
	}
	
    /**
     * @see Networks#addRelationships(URI)
     */
    public void addRelationships(URI arcrole) throws XBRLException {

        String query = "#roots#[@type='org.xbrlapi.impl.ArcImpl' and */*[@xlink:type='arc' and @xlink:arcrole='"+ arcrole +"']]";
        List<Arc> arcs = this.getStore().<Arc>queryForXMLResources(query);
        for (Arc arc: arcs) {
            List<ArcEnd> sources = arc.getSourceFragments();
            List<ArcEnd> targets = arc.getTargetFragments();
            for (Fragment source: sources) {
                Fragment s = source;
                if (source.isa("org.xbrlapi.impl.LocatorImpl")) s = ((Locator) source).getTarget();
                for (Fragment target: targets) {
                    Fragment t = target;
                    if (target.isa("org.xbrlapi.impl.LocatorImpl")) t = ((Locator) target).getTarget();
                    this.addRelationship(new RelationshipImpl(arc,s,t));
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
			Map<URI,Network> map = networks.get(arcrole);
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
        for (Map<URI,Network> map: networks.values()) {
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
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arcs == null) ? 0 : arcs.hashCode());
        result = prime * result
                + ((networks == null) ? 0 : networks.hashCode());
        result = prime * result + ((store == null) ? 0 : store.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NetworksImpl other = (NetworksImpl) obj;
        if (arcs == null) {
            if (other.arcs != null)
                return false;
        } else if (!arcs.equals(other.arcs))
            return false;
        if (networks == null) {
            if (other.networks != null)
                return false;
        } else if (!networks.equals(other.networks))
            return false;
        if (store == null) {
            if (other.store != null)
                return false;
        } else if (!store.equals(other.store))
            return false;
        return true;
    }

    /**
     * @see Networks#addRelationships(Collection)
     */
    public void addRelationships(Collection<Relationship> relationships) throws XBRLException {
        for (Relationship relationship: relationships) 
            this.addRelationship(relationship);
    }

    /**
     * @see org.xbrlapi.networks.Networks#getActiveRelationships()
     */
    public List<Relationship> getActiveRelationships() throws XBRLException {
        List<Relationship> result = new Vector<Relationship>();
        for (Network network: this) {
            result.addAll(network.getAllActiveRelationships());
        }
        return result;
    }
     
    
}
