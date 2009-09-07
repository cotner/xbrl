package org.xbrlapi.data.bdbxml.examples.utilities;

import org.xbrlapi.impl.ConceptImpl;

/**
 * Tests an XQuery against an existing data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TestQuery extends BaseUtilityExample {
    
    public TestQuery(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                
                String query = "let $allConcepts := for $root in #roots#[@type='"+ConceptImpl.class.getName()+"'] return $root for $concept at $count in subsequence($allConcepts, 1," + 30 + ") return $concept";
                long count = store.queryCount(query);
                System.out.println("# of query results = " + count);
                
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
        TestQuery utility = new TestQuery(args);
    }



   
    

    
}
