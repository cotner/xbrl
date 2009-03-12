package org.xbrlapi.networks;

import java.util.Comparator;

import org.xbrlapi.utilities.XBRLException;

/**
 * Facilitates sorting of relationships by arc order.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class RelationshipOrderComparator implements Comparator<Relationship> {

	/**
	 * Relationships with lower order values come first in the ordering.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Relationship relationship1, Relationship relationship2) throws ClassCastException {
		try {
			return relationship1.getArc().getOrder().compareTo(relationship2.getArc().getOrder());
		} catch (XBRLException e) {
			throw new ClassCastException("The arc order could not be determined to support relationship order comparison. " + e.getMessage());
		}
	}

}
