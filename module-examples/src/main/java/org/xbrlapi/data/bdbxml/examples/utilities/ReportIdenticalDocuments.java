package org.xbrlapi.data.bdbxml.examples.utilities;

import java.util.List;

import org.w3c.dom.NodeList;
import org.xbrlapi.Match;
import org.xbrlapi.utilities.Constants;

/**
 * Lists all sets of identical documents in the data store.
 * Identical documents are documents with the same content
 * but at a different URI.  Such URI differences can be ignored by
 * the data store so that the documents at the different URIs are
 * treated as being the same document.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ReportIdenticalDocuments extends BaseUtilityExample {
    
    public ReportIdenticalDocuments(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                List<Match> matches = store.<Match>getXMLResources("Match");
                System.out.println("# sets of identical documents = " + matches.size());
                for (Match match: matches) {
                    NodeList nodes = match.getMetadataRootElement().getElementsByTagNameNS(Constants.XBRLAPINamespace,"match");
                    if (nodes.getLength() > 1) {
                        System.out.println("Documents matching " + nodes.item(0).getTextContent() + " are:");
                        for (int i=1; i<nodes.getLength(); i++) {
                            System.out.println("\t" + i + "\t" + nodes.item(i).getTextContent());
                        }
                    }
                }
                System.out.println("# sets of identical documents = " + matches.size());
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
        ReportIdenticalDocuments utility = new ReportIdenticalDocuments(args);
    }



   
    

    
}
