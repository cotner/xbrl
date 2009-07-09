package org.xbrlapi.data.bdbxml.examples.utilities;

import java.net.URI;
import java.util.Set;

/**
 * Purges and then reloads any documents where there are multiple
 * versions of them in the data store. This fixes a data store corruption.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PurgeIdenticalDocuments extends BaseUtilityExample {
    
    public PurgeIdenticalDocuments(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();

        if (message.equals("")) {
            try {
                String query = "for $document in #roots#[@parentIndex=''], $duplicate in #roots#[@parentIndex=''] where ($document/@uri=$duplicate/@uri and $document/@index!=$duplicate/@index) return string($document/@uri)";
                Set<String> uris = store.queryForStrings(query);
                logger.info(uris.size());
                for (String uri: uris) {
                    logger.info(uri + " has duplicates in the data store.");
                    query = "for $fragment in #roots#[@uri='" + uri + "']  return string($fragment/@index)";
                    Set<String> indices = store.queryForStrings(query);
                    logger.info("# fragments in document = " + indices.size());
                    for (String index: indices) {
                        store.remove(index);
                    }
                }
                
                logger.info("Now reloading the problem documents.");
                for (String uri: uris) {
                    loader.discover(new URI(uri));
                }        
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
        PurgeIdenticalDocuments utility = new PurgeIdenticalDocuments(args);
    }



   
    

    
}
