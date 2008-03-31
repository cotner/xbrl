package org.xbrlapi;

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
    
}
