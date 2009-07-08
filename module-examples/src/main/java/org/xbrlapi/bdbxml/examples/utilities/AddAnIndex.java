package org.xbrlapi.bdbxml.examples.utilities;

import java.net.URI;

import org.xbrlapi.data.bdbxml.StoreImpl;

/**
 * Adds a new index to the data store.
 * Index string syntax documentation is available at:
 * @link http://www.oracle.com/technology/documentation/berkeley-db/xml/gsg_xml/java/indices.html
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>-namespace* [The namespace of the node being indexed]</li>
 *  <li>-name [The local name of the node being indexed]</li>
 *  <li>-index [The string representation of the index being added]</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AddAnIndex extends BaseUtilityExample {
    
    public AddAnIndex(String[] args) {
        System.out.println("This can take ages to run for a large database.");
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                if (! arguments.containsKey("namespace"))
                        ((StoreImpl) store).addIndex(null,arguments.get("name"),arguments.get("index"));
                else
                    ((StoreImpl) store).addIndex(new URI(arguments.get("namespace")),arguments.get("name"),arguments.get("index"));
            } catch (Exception e) {
                e.printStackTrace();
                badUsage(e.getMessage());
            }
        } else {
            badUsage(message);
        }

        
        tearDown();
    }

    protected String setUp() {
        String message = super.setUp();
        if (!arguments.containsKey("name")) 
            message += "The node local name is not specified.\n";
        if (!arguments.containsKey("index")) 
            message += "The new index is not specified.\n";
        return message;
    }    
    
    /**
     * @param args The array of commandline arguments.
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        AddAnIndex utility = new AddAnIndex(args);
    }

    protected String addArgumentDocumentation() {
        String explanation = super.addArgumentDocumentation();
        explanation += "-namespace*\t\tThe namespace of the node being indexed.\n";
        explanation += "-name\t\tThe local name of the node being indexed.\n";
        explanation += "-index\t\tThe string representation of the index being added.\n";
        return explanation;
    }

}
