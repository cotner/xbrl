package org.xbrlapi.loader;

import java.net.URI;

/**
 * The interface defining the contract for 
 * recording the history of documents being loaded
 * into a data store by a loader.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface History {

    public void addRecord(URI uri,String identifier);
    
}
