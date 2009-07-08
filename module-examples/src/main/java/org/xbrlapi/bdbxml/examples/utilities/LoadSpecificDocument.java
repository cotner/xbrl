package org.xbrlapi.bdbxml.examples.utilities;

import java.net.URI;

/**
 * Loads a specific document, and all of the documents that it enables
 * discovery of, into the data store.  The document is first deleted from
 * the data store if it is already in the data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>-document [The URI of the document]</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LoadSpecificDocument extends BaseUtilityExample {
    
    public LoadSpecificDocument(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                URI uri = new URI(arguments.get("document")); 
                if (store.hasDocument(uri)) store.deleteDocument(uri);
                loader.discover(uri);
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
        LoadSpecificDocument utility = new LoadSpecificDocument(args);
    }

    protected String addArgumentDocumentation() {
        String explanation = super.addArgumentDocumentation();
        explanation += "-document\t\tURI of the document to load.\n";
        return explanation;
    }    

    protected String setUp() {
        String message = super.setUp();
        if (!arguments.containsKey("document")) 
            message += "The document to be loaded is not specified.\n";
        return message;
    }

}
