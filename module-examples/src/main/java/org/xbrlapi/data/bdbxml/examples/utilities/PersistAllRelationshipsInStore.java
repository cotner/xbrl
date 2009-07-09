package org.xbrlapi.data.bdbxml.examples.utilities;

import java.net.URI;
import java.util.Set;

import org.xbrlapi.networks.Storer;
import org.xbrlapi.networks.StorerImpl;

/**
 * Persists all of the relationships in the data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PersistAllRelationshipsInStore extends BaseUtilityExample {
    
    public PersistAllRelationshipsInStore(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();

        if (message.equals("")) {
            try {
                
                Storer storer = new StorerImpl(store);
                
                Set<URI> uris = store.getDocumentURIs();
                System.out.println("# documents = " + uris.size());
                for (URI uri: uris) {
                    System.out.println("Processing " + uri);
                    storer.storeRelationships(uri);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                badUsage(e.getMessage());
            }
        } else {
            badUsage(message);
        }
        
        tearDown();
    }
    
    /**
     * @param args The array of commandline arguments.
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        PersistAllRelationshipsInStore utility = new PersistAllRelationshipsInStore(args);
    }

}
