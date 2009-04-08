package org.xbrlapi.networks;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

/**
 * Facilitates sorting of relationships by arc order.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class RelationshipOrderComparator implements Comparator<Relationship> {

    protected static Logger logger = Logger.getLogger(RelationshipOrderComparator.class);
    
	/**
	 * Relationships with lower order values come first in the ordering.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Relationship relationship1, Relationship relationship2) throws ClassCastException {
		try {
		    
		    if (relationship1.getArc().getOrder().doubleValue() != relationship2.getArc().getOrder().doubleValue())
		        return relationship1.getArc().getOrder().compareTo(relationship2.getArc().getOrder());
            
            if (! relationship1.getSourceIndex().equals(relationship2.getSourceIndex()))
                return relationship1.getSourceIndex().compareTo(relationship2.getSourceIndex());
		    
            if (! relationship1.getTargetIndex().equals(relationship2.getTargetIndex()))
                return relationship1.getTargetIndex().compareTo(relationship2.getTargetIndex());

            return relationship1.getSignature().compareTo(relationship2.getSignature());
            
		} catch (XBRLException e) {
			throw new ClassCastException("The arc order could not be determined to support relationship order comparison. " + e.getMessage());
		}
	}

}
