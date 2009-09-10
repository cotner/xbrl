package org.xbrlapi.data.bdbxml.examples.utilities;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;

import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.resource.Matcher;
import org.xbrlapi.networks.AnalyserImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * Loads all of the documents in the cache into the underlying data store.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LoadAllDocumentsInCache extends BaseUtilityExample {
    
    private Matcher matcher = null;
    
    public LoadAllDocumentsInCache(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                store.setAnalyser(new AnalyserImpl(store));
                loadFiles(((CacheImpl) cache).getCacheRoot());
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
        LoadAllDocumentsInCache utility = new LoadAllDocumentsInCache(args);
    }

    private void loadFiles(File directory) {
        for (File f: this.getChildFiles(directory)) loadFile(f);
        for (File d: this.getChildDirectories(directory)) loadFiles(d);
    }
    
    private void loadFile(File file) {
        try {
            URI originalURI = cache.getOriginalURI(file);
            loader.discover(originalURI);
        } catch (XBRLException e) {
            e.printStackTrace();
            badUsage(e.getMessage());
        }
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
