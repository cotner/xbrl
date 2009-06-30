package org.xbrlapi.data.bdbxml.utilities.tests;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Match;
import org.xbrlapi.cache.Cache;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.data.resource.Matcher;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

public class CacheMatcherTestCase extends BaseTestCase {
	private Cache cache = null;
	private Matcher matcher = null;
	
    // Create the logger
    protected static Logger logger = Logger.getLogger(BaseTestCase.class);  
	
	protected void setUp() throws Exception {
		super.setUp();
		cache = loader.getCache();
		matcher = new InStoreMatcherImpl(store,cache);
	}
	
	protected void tearDown() throws Exception {
        // Close but DO NOT delete the data stores.
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
	}	
	
	public CacheMatcherTestCase(String arg0) {
		super(arg0);
	}
	
	public void testURIStashingWithMatcher() {
		try {
		    testFiles(((CacheImpl) cache).getCacheRoot());
		    /*
		    List<Match> matches = store.getXMLs("Match");
		    for (Match match: matches) {
                URI uri = match.getMatch();
		        List<URI> uris = match.getURIs();
		        if (uris.size() > 1) {
	                logger.info("Duplicated document: " + uri);
	                match.serialize();
	                for (URI m: uris) {
	                    if (!m.equals(uri)) 
	                        logger.info(m);
	                }
		        }
		    }*/
		    
		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
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
	            logger.info(originalURI + " " + match);
	            getMatchXMLResource(originalURI).serialize();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        fail(e.getMessage());
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
        if (! directory.isDirectory()) fail(directory + " is not a directory.");
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
        if (! directory.isDirectory()) fail(directory + " is not a directory.");
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return (file.isDirectory());
            }
        };
        return directory.listFiles(fileFilter);
    }   
	
	
}
