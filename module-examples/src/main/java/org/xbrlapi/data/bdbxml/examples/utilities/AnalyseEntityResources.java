package org.xbrlapi.data.bdbxml.examples.utilities;

import java.util.List;

import org.xbrlapi.EntityResource;

/**
 * Reports on entity resources that have data.  Does so by matching
 * up entity resources with XBRL context entity identifiers.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalyseEntityResources extends BaseUtilityExample {
    
    public AnalyseEntityResources(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                
                String query = "for $index in distinct-values(for $er in #roots#[@type='org.xbrlapi.impl.EntityResourceImpl'], $e in #roots#[@type='org.xbrlapi.impl.EntityImpl'] where $er/*/*/@scheme = $e/*/*/*/@scheme and $er/*/*/@value=$e/*/*/* return $er/@index),$root in #roots# where $root/@index=$index return $root";
                logger.info(query);
                List<EntityResource> entitiesWithData = store.<EntityResource>queryForXMLResources(query);
                logger.info(entitiesWithData.size());
                
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
        AnalyseEntityResources utility = new AnalyseEntityResources(args);
    }

}
