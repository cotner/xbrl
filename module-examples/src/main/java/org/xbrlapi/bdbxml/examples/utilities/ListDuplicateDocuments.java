package org.xbrlapi.bdbxml.examples.utilities;

import java.util.Set;

/**
 * Lists the URIs of all documents that have multiple versions
 * stored with the same URI.  This indicates a data store corruption
 * that should be rectified.
 * @see PurgeDuplicateDocuments
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ListDuplicateDocuments extends BaseUtilityExample {
    
    public ListDuplicateDocuments(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (! message.equals("")) badUsage(message);
        try {
            String query = "for $document in #roots#[@parentIndex=''], $duplicate in #roots#[@parentIndex=''] where ($document/@uri=$duplicate/@uri and $document/@index!=$duplicate/@index) return string($document/@uri)";
            Set<String> uris = store.queryForStrings(query);
            System.out.println("# duplicated documents = " + uris.size());
            for (String uri: uris) {
                System.out.println(uri + " has duplicates in the data store.");
            }        } catch (Exception e) {
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
        ListDuplicateDocuments utility = new ListDuplicateDocuments(args);
    }



   
    

    
}
