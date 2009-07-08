package org.xbrlapi.bdbxml.examples.utilities;

import java.util.Set;

import org.xbrlapi.Fragment;

/**
 * Reports the number of persisted relationships in the data store.
 * Reports the URIs of documents that relationships have been persisted for.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalysePersistedRelationships extends BaseUtilityExample {
    
    public AnalysePersistedRelationships(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                
                Set<String> indices = store.getFragmentIndices("PersistedRelationship");
                System.out.println("# of persisted relationships = " + indices.size());
                String query = "for $root in #roots# where ($root/@arcIndex) return distinct-values(substring-before($root/@arcIndex,'_'))";
                Set<String> prefixes = store.queryForStrings(query);
                for (String prefix: prefixes) {
                    System.out.println(store.<Fragment>getXMLResource(prefix + "_1").getURI() + " has persisted relationships.");
                }
                System.out.println("# of persisted relationships = " + indices.size());
                
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
        AnalysePersistedRelationships utility = new AnalysePersistedRelationships(args);
    }



   
    

    
}
