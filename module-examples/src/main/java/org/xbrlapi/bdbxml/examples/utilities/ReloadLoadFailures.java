package org.xbrlapi.bdbxml.examples.utilities;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Stub;

/**
 * Attempts to reload all of the documents that are identified
 * in the data store as having not yet loaded properly.
 * Each document is first deleted from the data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ReloadLoadFailures extends BaseUtilityExample {
    
    public ReloadLoadFailures(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (! message.equals("")) badUsage(message);
        try {
            
            List<Stub> stubs = store.getStubs();
            logger.info("The data store failed to load " + stubs.size() + " documents.");

            for (Stub stub: stubs) {
                URI uri = stub.getResourceURI();
                logger.info(uri + " failed to load. " + stub.getReason());
                if (store.hasDocument(uri)) {
                    store.deleteDocument(uri);
                }
                loader.discover(uri);
            }
            
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
        ReloadLoadFailures utility = new ReloadLoadFailures(args);
    }

}
