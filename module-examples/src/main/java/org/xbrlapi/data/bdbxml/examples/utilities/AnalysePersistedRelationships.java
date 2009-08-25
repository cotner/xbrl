package org.xbrlapi.data.bdbxml.examples.utilities;

import java.util.Set;

import org.xbrlapi.utilities.Constants;

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
                
                String query = "for $root in #roots#[@type='org.xbrlapi.impl.RelationshipImpl'] return $root";
                long count = store.queryCount(query);
                System.out.println("# of persisted relationships = " + count);

                query = "for $root in #roots#[@type='org.xbrlapi.impl.RelationshipImpl' and @arcRole='"+Constants.LabelArcrole+"'] return string($root/@targetIndex)";
                Set<String> labelIndices = store.queryForStrings(query);
                System.out.println("There are " + labelIndices.size() + " label relationships in documents.");
/*                
                for (String index: labelIndices) {
                    LabelResource label = store.<LabelResource>getXMLResource(index);
                    Set<String> sourceIndices = store.getSourceIndices(index,null,Constants.LabelArcrole);
                    System.out.println("label " + label.getStringValue() + " applies to " + sourceIndices.size() + " fragments.");
                    break;
                }*/

                query = "for $root in #roots#[@type='org.xbrlapi.impl.RelationshipImpl'] return distinct-values(substring-before($root/@arcIndex,'_'))";
                Set<String> prefixes = store.queryForStrings(query);
                System.out.println("There are relationships in " + prefixes.size() + " documents.");

                query = "for $root in #roots#[@type='org.xbrlapi.impl.RelationshipImpl' and @arcRole='"+Constants.LabelArcrole+"'] return distinct-values(substring-before($root/@arcIndex,'_'))";
                prefixes = store.queryForStrings(query);
                System.out.println("There are label relationships in " + prefixes.size() + " documents.");


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
        AnalysePersistedRelationships utility = new AnalysePersistedRelationships(args);
    }



   
    

    
}
