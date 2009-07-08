package org.xbrlapi.bdbxml.examples.utilities;

import org.xbrlapi.networks.Storer;
import org.xbrlapi.networks.StorerImpl;

/**
 * Deletes all of the persisted relationship XML resources from the
 * data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DeletePersistedRelationships extends BaseUtilityExample {
    
    public DeletePersistedRelationships(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (! message.equals("")) badUsage(message);
        try {
            
            Storer storer = new StorerImpl(store);
            storer.deleteRelationships();
            
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
        DeletePersistedRelationships utility = new DeletePersistedRelationships(args);
    }



   
    

    
}
