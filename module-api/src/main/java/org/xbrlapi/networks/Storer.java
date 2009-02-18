package org.xbrlapi.networks;

import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * Classes implementing this interface support persistance
 * of relationship information in a data store.  This kind
 * of persistence can reduce the query burden associated with
 * network analysis.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface Storer {

    
    /**
     * @param networks The set of networks from 
     * which all active relationships
     * will be persisted in the data store.
     * @see org.xbrlapi.Relationship
     * @throws XBRLException
     */
    public void storeRelationships(Networks networks) throws XBRLException;
    
    /**
     * @param network The network from which all 
     * active relationships will be persisted in the data store.
     * @see org.xbrlapi.Relationship
     * @throws XBRLException
     */
    public void storeRelationships(Network network) throws XBRLException;
    
    /**
     * When the relationship is persisted in the data store, any equivalent
     * relationship will be removed from the data store.  Equivalence is 
     * assessed on the basis of the relationship semantic key.
     * @see org.xbrlapi.networks.Relationship#getSemanticKey()
     * @param relationship The relationship that will be be persisted in the data store.
     * @see org.xbrlapi.Relationship
     * @throws XBRLException
     */
    public void storeRelationship(Relationship relationship) throws XBRLException;
    
}
