package org.xbrlapi.networks;


import org.xbrlapi.Relationship;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface EquivalentRelationships {
	
    /**
     * @return the number of equivalent relationships in the set.
     */
    public int size();
    
	/**
	 * @param relationship The relationship to add.
	 * @throws XBRLException if the relationship is not equivalent to 
	 * the relationship with the highest priority in the collection.
	 */
	public void addRelationship(Relationship relationship) throws XBRLException;
	
	/**
	 * @return the active relationship in the collection of 
	 * equivalent relationships.  This is any one of the relationships
	 * that has the maximum priority out of the set of relationships in the
	 * collection. If there are no relationships in the collection, return null.
	 * @throws XBRLException
	 */
	public Relationship getActiveRelationship() throws XBRLException;
	
}
