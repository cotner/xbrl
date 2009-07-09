package org.xbrlapi.data.bdbxml.examples.utilities;

import java.net.URI;
import java.util.Set;

/**
 * Lists all documents in the data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ListStoredDocuments extends BaseUtilityExample {
    
    public ListStoredDocuments(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                Set<URI> uris = store.getDocumentURIs();
                for (URI uri: uris) {
                    System.out.println(uri);
                }
                System.out.println("# documents = " + uris.size());
            } catch (Exception e) {
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
        ListStoredDocuments utility = new ListStoredDocuments(args);
    }



   
    

    
}
