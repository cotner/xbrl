package org.xbrlapi.networks;

import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;


import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class EquivalentRelationshipsImpl implements EquivalentRelationships {

	SortedMap<Integer,Relationship> relationships = new TreeMap<Integer,Relationship>();

    /**
     * @see org.xbrlapi.networks.EquivalentRelationships#getLength()
     */
	public int getLength() {
	    return relationships.size();
	}
	
	/**
	 * @see org.xbrlapi.networks.EquivalentRelationships#addRelationship(Relationship)
	 */
	public void addRelationship(Relationship relationship) throws XBRLException {
		
		if (getActiveRelationship() == null) {
			relationships.put(relationship.getPriority(),relationship);
			return;
		}
		
		if (! relationship.getSemanticKey().equals(getSemanticKey())) {
			throw new XBRLException("The new relationship is not semantically equal to the existing relationships.");			
		}

		if (! relationship.getSourceIndex().equals(getSourceIndex())) {
			throw new XBRLException("The new relationship starts from a different fragment.");
		}

		if (! relationship.getTargetIndex().equals(getTargetIndex())) {
			throw new XBRLException("The new relationship ends at a different fragment.");
		}
		
		relationships.put(relationship.getPriority(),relationship);

	}

	/**
	 * @see org.xbrlapi.networks.EquivalentRelationships#getActiveRelationship()
	 */
	public Relationship getActiveRelationship() throws XBRLException {
		Integer key = null;
		try {
			key = relationships.lastKey();
		} catch (NoSuchElementException e) {
			return null;
		}
		return relationships.get(key);
	}
	
	/**
	 * @return The semantic key that is the same for all relationships in the 
	 * collection, or null if the collection contains no relationships.
	 * @throws XBRLException
	 */
	private String getSemanticKey() throws XBRLException {
		Relationship r = getActiveRelationship();
		if (r == null) return null;
		return r.getSemanticKey();
	}
	
	/**
	 * @return The index of the source fragment null if the collection 
	 * contains no relationships.
	 * @throws XBRLException
	 */
	private String getSourceIndex() throws XBRLException {
		Relationship r = getActiveRelationship();
		if (r == null) return null;
		return r.getSourceIndex();
	}
	
	/**
	 * @return The index of the target fragment null if the collection 
	 * contains no relationships.
	 * @throws XBRLException
	 */
	private String getTargetIndex() throws XBRLException {
		Relationship r = getActiveRelationship();
		if (r == null) return null;
		return r.getTargetIndex();
	}	
	
}
