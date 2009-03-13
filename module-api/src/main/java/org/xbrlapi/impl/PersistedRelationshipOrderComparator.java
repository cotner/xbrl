package org.xbrlapi.impl;

import java.util.Comparator;

import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.networks.RelationshipImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * Facilitates sorting of relationships by arc order.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class PersistedRelationshipOrderComparator implements Comparator<PersistedRelationship> {

	/**
	 * Persisted relationships with lower order values come first in the ordering.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(PersistedRelationship relationship1, PersistedRelationship relationship2) throws ClassCastException {
		
	    if (relationship1.getArcOrder().doubleValue() != relationship2.getArcOrder().doubleValue()) 
	        return relationship1.getArcOrder().compareTo(relationship2.getArcOrder());

        if (! relationship1.getSourceIndex().equals(relationship2.getSourceIndex()))
            return relationship1.getSourceIndex().compareTo(relationship2.getSourceIndex());
        
        if (! relationship1.getTargetIndex().equals(relationship2.getTargetIndex()))
            return relationship1.getTargetIndex().compareTo(relationship2.getTargetIndex());

        try {
            Relationship r1 = new RelationshipImpl(relationship1);
            Relationship r2 = new RelationshipImpl(relationship2);
            return r1.getSemanticKey().compareTo(r2.getSemanticKey());	

        } catch (XBRLException e) {
            throw new ClassCastException("The comparison of the arc semantics failed.");
        }
	}

}
