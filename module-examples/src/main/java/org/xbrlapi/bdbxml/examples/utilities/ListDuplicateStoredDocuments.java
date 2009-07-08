package org.xbrlapi.bdbxml.examples.utilities;

import java.util.Set;

/**
 * Lists the URIs of all documents that have multiple versions
 * stored with the same URI.  This indicates a data store corruption
 * that should be rectified.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @see PurgeIdenticalDocuments
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ListDuplicateStoredDocuments extends BaseUtilityExample {
    
    public ListDuplicateStoredDocuments(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                String query = "for $document in #roots#[@parentIndex=''], $duplicate in #roots#[@parentIndex=''] where ($document/@uri=$duplicate/@uri and $document/@index!=$duplicate/@index) return string($document/@uri)";
                Set<String> uris = store.queryForStrings(query);
                System.out.println("# duplicated documents = " + uris.size());
                for (String uri: uris) {
                    System.out.println(uri + " has duplicates in the data store.");
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
        ListDuplicateStoredDocuments utility = new ListDuplicateStoredDocuments(args);
    }



   
    

    
}
