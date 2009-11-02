package org.xbrlapi.networks;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.Relationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.LocatorImpl;
import org.xbrlapi.impl.RelationshipImpl;
import org.xbrlapi.impl.RelationshipOrderComparator;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NetworkImpl implements Network, Serializable {

	private final static Logger logger = Logger.getLogger(NetworkImpl.class);	
	
	/**
	 * The link role for the network.
	 */
	private URI linkRole = null;
	
	/**
	 * The arcrole for the network.
	 */
	private URI arcRole = null;

	/**
	 * The data store to retrieve fragments if they are not already retrieved.
	 */
	private Store store = null;
		
	private Set<Relationship> relationships = new HashSet<Relationship>();
	
	/**
	 * The map of fragments involved in the relationships in the network.
	 */
	private HashMap<String,Fragment> fragments = new HashMap<String,Fragment>();

	/**
	 * The map from sources to relationships. The maps are indexed
	 * first by the source fragment index and then by the semantic key 
	 * for the relationship.
	 */
	private HashMap<String,HashMap<String,EquivalentRelationships>> sourceRelationships = new HashMap<String,HashMap<String,EquivalentRelationships>>();
	
	/**
	 * The map from targets to relationships.  The maps are indexed
	 * first by the target fragment index and then by the semantic key 
	 * for the relationship.
	 */
	private HashMap<String,HashMap<String,EquivalentRelationships>> targetRelationships = new HashMap<String,HashMap<String,EquivalentRelationships>>();
	
	/**
	 * @param store The data store.
	 * @param linkRole The link role defining the network.
	 * @param arcrole The arc role defining the network.
	 * @throws XBRLException if the data store is null.
	 */
	public NetworkImpl(Store store, URI linkRole, URI arcrole) throws XBRLException {
		super();
		if (store == null) throw new XBRLException("The store must not be null.");
        if (linkRole == null) throw new XBRLException("The link role must not be null.");
        if (arcrole == null) throw new XBRLException("The arcrole must not be null.");
		setStore(store);
		setLinkRole(linkRole);
		setArcrole(arcrole);
	}

	/**
	 * @see org.xbrlapi.networks.Network#getArcrole()
	 */
	public URI getArcrole() {
		return arcRole;
	}

	/**
	 * @see org.xbrlapi.networks.Network#setArcrole(URI)
	 */
	public void setArcrole(URI arcrole) throws XBRLException {
		 if (arcrole == null) throw new XBRLException("The network arcrole must not be set to null");
		this.arcRole = arcrole;
	}

	/**
	 * @see org.xbrlapi.networks.Network#getLinkRole()
	 */
	public URI getLinkRole() {
		return linkRole;
	}

	/**
	 * @see org.xbrlapi.networks.Network#setLinkRole(URI)
	 */
	public void setLinkRole(URI linkRole) throws XBRLException {
		 if (linkRole == null) throw new XBRLException("The network link role must not be set to null");
		this.linkRole = linkRole;
	}
		
	/** 
	 * @see org.xbrlapi.networks.Network#hasFragment(String)
	 */
	public boolean hasFragment(String index) throws XBRLException {
		if (fragments.containsKey(index)) return true;
		return false;
	}
	
	/** 
	 * @see org.xbrlapi.networks.Network#get(String)
	 */
	public Fragment get(String index) throws XBRLException {
		Fragment fragment = fragments.get(index);
		if (fragment == null) {
			if (getStore() == null) return null;
			fragment = getStore().<Fragment>getXMLResource(index);
			fragments.put(index,fragment);
		}
		return fragment;
	}

	/**
	 * @see org.xbrlapi.networks.Network#getRootFragments()
	 */
	@SuppressWarnings("unchecked")
	public <F extends Fragment> List<F> getRootFragments() {
		List<F> fragmentList = new Vector<F>();
		Set<String> rootIndexes = getRootFragmentIndexes();
		for (String index: rootIndexes) {
			fragmentList.add((F) this.fragments.get(index));
		}
		return fragmentList;
	}
	
	/**
	 * @see org.xbrlapi.networks.Network#getRootFragmentIndexes()
	 */
	public Set<String> getRootFragmentIndexes() {
		Set<String> sourceIndexes = sourceRelationships.keySet();
		Set<String> targetIndexes = targetRelationships.keySet();
		Set<String> rootIndexes = new HashSet<String>(sourceIndexes);
		rootIndexes.removeAll(targetIndexes);
		return rootIndexes;
	}

	/**
	 * Add the fragment to the set of fragments participating in 
	 * the network.  Initialises the store property if it has
	 * not already been done.
	 * @param fragment The fragment to add.
	 */
	private void add(Fragment fragment) {
		if (store == null) store = fragment.getStore();
		fragments.put(fragment.getIndex(),fragment);
	}
	
	/**
	 * @see Network#getStore()
	 */
	public Store getStore() {
		return store;
	}
	
	/**
	 * @param store The data store for the network.
	 * @throws XBRLException if the data store is null.
	 */
	private void setStore(Store store) throws XBRLException {
		if (store == null) throw new XBRLException("The data store for the network must not be null.");
		this.store = store;
	}
	
	/**
	 * @see org.xbrlapi.networks.Network#addRelationship(Relationship)
	 */
	public void addRelationship(Relationship relationship) throws XBRLException {
		
		// Ensure that the relationship belongs
		if (! getLinkRole().equals(relationship.getLinkRole())) throw new XBRLException("The network link role does not match that of the relationship.");
		if (! getArcrole().equals(relationship.getArcrole())) throw new XBRLException("The network arc role does not match that of the relationship.");

		relationships.add(relationship);
		
        String semanticKey = relationship.getSignature();
        Fragment source = relationship.getSource();
        String sourceIndex = source.getIndex();
		
		// Make sure the relationship is not already in the network.
		String targetsSemanticKey = semanticKey + sourceIndex;
		if (this.targetRelationships.containsKey(targetsSemanticKey)) {
		    return;// The relationship is already recorded in the network
		}

        Fragment target = relationship.getTarget();
        String targetIndex = target.getIndex();
        String sourcesSemanticKey = semanticKey + targetIndex;

        if (! fragments.containsKey(targetIndex)) fragments.put(targetIndex,target);
        if (! fragments.containsKey(sourceIndex)) fragments.put(sourceIndex,source);
		
		// Store the fragments in the relationship
		Arc arc = relationship.getArc();
		String arcIndex = arc.getIndex();
		if (! fragments.containsKey(arcIndex)) fragments.put(arcIndex,arc);
		
		ExtendedLink link = relationship.getExtendedLink();
		String linkIndex = arc.getIndex();
		if (! fragments.containsKey(linkIndex)) fragments.put(linkIndex,link);
		
		HashMap<String,EquivalentRelationships> fragmentRelationships = null;
		EquivalentRelationships er = null;
		if (sourceRelationships.containsKey(sourceIndex)) {
			fragmentRelationships = sourceRelationships.get(sourceIndex);
			if (fragmentRelationships.containsKey(sourcesSemanticKey)) {
				er = fragmentRelationships.get(sourcesSemanticKey);
			} else {
				er = new EquivalentRelationshipsImpl();
	            fragmentRelationships.put(sourcesSemanticKey,er);
			}
		} else {
			fragmentRelationships = new HashMap<String,EquivalentRelationships>();
			er = new EquivalentRelationshipsImpl();
			fragmentRelationships.put(sourcesSemanticKey,er);
			sourceRelationships.put(sourceIndex,fragmentRelationships);
		}
		er.addRelationship(relationship);

		if (targetRelationships.containsKey(targetIndex)) {
			fragmentRelationships = targetRelationships.get(targetIndex);
			if (fragmentRelationships.containsKey(targetsSemanticKey)) {
				er = fragmentRelationships.get(targetsSemanticKey);
			} else {
				er = new EquivalentRelationshipsImpl();
                fragmentRelationships.put(targetsSemanticKey,er);
			}
		} else {
			fragmentRelationships = new HashMap<String,EquivalentRelationships>();
			er = new EquivalentRelationshipsImpl();
			fragmentRelationships.put(targetsSemanticKey,er);
			targetRelationships.put(targetIndex,fragmentRelationships);
		}
		er.addRelationship(relationship);

	}
	
	/**
	 * @see Network#addRelationships(Collection)
	 */
	public void addRelationships(Collection<Relationship> relationships) throws XBRLException {
    for (Relationship relationship: relationships) {
        this.addRelationship(relationship);
    }
}   	
	
	/**
	 * @return a String that will be identical the 
	 * same source and the same target.
	 * @throws XBRLException
	 */	
	private String getEndFragmentsKey(String source, String target) {
		return source + "|" + target;
	}	
	
	/**
	 * @see org.xbrlapi.networks.Network#getActiveRelationshipsFrom(String)
	 */
	public SortedSet<Relationship> getActiveRelationshipsFrom(String index) throws XBRLException {

	    logger.debug("Getting active relationships from " + index + " for " + this);
	    
		SortedSet<Relationship> activeRelationships = new TreeSet<Relationship>(new RelationshipOrderComparator());

		if (! sourceRelationships.containsKey(index)) return activeRelationships;
		
		// Sort the relationships based on their order attribute.
		HashMap<String,EquivalentRelationships> sr = sourceRelationships.get(index);
		logger.debug("There are " + sr.size() + " source relationships.");
		for (EquivalentRelationships er: sr.values()) {
            activeRelationships.add(er.getActiveRelationship());
		}
		logger.debug("Returning " + activeRelationships.size() + " active relationships.");
		
		return activeRelationships;
	}
	
	/**
	 * @see Network#hasSingleParent(String)
	 */
    public boolean hasSingleParent(String index) throws XBRLException {
        return (this.getActiveRelationshipsTo(index).size() == 1);
    }
    
    /**
     * @see Network#isRoot(String)
     */
    public boolean isRoot(String index) throws XBRLException {
        return (this.getActiveRelationshipsTo(index).size() == 0);
    }
    
    /**
     * @see Network#isLeaf(String)
     */
    public boolean isLeaf(String index) throws XBRLException {
        return (this.getActiveRelationshipsFrom(index).size() == 0);
    }	
	
    /**
     * @see org.xbrlapi.networks.Network#hasActiveRelationshipsFrom(String)
     */
    public boolean hasActiveRelationshipsFrom(String index) {
        return sourceRelationships.containsKey(index);
    }
    
    /**
     * @see org.xbrlapi.networks.Network#hasActiveRelationshipsTo(String)
     */
    public boolean hasActiveRelationshipsTo(String index) {
        return (targetRelationships.containsKey(index));
    }
    
	
	/**
	 * @see org.xbrlapi.networks.Network#getActiveRelationshipsTo(String)
	 */
	public SortedSet<Relationship> getActiveRelationshipsTo(String index) throws XBRLException {

        logger.debug("Getting active relationships to " + index + " for " + this);
        
        SortedSet<Relationship> activeRelationships = new TreeSet<Relationship>(new RelationshipOrderComparator());

        if (! targetRelationships.containsKey(index)) return activeRelationships;
        
        // Sort the relationships based on their order attribute.
        HashMap<String,EquivalentRelationships> tr = targetRelationships.get(index);
        logger.debug("There are " + tr.size() + " target relationships.");
        for (EquivalentRelationships equivalentRelationships: tr.values()) {
            activeRelationships.add(equivalentRelationships.getActiveRelationship());
        }
        
        return activeRelationships;	    
	    
	}

    /**
     * @see Network#getChildren(String)
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> List<F> getChildren(String index) throws XBRLException {
        List<F> children = new Vector<F>();
        for (Relationship relationship: this.getActiveRelationshipsFrom(index)) {
            children.add((F) relationship.getTarget());
        }
        return children;
    }


    /**
     * @see Network#getParents(String)
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> List<F> getParents(String index) throws XBRLException {
        List<F> parents = new Vector<F>();
        for (Relationship relationship: this.getActiveRelationshipsTo(index)) {
            parents.add((F) relationship.getSource());
        }
        return parents;
    }

    /**
     * @see Network#getNumberOfRelationships()
     */
    public int getNumberOfRelationships() {
        int count = 0;
        for (HashMap<String,EquivalentRelationships> map: this.sourceRelationships.values()) {
            for (EquivalentRelationships relationships: map.values()) {
                count += relationships.size();
            }
        }
        return count;
    }

    /**
     * @see Network#getNumberOfActiveRelationships()
     */
    public int getNumberOfActiveRelationships() {
        int count = 0;
        for (HashMap<String,EquivalentRelationships> map: this.sourceRelationships.values()) {
            count += map.size();
        }
        return count;
    }
    
    /**
     * @see Network#complete()
     */
    public void complete() throws XBRLException {
        
        logger.debug("Completing network with arcrole " + this.getArcrole() + " and link role " + getLinkRole());

        if (this.getStore().isPersistingRelationships()) {
            
            Analyser analyser = new AnalyserImpl(getStore());
            List<Relationship> persistedRelationships = analyser.getRelationships(this.getLinkRole(), this.getArcrole());
            for (Relationship persistedRelationship: persistedRelationships) {
                this.addRelationship(persistedRelationship);
            }
            
        } else {
        
            // Get the arcs that define relationships in the network
            List<ExtendedLink> links = getStore().getExtendedLinks(this.getLinkRole());
            for (ExtendedLink link: links) {
                List<Arc> arcs = link.getArcsWithArcrole(this.getArcrole());
                for (Arc arc: arcs) {
                    List<ArcEnd> sources = arc.getSourceFragments();
                    List<ArcEnd> targets = arc.getTargetFragments();
                    for (Fragment source: sources) {
                        Fragment s = source;
                        if (source.isa(LocatorImpl.class)) s = ((Locator) source).getTarget();
                        for (Fragment target: targets) {
                            Fragment t = target;
                            if (target.isa(LocatorImpl.class)) t = ((Locator) target).getTarget();
                            Relationship relationship = new RelationshipImpl(arc,s,t);
                            this.addRelationship(relationship);
                        }
                    }
                }
            }

        }

    }

    /**
     * @see Network#contains(String)
     */
    public boolean contains(String index) {
        return fragments.containsKey(index);
    }
    
    
    /**
     * @see Network#getAllRelationships()
     */
    public List<Relationship> getAllRelationships() throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        relationships.addAll(this.relationships);
        return relationships;
    }
    
    /**
     * @see Network#getAllActiveRelationships()
     */
    public List<Relationship> getAllActiveRelationships() throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        for (String sourceIndex: this.sourceRelationships.keySet()) {
            relationships.addAll(this.getActiveRelationshipsFrom(sourceIndex));
        }
        return relationships;
    }    
    
    /**
     * @see Network#add(Network)
     */
    public void add(Network network) throws XBRLException {
        if (this == network) return;
        for (Relationship relationship: network.getAllRelationships()) {
            if (! this.contains(relationship.getArcIndex())) {
                this.addRelationship(relationship);
            }
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arcRole == null) ? 0 : arcRole.hashCode());
        result = prime * result
                + ((fragments == null) ? 0 : fragments.hashCode());
        result = prime * result
                + ((linkRole == null) ? 0 : linkRole.hashCode());
        result = prime * result
                + ((relationships == null) ? 0 : relationships.hashCode());
        result = prime
                * result
                + ((sourceRelationships == null) ? 0 : sourceRelationships
                        .hashCode());
        result = prime * result + ((store == null) ? 0 : store.hashCode());
        result = prime
                * result
                + ((targetRelationships == null) ? 0 : targetRelationships
                        .hashCode());
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
        NetworkImpl other = (NetworkImpl) obj;
        if (arcRole == null) {
            if (other.arcRole != null)
                return false;
        } else if (!arcRole.equals(other.arcRole))
            return false;
        if (fragments == null) {
            if (other.fragments != null)
                return false;
        } else if (!fragments.equals(other.fragments))
            return false;
        if (linkRole == null) {
            if (other.linkRole != null)
                return false;
        } else if (!linkRole.equals(other.linkRole))
            return false;
        if (relationships == null) {
            if (other.relationships != null)
                return false;
        } else if (!relationships.equals(other.relationships))
            return false;
        if (sourceRelationships == null) {
            if (other.sourceRelationships != null)
                return false;
        } else if (!sourceRelationships.equals(other.sourceRelationships))
            return false;
        if (store == null) {
            if (other.store != null)
                return false;
        } else if (!store.equals(other.store))
            return false;
        if (targetRelationships == null) {
            if (other.targetRelationships != null)
                return false;
        } else if (!targetRelationships.equals(other.targetRelationships))
            return false;
        return true;
    }
    
}
