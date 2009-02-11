package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * Defines the functionality for the custom entity resource:
 * a extended link resource that has a 1:1 association with 
 * a given entity identifier.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface EntityResource extends MixedContentResource {

    /**
     * @return the entity identifier scheme URI 
     * @throws XBRLException
     */
    public String getIdentifierScheme() throws XBRLException;

    /**
     * @return the entity identifier value token
     * @throws XBRLException
     */
    public String getIdentifierValue() throws XBRLException;
    
    
    /**
     * @return a list of all equivalent entity resources
     * @throws XBRLException
     */
    public FragmentList<EntityResource> getEquivalents() throws XBRLException;
    
    /**
     * @return a list of entity identifier fragments from the contexts the
     * data store (or an empty fragment list if there are none
     * matching this entity resource).
     * @throws XBRLException
     */
    public FragmentList<Entity> getEntities() throws XBRLException;

    /**
     * @param uri The URI of the document to get the entities from
     * @return a list of entity identifier fragments from the contexts in
     * the specified document (or an empty fragment list if there are none
     * matching this entity resource).
     * @throws XBRLException
     */
    public FragmentList<Entity> getEntities(URI uri) throws XBRLException;
    
}
