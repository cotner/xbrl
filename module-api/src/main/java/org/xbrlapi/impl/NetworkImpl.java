package org.xbrlapi.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.EquivalentRelationships;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Network;
import org.xbrlapi.Relationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NetworkImpl implements Network {

	protected static Logger logger = Logger.getLogger(Loader.class);	
	
	/**
	 * The link role for the network.
	 */
	private String linkRole = null;
	
	/**
	 * The arc role for the network.
	 */
	private String arcRole = null;

	/**
	 * The data store to retrieve fragments if they are not already retrieved.
	 */
	private Store store = null;
		
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
	 * @param linkRole The link role defining the network.
	 * @param arcRole The arc role defining the network.
	 * @throws XBRLException
	 */
	public NetworkImpl(String linkRole, String arcRole) throws XBRLException {
		super();
		setLinkRole(linkRole);
		setArcRole(arcRole);
	}

	/**
	 * @see org.xbrlapi.Network#getArcRole()
	 */
	public String getArcRole() {
		return arcRole;
	}

	/**
	 * @see org.xbrlapi.Network#setArcRole(String)
	 */
	public void setArcRole(String arcRole) throws XBRLException {
		 if (arcRole == null) throw new XBRLException("The network arcrole must not be set to null");
		this.arcRole = arcRole;
	}

	/**
	 * @see org.xbrlapi.Network#getLinkRole()
	 */
	public String getLinkRole() {
		return linkRole;
	}

	/**
	 * @see org.xbrlapi.Network#setLinkRole(String)
	 */
	public void setLinkRole(String linkRole) throws XBRLException {
		 if (linkRole == null) throw new XBRLException("The network link role must not be set to null");
		this.linkRole = linkRole;
	}
		
	/** 
	 * @see org.xbrlapi.Network#hasFragment(String)
	 */
	public boolean hasFragment(String index) throws XBRLException {
		if (fragments.containsKey(index)) return true;
		return false;
	}
	
	/** 
	 * @see org.xbrlapi.Network#getFragment(String)
	 */
	public Fragment getFragment(String index) throws XBRLException {
		Fragment fragment = fragments.get(index);
		if (fragment == null) {
			if (getStore()== null) return null;
			fragment = getStore().getFragment(index);
			fragments.put(index,fragment);
		}
		return fragment;
	}

	/**
	 * @see org.xbrlapi.Network#getRootFragments()
	 */
	@SuppressWarnings("unchecked")
	public <F extends Fragment> FragmentList<F> getRootFragments() {
		FragmentList<F> fragmentList = new FragmentListImpl<F>();
		Set<String> rootIndexes = getRootFragmentIndexes();
		for (String index: rootIndexes) {
			fragmentList.add((F) this.fragments.get(index));
		}
		return fragmentList;
	}
	
	/**
	 * @see org.xbrlapi.Network#getRootFragmentIndexes()
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
	private void addFragment(Fragment fragment) {
		if (store == null) store = fragment.getStore();
		fragments.put(fragment.getFragmentIndex(),fragment);
	}
	
	/**
	 * @return the data store for the network.
	 */
	private Store getStore() {
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
	 * @see org.xbrlapi.Network#addRelationship(org.xbrlapi.Relationship)
	 */
	public void addRelationship(Relationship relationship) throws XBRLException {
		
		// Ensure that the relationship belongs
		if (! getLinkRole().equals(relationship.getLinkRole())) throw new XBRLException("The network link role does not match that of the relationship.");
		
		if (! getArcRole().equals(relationship.getArcRole())) throw new XBRLException("The network arc role does not match that of the relationship.");
		
		// Inform the relationship of the network it is in
		relationship.setNetwork(this);
		
		// Store the fragments in the relationship
		Arc arc = relationship.getArc();
		String arcIndex = arc.getFragmentIndex();
		if (! fragments.containsKey(arcIndex)) fragments.put(arcIndex,arc);
		
		ExtendedLink link = relationship.getLink();
		String linkIndex = arc.getFragmentIndex();
		if (! fragments.containsKey(linkIndex)) fragments.put(linkIndex,link);
		
		Fragment source = relationship.getSource();
		String sourceIndex = source.getFragmentIndex();
		if (! fragments.containsKey(sourceIndex)) fragments.put(sourceIndex,source);

		Fragment target = relationship.getTarget();
		String targetIndex = target.getFragmentIndex();
		if (! fragments.containsKey(targetIndex)) fragments.put(targetIndex,target);

		// Store the relationship itself
		String semanticKey = relationship.getSemanticKey();

		//Integer priority = relationship.getPriority();

		String sourcesSemanticKey = semanticKey + targetIndex;
		String targetsSemanticKey = semanticKey + sourceIndex;
		
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
	 * @return a String that will be identical the 
	 * same source and the same target.
	 * @throws XBRLException
	 */	
	private String getEndFragmentsKey(String source, String target) {
		return source + "|" + target;
	}	
	
	/**
	 * @see org.xbrlapi.Network#getActiveRelationshipsFrom(String)
	 */
	public List<Relationship> getActiveRelationshipsFrom(String index) throws XBRLException {

	    logger.debug("Getting active relationships from " + index + " for " + this);
	    
		List<Relationship> activeRelationships = new LinkedList<Relationship>();

		if (! sourceRelationships.containsKey(index)) return activeRelationships;
		
		HashMap<String,EquivalentRelationships> sr = sourceRelationships.get(index);
		logger.debug("There are " + sr.size() + " source relationships.");
		for (String key: sr.keySet()) {
			EquivalentRelationships er = sr.get(key);
			activeRelationships.add(er.getActiveRelationship());
		}
		return activeRelationships;
	}
	
	/**
	 * @see org.xbrlapi.Network#getActiveRelationshipsTo(String)
	 */
	public List<Relationship> getActiveRelationshipsTo(String index) throws XBRLException {

		List<Relationship> activeRelationships = new LinkedList<Relationship>();

		if (! targetRelationships.containsKey(index)) return activeRelationships;
		
		// Get the map from semantic keys to sets of equivalent relationships
		HashMap<String,EquivalentRelationships> tr = targetRelationships.get(index);

		Iterator<String> iterator = tr.keySet().iterator();
		while (iterator.hasNext()) {
			EquivalentRelationships er = tr.get(iterator.next());
			activeRelationships.add(er.getActiveRelationship());
		}

		return activeRelationships;
	}	
	
}
