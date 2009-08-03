package org.xbrlapi.data.bdbxml.examples.utilities;

import java.net.URI;
import java.util.List;
import java.util.SortedSet;

import org.xbrlapi.Fragment;
import org.xbrlapi.Relationship;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;

/**
 * Provides a range of diagnostic information about the 
 * XLink relationships involving the specified fragment.
 * <ul>
 *  <li>-index [The index of the fragment to analyse]</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalyseRelationshipsForAFragment extends BaseUtilityExample {
    
    public AnalyseRelationshipsForAFragment(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                String index = arguments.get("index");

                Fragment fragment = store.getXMLResource(index);
                System.out.println("The " + fragment.getType() + " fragment has index=" + index);

                Networks networks = store.getNetworksTo(index);
                System.out.println("# networks to = " + networks.getSize());
                List<URI> arcroles = networks.getArcroles();
                for (URI arcrole: arcroles) {
                    List<URI> linkRoles = networks.getLinkRoles(arcrole);
                    for (URI linkRole: linkRoles) {
                        Network network = networks.getNetwork(linkRole,arcrole);
                        System.out.println("Network: " + network.getLinkRole() + " " + network.getArcrole());
                    }
                }

                SortedSet<Relationship> relationships = store.getRelationshipsTo(index,null,null);
                System.out.println("# relationships to = " + relationships.size());
                for (Relationship r: relationships) {
                    System.out.println(r.getLinkRole() + " " + r.getArcrole() + " " + r.getSourceIndex());
                }

                relationships = store.getRelationshipsFrom(index,null,null);
                System.out.println("# relationships from = " + relationships.size());
                for (Relationship r: relationships) {
                    System.out.println(r.getLinkRole() + " " + r.getArcrole() + " " + r.getTargetIndex());
                }

                networks = store.getNetworksFrom(index);
                System.out.println("# networks from = " + networks.getSize());
                arcroles = networks.getArcroles();
                for (URI arcrole: arcroles) {
                    List<URI> linkRoles = networks.getLinkRoles(arcrole);
                    for (URI linkRole: linkRoles) {
                        Network network = networks.getNetwork(linkRole,arcrole);
                        System.out.println("Network: " + network.getLinkRole() + " " + network.getArcrole());
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                badUsage(e.getMessage());
            }
        } else {
            badUsage(message);
        }
   
        tearDown();
    }
    
    protected String addArgumentDocumentation() {
        String explanation = super.addArgumentDocumentation();
        explanation += "-index\t\tIndex of the fragment to analyse.\n";
        return explanation;
    }    

    protected String setUp() {
        String message = super.setUp();
        if (!arguments.containsKey("index")) 
            message += "The fragment to analyse needs to be specified.\n";
        return message;
    }
    
    /**
     * @param args The array of commandline arguments.
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        AnalyseRelationshipsForAFragment utility = new AnalyseRelationshipsForAFragment(args);
    }

}
