package org.xbrlapi.bdbxml.examples.utilities;

import java.util.Set;
import java.util.TreeSet;

/**
 * Finds all locators in a document that identify more than one locator target.
 * Generally such situations arise when the same document has been stored
 * in the data store more than once.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>-document [The URI of the document]</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class FindLocatorsWithMultipleTargetsForADocument extends BaseUtilityExample {
    
    public FindLocatorsWithMultipleTargetsForADocument(String[] args) {
        System.out.println("Returns the URI, XPointer and the database index for each target fragment.");
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                String query = "for $locator in #roots#,$target in #roots#/xbrlapi:xptr where ($locator/@uri='"+arguments.get("document")+"' and $locator/*/*/@xlink:type='locator' and $target/../@uri=$locator/@targetDocumentURI and $locator/@targetPointerValue=$target/@value) return concat($target/../@uri,' ',$target/@value,' ',$target/../@index)";
                Set<String> results = store.queryForStrings(query);
                System.out.println("# locators with more than one target = " + results.size());
                Set<String> sortedResults = new TreeSet<String>();
                sortedResults.addAll(results);
                for (String result: sortedResults) {
                    System.out.println(result);
                }
                System.out.println("# locators with more than one target = " + results.size());
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
        FindLocatorsWithMultipleTargetsForADocument utility = new FindLocatorsWithMultipleTargetsForADocument(args);
    }

    protected String addArgumentDocumentation() {
        String explanation = super.addArgumentDocumentation();
        explanation += "-document\t\tURI of the document to containing locators to analyse.\n";
        return explanation;
    }    

    protected String setUp() {
        String message = super.setUp();
        if (!arguments.containsKey("document")) 
            message += "The document to be analysed is not specified.\n";
        return message;
    }
}
