package org.xbrlapi.bdbxml.examples.utilities;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.List;

import org.xbrlapi.Match;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.data.resource.Matcher;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests each document in the taxonomy cache to see if it has matching
 * documents based on the @link org.xbrlapi.resource.Matcher used by 
 * the underlying data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalyseCacheForMatchedDocuments extends BaseUtilityExample {
    
    private Matcher matcher = null;
    
    public AnalyseCacheForMatchedDocuments(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                matcher = new InStoreMatcherImpl(store,cache);
                testFiles(((CacheImpl) cache).getCacheRoot());
                
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
        AnalyseCacheForMatchedDocuments utility = new AnalyseCacheForMatchedDocuments(args);
    }

    private void testFiles(File directory) {
        
        for (File f: this.getChildFiles(directory)) {
            testFile(f);
        }
        for (File d: this.getChildDirectories(directory)) {
            testFiles(d);
        }
    }
    
    private void testFile(File file) {
        try {
            URI originalURI = cache.getOriginalURI(file);
            URI match = matcher.getMatch(originalURI);
            if (!match.equals(originalURI)) {
                System.out.println(match + " matches " + originalURI);
            }
        } catch (Exception e) {
            e.printStackTrace();
            badUsage(e.getMessage());
        }
    }
    
    private Match getMatchXMLResource(URI uri) throws XBRLException {
        String query = "for $match in #roots#[@type='org.xbrlapi.impl.MatchImpl'] where $match/" + Constants.XBRLAPIPrefix + ":match" + "/@value='"+ uri + "' return $match";
        List<Match> matches = store.<Match>queryForXMLResources(query);
        if (matches.size() == 1) return matches.get(0);
        if (matches.size() == 0) return null;
        throw new XBRLException("The wrong number of match fragments was retrieved.  There must be just one.");
    }   
    
    /**
     * Fails if the directory is not a directory.
     * @param directory
     * @return a list of all files (but not directories) 
     * contained in the given directory.
     */
    private File[] getChildFiles(File directory) {
        if (! directory.isDirectory()) 
            problems += directory + " is not a directory.\n";
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return (!file.isDirectory());
            }
        };
        return directory.listFiles(fileFilter);
    }
    
    /**
     * Fails if the directory is not a directory.
     * @param directory
     * @return a list of all child directories. 
     */
    private File[] getChildDirectories(File directory) {
        if (! directory.isDirectory()) 
            problems += directory + " is not a directory.\n";
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return (file.isDirectory());
            }
        };
        return directory.listFiles(fileFilter);
    }


   
    

    
}
