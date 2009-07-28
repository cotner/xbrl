package org.xbrlapi.networks;

import java.net.URI;
import java.util.Collection;

import org.xbrlapi.Arc;
import org.xbrlapi.Fragment;
import org.xbrlapi.Relationship;
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
     * @param documents The collection of URIs of the documents to store the relationships for.
     * Relationships are sought among the XLink arcs in the documents.
     * This is used by the discover to updated persisted relationships
     * in the data store.
     * @throws XBRLException
     */
    public void storeRelationships(Collection<URI> documents) throws XBRLException;
    
    /**
     * @param document The URI of the document to store the relationships for.
     * Relationships are sought among the XLink arcs in the documents.
     * This is used by the discover to updated persisted relationships
     * in the data store.
     * @throws XBRLException
     */
    public void storeRelationships(URI document) throws XBRLException;    
    
    /**
     * @param network The network from which all 
     * relationships (active and inactive) will be 
     * persisted in the data store.
     * @see org.xbrlapi.Relationship
     * @throws XBRLException
     */
    public void storeRelationships(Network network) throws XBRLException;
    
    /**
     * When the relationship is persisted in the data store, any equivalent
     * relationship will be removed from the data store.  Equivalence is 
     * assessed on the basis of the relationship semantic key.
     * @see org.xbrlapi.Relationship#getSignature()
     * @param relationship The relationship that will be be persisted in the data store.
     * @throws XBRLException
     */
    public void storeRelationship(Relationship relationship) throws XBRLException;

    /**
     * Persist the relationship defined by the given fragments.
     * @param arc The relationship arc
     * @param source The relationship source fragment
     * @param target The relationship target fragment
     * @throws XBRLException
     */
    public void storeRelationship(Arc arc, Fragment source, Fragment target) throws XBRLException;
    
    
    /**
     * Stores all active relationships in the entire data store.
     * @throws XBRLException
     */
    public void storeAllRelationships() throws XBRLException;
    
    /**
     * Deletes the persisted relationships with the given link and arc role from 
     * the data store.  Note that this has no impact on the document fragments
     * stored in the data store.
     * @param linkRole The linkrole for relationships to be deleted.
     * @param arcrole The arcrole for relationships to be deleted.
     * @throws XBRLException
     */
    public void deleteRelationships(URI linkRole, URI arcrole) throws XBRLException;
    
    /**
     * Deletes the persisted relationships that are related to arcs 
     * in the specified document.
     * @param document The URI of the document to delete the persisted
     * relationships for.
     * @throws XBRLException
     */
    public void deleteRelationships(URI document) throws XBRLException;    
    
    /**
     * Deletes all persisted relationships in the data store.
     * @throws XBRLException
     */
    public void deleteRelationships() throws XBRLException;
    
    /**
     * Removes persisted prohibited or overridden relationships from a network.
     * @param linkRole The linkRole of the network to purge.
     * @param arcrole The arcrole of the network to purge.
     * @throws XBRLException
     */
    public void deleteInactiveRelationships(URI linkRole, URI arcrole) throws XBRLException;

    /**
     * Removes all persisted prohibited or overridden relationships from a network.
     * @throws XBRLException
     */
    public void deleteInactiveRelationships() throws XBRLException;

}
