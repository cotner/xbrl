package org.xbrlapi.bdbxml.examples.utilities;

import java.net.URI;

/**
 * Loads a specific document, and all of the documents that it enables
 * discovery of, into the data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LoadSpecificDocument extends BaseUtilityExample {
    
    public LoadSpecificDocument(String[] args) {
        usage = "Command line usage: java org.xbrlapi.examples.load.Load -database VALUE -container VALUE -cache VALUE URI1 URI2 ...";
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        setUp();
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
     * Adds a document argument that is used to provide
     * the URI of the document to be loaded into the data store.
     * @see BaseUtilityExample#mapArgument(String, String)
     */
    protected boolean mapArgument(String name, String value) {
        if (super.mapArgument(name,value)) return true;
        if (name.equals("-document")) {
            arguments.put("document", value);
            return true;
        }
        return false;
    }
    
    /**
     * @param args The array of commandline arguments.
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        LoadSpecificDocument utility = new LoadSpecificDocument(args);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }   
    
    public void testLoadSpecificDocument() {
        try {
           
            URI uri = new URI("http://www.sec.gov/Archives/edgar/data/796343/000079634309000021/adbe-20090227.xsd"); 
            if (store.hasDocument(uri)) store.deleteDocument(uri);
            loader.discover(uri);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
