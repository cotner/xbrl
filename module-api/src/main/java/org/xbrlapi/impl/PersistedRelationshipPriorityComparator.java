package org.xbrlapi.impl;

import java.util.Comparator;

import org.xbrlapi.PersistedRelationship;

/**
 * Facilitates sorting of relationships by arc order.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class PersistedRelationshipPriorityComparator implements Comparator<PersistedRelationship> {

	/**
	 * Persisted relationships with lower priority values come later in the ordering.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(PersistedRelationship relationship1, PersistedRelationship relationship2) throws ClassCastException {
            return - relationship1.getPriority().compareTo(relationship2.getPriority());
            
	}
	

}
