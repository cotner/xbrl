package org.xbrlapi.bdbxml.examples.utilities;

import java.net.URI;

/**
 * Loads a specific document, and all of the documents that it enables
 * discovery of, into the data store.  The document is first deleted from
 * the data store if it is already in the data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LoadSpecificDocument extends BaseUtilityExample {
    
    public LoadSpecificDocument(String[] args) {
        usage = "java org.xbrlapi.bdbxml.examples.utilities.LoadSpecificDocument <ARGUMENTS>";
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (! message.equals("")) badUsage(message);
        try {
            URI uri = new URI(arguments.get("document")); 
            if (store.hasDocument(uri)) store.deleteDocument(uri);
            loader.discover(uri);
        } catch (Exception e) {
            e.printStackTrace();
            badUsage(e.getMessage());
        }
        
        tearDown();
    }
    
    protected String addArgumentDocumentation() {
        String explanation = super.addArgumentDocumentation();
        explanation += "-document\t\tURI of the document to load.\n";
        return explanation;
    }    


    
    /**
     * @param args The array of commandline arguments.
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        LoadSpecificDocument utility = new LoadSpecificDocument(args);
    }

    protected String setUp() {
        String message = super.setUp();
        if (!arguments.containsKey("document")) 
            message += "The document to be loaded is not specified.\n";
        return message;
    }

    protected void tearDown() {
        super.tearDown();
    }   
    

    
}
