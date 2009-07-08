package org.xbrlapi.bdbxml.examples.utilities;

import java.util.List;

import org.xbrlapi.Stub;

/**
 * Reports information about the documents that failed to load properly.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalyseLoadFailures extends BaseUtilityExample {
    
    public AnalyseLoadFailures(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                
                List<Stub> stubs = store.getStubs();
                System.out.println("The data store failed to load " + stubs.size() + " documents.");

                for (Stub stub: stubs) {
                    System.out.println(stub.getResourceURI() + " failed to load. " + stub.getReason());
                    System.out.println("Store contains this document? " + store.hasDocument(stub.getResourceURI()));
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
        AnalyseLoadFailures utility = new AnalyseLoadFailures(args);
    }



   
    

    
}
