package org.xbrlapi.bdbxml.examples.utilities;

import java.net.URI;

/**
 * Deletes a specific document from the data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>-document [The URI of the document]</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DeleteSpecificDocument extends BaseUtilityExample {
    
    public DeleteSpecificDocument(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (! message.equals("")) badUsage(message);
        try {
            URI uri = new URI(arguments.get("document")); 
            store.deleteDocument(uri);
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
        DeleteSpecificDocument utility = new DeleteSpecificDocument(args);
    }

    protected String addArgumentDocumentation() {
        String explanation = super.addArgumentDocumentation();
        explanation += "-document\t\tURI of the document to delete.\n";
        return explanation;
    }    

    protected String setUp() {
        String message = super.setUp();
        if (!arguments.containsKey("document")) 
            message += "The document to be deleted is not specified.\n";
        return message;
    }

}
