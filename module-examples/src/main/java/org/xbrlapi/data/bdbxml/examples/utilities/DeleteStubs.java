package org.xbrlapi.data.bdbxml.examples.utilities;

import java.util.List;

import org.xbrlapi.Stub;

/**
 * Deletes all of the stubs from the
 * data store.  Stubs record the reasons for document load failures.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DeleteStubs extends BaseUtilityExample {
    
    public DeleteStubs(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                
                List<Stub> stubs = store.getXMLResources("Stub");
                for (Stub stub: stubs) {
                    store.remove(stub);
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
        DeleteStubs utility = new DeleteStubs(args);
    }



   
    

    
}
