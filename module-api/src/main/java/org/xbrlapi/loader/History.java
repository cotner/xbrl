package org.xbrlapi.loader;

import java.io.Serializable;
import java.net.URI;
import java.util.Set;

/**
 * The interface defining the contract for 
 * recording the history of documents being loaded
 * into a data store by a loader.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface History extends Serializable {

    public void addRecord(URI uri,String identifier);

    public String getIdentifier(URI uri);

    public Set<URI> getURIs();
    
}
