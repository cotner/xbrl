package org.xbrlapi.impl;

import java.util.Comparator;

import org.xbrlapi.Relationship;

/**
 * Facilitates sorting of relationships by arc order.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class RelationshipOrderComparator implements Comparator<Relationship> {

	/**
	 * Persisted relationships with lower order values come first in the ordering.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Relationship relationship1, Relationship relationship2) throws ClassCastException {

        if (relationship1.getArcOrder().doubleValue() != relationship2.getArcOrder().doubleValue()) 
            return relationship1.getArcOrder().compareTo(relationship2.getArcOrder());

        if (! relationship1.getSignature().equals(relationship2.getSignature())) 
            return relationship1.getSignature().compareTo(relationship2.getSignature());

        if (! relationship1.getSourceIndex().equals(relationship2.getSourceIndex()))
            return relationship1.getSourceIndex().compareTo(relationship2.getSourceIndex());
        
        if (! relationship1.getTargetIndex().equals(relationship2.getTargetIndex()))
            return relationship1.getTargetIndex().compareTo(relationship2.getTargetIndex());

        if (! relationship1.getArcrole().equals(relationship2.getArcrole()))
            return relationship1.getArcrole().compareTo(relationship2.getArcrole());

        if (! relationship1.getLinkRole().equals(relationship2.getLinkRole()))
            return relationship1.getLinkRole().compareTo(relationship2.getLinkRole());

        return 0;

	}
	

}
