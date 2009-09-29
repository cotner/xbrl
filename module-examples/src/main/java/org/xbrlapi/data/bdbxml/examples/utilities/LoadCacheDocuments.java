package org.xbrlapi.data.bdbxml.examples.utilities;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;

import org.xbrlapi.networks.AnalyserImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * Loads all of the documents, from the specified cache directory, 
 * into the underlying data store.  This operation is recursive in that all
 * documents within the specified directory and any descendant directories are
 * loaded.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>-directory [The cache directory to start loading from]</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LoadCacheDocuments extends BaseUtilityExample {

    private File start;
    
    protected String setUp() {
        String message = super.setUp();
        if (!arguments.containsKey("directory")) 
            message += "The starting cache directory is not specified.\n";
        start = new File(arguments.get("directory"));
        if (! start.isDirectory()) message += "The starting directory MUST be a directory - not a URL or a file.\n";
        return message;
    }
    
    public LoadCacheDocuments(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {
                store.setAnalyser(new AnalyserImpl(store));
                loadFiles(start);
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
        LoadCacheDocuments utility = new LoadCacheDocuments(args);
    }
    
    protected String addArgumentDocumentation() {
        String explanation = super.addArgumentDocumentation();
        explanation += "-directory\t\t\tXBRL cache directory to start loading documents from.\n";
        return explanation;
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
