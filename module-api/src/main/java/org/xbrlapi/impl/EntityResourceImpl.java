package org.xbrlapi.impl;

import java.net.URI;
import java.util.HashMap;

import org.xbrlapi.Entity;
import org.xbrlapi.EntityResource;
import org.xbrlapi.FragmentList;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Entity resource implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class EntityResourceImpl extends MixedContentResourceImpl implements EntityResource {

    /**
     * @see org.xbrlapi.EntityResource#getIdentifierScheme()
     */
    public String getIdentifierScheme() throws XBRLException {
        return this.getDataRootElement().getAttribute("scheme");
    }    

    /**
     * @see org.xbrlapi.EntityResource#getIdentifierValue()
     */
    public String getIdentifierValue() throws XBRLException {
        return this.getDataRootElement().getAttribute("value");
    }

    /**
     * @see org.xbrlapi.EntityResource#getEquivalents()
     */
    public FragmentList<EntityResource> getEquivalents() throws XBRLException { 
        logger.debug("Getting equivalents to " + this.getStringIdentifier());
        HashMap<String,EntityResource> map = new HashMap<String,EntityResource>();
        getEquivalentsMap(map);
        FragmentList<EntityResource> result = new FragmentListImpl<EntityResource>();
        for (EntityResource entity: map.values()) {
            result.add(entity);
        }
        return result;        
    }    
    
    /**
     * @return a string identifier for the entity resource
     * @throws XBRLException
     */
    protected String getStringIdentifier() throws XBRLException {
        return this.getIdentifierScheme() + ":" + this.getIdentifierValue();
    }

    /**
     * @return a list of entity resources that are parent or children 
     * of equivalent-entity relationships to this entity resource.
     * @throws XBRLException
     */
    protected FragmentList<EntityResource> getDirectEquivalents() throws XBRLException {
        Networks networks = this.getNetworks();
        FragmentList<EntityResource> result = networks.<EntityResource>getTargetFragments(this.getIndex(),Constants.XBRLAPIEquivalentEntitiesArcrole);
        result.addAll(networks.<EntityResource>getSourceFragments(this.getIndex(),Constants.XBRLAPIEquivalentEntitiesArcrole));
        return result;
    }
    
    /**
     * Augments a map of equivalent entities
     * @throws XBRLException
     */
    protected void getEquivalentsMap(HashMap<String,EntityResource> map) throws XBRLException {

        logger.debug("Getting equivalents map for " + this.getStringIdentifier());
        
        String id = this.getStringIdentifier();
        if (map.isEmpty()) {
            map.put(id,this);
        } else {
            if(! map.containsKey(id)) {
                logger.debug("Adding " + id + " to the equivalents map.");
                map.put(id,this);
            }
        }
        
        FragmentList<EntityResource> directEquivalents = this.getDirectEquivalents();
        logger.debug(id + " has " + directEquivalents.getLength() + " direct equivalents.");
        for (EntityResource candidate: directEquivalents) {
            EntityResourceImpl impl = (EntityResourceImpl) candidate;
            if(! map.containsKey(impl.getStringIdentifier())) {
                impl.getEquivalentsMap(map);
            }
        }
    }
    
    /**
     * @see org.xbrlapi.EntityResource#getEntities()
     */
    public FragmentList<Entity> getEntities() throws XBRLException {
        String query = "/*[@type='org.xbrlapi.impl.EntityImpl' and */*/*[@scheme='" + this.getIdentifierScheme() + "' and .='" + this.getIdentifierValue() + "']]";
        FragmentList<Entity> entities = getStore().<Entity>query(query);
        return entities;
    }

    /**
     * @see org.xbrlapi.EntityResource#getEntities(URI)
     */
    public FragmentList<Entity> getEntities(URI uri) throws XBRLException {
        URI matchURI = getStore().getMatcher().getMatch(uri);
        String query = "/*[@type='org.xbrlapi.impl.EntityImpl' and @uri='" + matchURI + "' and */*/*[@scheme='" + this.getIdentifierScheme() + "' and .='" + this.getIdentifierValue() + "']]";
        FragmentList<Entity> entities = getStore().<Entity>query(query);
        return entities;
    }
    
}
