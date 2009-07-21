package org.xbrlapi.data.bdbxml.examples.utilities;

import java.util.Set;

import org.xbrlapi.PersistedRelationship;

/**
 * Updates all relationships in the store to include the arc URI.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class UpdateAllRelationshipsInStore extends BaseUtilityExample {
    
    public UpdateAllRelationshipsInStore(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();

        if (message.equals("")) {
            try {
                
                Set<String> indices = store.queryForStrings("for $r in #roots#[@type='org.xbrlapi.impl.PersistedRelationshipImpl'] return string($r/@index)");
                long count = indices.size();
                long gap = 0;
                for (String index: indices) {
                    PersistedRelationship r = store.<PersistedRelationship>getXMLResource(index);
                    r.setMetaAttribute("arcURI",r.getArc().getURI().toString());
                    store.persist(r);
                    count--;
                    gap++;
                    if (gap > 1000) {
                        logger.info("# to process = " + count);
                        gap = 0;
                    }
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
        UpdateAllRelationshipsInStore utility = new UpdateAllRelationshipsInStore(args);
    }

}
