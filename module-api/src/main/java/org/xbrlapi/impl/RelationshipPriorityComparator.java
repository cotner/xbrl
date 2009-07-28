package org.xbrlapi.impl;

import java.util.Comparator;

import org.xbrlapi.Relationship;

/**
 * Facilitates sorting of relationships by arc order.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class RelationshipPriorityComparator implements Comparator<Relationship> {

	/**
	 * Persisted relationships with lower priority values come later in the ordering.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Relationship relationship1, Relationship relationship2) throws ClassCastException {
            return - relationship1.getArcPriority().compareTo(relationship2.getArcPriority());
            
	}
	

}
