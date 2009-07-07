package org.xbrlapi.bdbxml.examples.utilities;

import java.net.URI;
import java.util.Set;

/**
 * Lists all documents in the data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ListStoredDocuments extends BaseUtilityExample {
    
    public ListStoredDocuments(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (! message.equals("")) badUsage(message);
        try {
            Set<URI> uris = store.getDocumentURIs();
            for (URI uri: uris) {
                System.out.println(uri);
            }
            System.out.println("# documents = " + uris.size());
        } catch (Exception e) {
            e.printStackTrace();
            badUsage(e.getMessage());
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
