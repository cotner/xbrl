package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Label Cache</h2>
 * 
 * <p>
 * This interface defines the capabilities of aspect label caching systems.
 * </p>
 * 
 * <p>
 * Label caching systems are intended to improve the performance of aspect 
 * rendering systems by determining aspect and aspect value labels in advance.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface LabelCache extends Serializable {
    
    /**
     * @param aspectId the ID of the aspect - must not be null.
     * @param valueId The id of the aspect value - must not be null.
     * @param locale The XML language code - can be null.
     * @param resourceRole The label resource role - can be null.
     * @param linkRole The link role for the extended link containing the label - can be null.
     * @return the label matching the parameters or null if there is no such label in the cache.
     * @throws XBRLException
     */
    public String getLabel(URI aspectId, String valueId, String locale, URI resourceRole, URI linkRole) throws XBRLException;

    /**
     * Caches the given label, replacing earlier versions of such label in the cache to 
     * ensure that there is only ever one label in the cache that matches the selection 
     * criteria.
     * 
     * @param aspectId the ID of the aspect - must not be null.
     * @param valueId The id of the aspect value - must not be null.
     * @param locale The XML language code - can be null.
     * @param resourceRole The label resource role - can be null.
     * @param linkRole The link role for the extended link containing the label - can be null.
     * @param label The label to cache - must not be null.
     * @throws XBRLException
     */
    public void cacheLabel(URI aspectId, String valueId, String locale, URI resourceRole, URI linkRole, String label) throws XBRLException;

}
