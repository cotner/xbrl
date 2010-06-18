package org.xbrlapi.networks;

import java.io.Serializable;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.xbrlapi.Relationship;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class EquivalentRelationshipsImpl implements EquivalentRelationships, Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 5470514499342023524L;
    private SortedMap<Integer,Relationship> relationships = new TreeMap<Integer,Relationship>();

    /**
     * @see org.xbrlapi.networks.EquivalentRelationships#size()
     */
	public int size() {
	    return relationships.size();
	}
	
	/**
	 * @see org.xbrlapi.networks.EquivalentRelationships#addRelationship(Relationship)
	 */
	public void addRelationship(Relationship relationship) throws XBRLException {
		
		if (getActiveRelationship() == null) {
			relationships.put(relationship.getArcPriority(),relationship);
			return;
		}
		
		if (! relationship.getSignature().equals(getSemanticKey())) {
			throw new XBRLException("The new relationship is not semantically equal to the existing relationships.");			
		}

		if (! relationship.getSourceIndex().equals(getSourceIndex())) {
			throw new XBRLException("The new relationship starts from a different fragment.");
		}

		if (! relationship.getTargetIndex().equals(getTargetIndex())) {
			throw new XBRLException("The new relationship ends at a different fragment.");
		}
		
		relationships.put(relationship.getArcPriority(),relationship);

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
		return r.getSignature();
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

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((relationships == null) ? 0 : relationships.hashCode());
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
        EquivalentRelationshipsImpl other = (EquivalentRelationshipsImpl) obj;
        if (relationships == null) {
            if (other.relationships != null)
                return false;
        } else if (!relationships.equals(other.relationships))
            return false;
        return true;
    }	
	
    public Collection<Relationship> getAllRelationships() {
        return relationships.values();
    }
    
}
