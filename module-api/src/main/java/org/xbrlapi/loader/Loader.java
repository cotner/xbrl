package org.xbrlapi.loader;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.Fragment;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.ElementState;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xml.sax.EntityResolver;

/**
 * Loader defines the functions required to get a DTS
 * into the data store ready for interactions via the 
 * XBRL API.  In many regards, this performs a similar
 * role to an XML parser in that it triggers the
 * exploration process and eventually populates a data
 * structure.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Loader {

	/**
	 * Get the DTS storage implementation
	 * @return The DTS store to be used for holding 
	 * the information in the DTS
	 */
	public Store getStore();	

	/**
	 * Get the XLink processor
	 * @return The XLink processor used in the DTS discovery process
	 */
	public XLinkProcessor getXlinkProcessor();
	
	/**
	 * Set the resolver for the resolution of entities found during 
	 * the loading and XLink processing
	 * @param resolver An entity resolver implementation
	 */
	public void setEntityResolver(EntityResolver resolver);

	/**
	 * This method specifies a cache to use when loading 
	 * data into the data store.  The cache is only useful (and
	 * thus only needs to be specified) when discovering XML that
	 * is provided as a string rather than a resource that actually
	 * exists at a specified URL.  This is because generally the
	 * caching mechanism available via the entityResolver will handle
	 * caching for documents that actually exist at the specified URL.
	 * care must be taken to ensure that the cache used by the loader 
	 * is using the same local root folder as the cache used by the 
	 * entityResolver.  This is because the documents cached by the 
	 * loader can then be sought in the cache by the entity resolver.
	 * @param cache The cache to use in the loader
	 */
	public void setCache(CacheImpl cache);
	
	/**
	 * @return The cache to be used by the loader.
	 * @throws XBRLException if the cache is null.
	 */
	public CacheImpl getCache() throws XBRLException;
	
	/**
	 * The children vector has an element for each element that has been started and
	 * that has not yet been ended in the SAX content handler.
	 * @return The vector of children counts.
	 * @throws XBRLException
	 */
	public Vector<Long> getChildrenVector() throws XBRLException;
	
	/**
	 * Add a new child tracking vector to the childrenStack to use for the new fragment
	 * that is being built by the loader.
	 * @throws XBRLException
	 */
	public void prepareToTrackChildrenForNewFragment() throws XBRLException;
	
	/**
	 * Increment the children tracker to show that a new element has been found.
	 * @throws XBRLException
	 */
	public void incrementChildren() throws XBRLException;
	
	/**
	 * Add a new node to the vector of children being tracked for the current fragment.
	 * Initialise its value to zero to capture the fact that no children have been found
	 * for the newly processed element - as yet.
	 * @throws XBRLException
	 */
	public void extendChildren() throws XBRLException;

	/**
	 * @return the list of documents that are known to still need parsing
	 * into the data store.
	 */
	public List<String> getDocumentsStillToAnalyse();
	
	/**
	 * Begin the XBRL DTS discovery process with the URLs that
	 * are already in the loading/discovery queue.
	 * @throws XBRLException if the discovery process fails.
	 */
	public void discover() throws XBRLException;
	
	/**
	 * Parses the next document in the queue of documents
	 * to be discovered.  This updates the data store and
	 * updates the queue with any new documents that are 
	 * identified.
	 * @throws XBRLException if the discovery process fails.
	 */
	public void discoverNext() throws XBRLException;
	
	/**
	 * Perform a discovery starting with an XML document that is represented as a string.
	 * @param url The URL to be used for the document that is supplied as a string.
	 * @param xml The string representation of the XML document to be parsed.
	 * @throws XBRLException if the discovery process fails.
	 */
	public void discover(URL url, String xml) throws XBRLException;
	
	/**
	 * Begin the XBRL DTS discovery process with the specified
	 * URLs given in the provided list.
	 * @param startingURLs The starting point URLs for the DTS
	 * discovery process
	 * Trigger the discovery process given the starting URLs.
	 * @throws XBRLException if the input list contains objects 
	 * other than java.net.URLs.
	 */
	public void discover(List<URL> startingURLs) throws XBRLException;
	
	/**
	 * Trigger the discovery process given a single URL.
	 * @param url The URL to discover.
	 * @throws XBRLException
	 */
	public void discover(URL url) throws XBRLException;

	/**
	 * Trigger the discovery process given a single URL.
	 * @param url The URL to discover.
	 * @throws XBRLException
	 */
	public void discover(String url) throws XBRLException;	

	/**
	 * Load up a serialised DTS ready for interaction via the XBRL API
	 * 
	 * @param file the serialised DTS file location
	 * @throws XBRLException
	 */
	public void load(File file) throws XBRLException;

	/**
	 * Load up a serialised DTS ready for interaction via the XBRL API
	 * 
	 * @param url the serialised DTS URL location
	 * @throws XBRLException
	 */
	public void load(URL url) throws XBRLException;
	
	/**
	 * Stash a URL to await loading into DTS.
	 * @param url The absolute URL to be stashed (any relative
	 * URL gets resolved against the Base URL before stashing.
	 * @throws XBRLException if the URL cannot be stored for 
	 * later exploration or if the URL is not absolute.
	 */
	public void stashURL(URL url) throws XBRLException;

	/*
	 * Stash a URL to await loading into data store.
	 * @param url The absolute URL to be stashed (any relative
	 * URL gets resolved against the Base URL before stashing.
	 * @namespace The namespace of the schema if the document at the 
	 * specified URL is a schema.  Use a null if no namespace is available.
	 * Perhaps this should be implemented as two methods, one for use when
	 * the namespace is available and one for use otherwise.
	 * @throws XBRLException if the URL cannot be stored for
	 * later exploration or if the URL is not absolute
	 */
	//public void stashURL(URL url, String namespace) throws XBRLException; //hju	
	
	/**
	 * Get the fragment that is currently being built by the DTS loader
	 * @return the fragment being built currently by the DTS loader or null if 
	 * no fragments are being built by the loader.
	 * @throws XBRLException
	 */
	public Fragment getFragment() throws XBRLException;

	/**
	 * Push a new fragment onto the stack of fragments that are being built
	 * by the loader.
	 * @param fragment The fragment to be added to the stack of fragments
	 * being built by the loader.
	 * @param state The state of the element that is the root of the fragment.
	 * @throws XBRLException
	 */
	public void addFragment(Fragment fragment, ElementState state) throws XBRLException;

	/**
	 * Tests if the element that has just been found has triggered the addition of a fragment.
	 * Sets the flag to false once it has been tested, ready for the next element to be parsed.
	 * @return true iff the element that has just been found has triggered the addition of a fragment.
	 * @throws XBRLException.
	 */
	public boolean addedAFragment();
	
	/**
     * If a fragment is completed, remove the fragment from the 
	 * stack being maintained by the loader, store it in the data store and
	 * make the necessary update to the stack of child counts for the fragments.
	 * @param state The element state for the element currently being parsed.
	 * @throws XBRLException
	 */
	public void updateState(ElementState state) throws XBRLException;
	
	/**
	 * Remove a fragment from the stack of fragments that are being built
	 * by the loader.
	 * @throws XBRLException if their are no fragments being built.
	 */
	public Fragment removeFragment() throws XBRLException;
	
	/**
	 * Returns the next unique fragment index and increments the 
	 * fragment index.
	 * @return the next unique fragment index
	 * @throws XBRLException
	 */
	public String getNextFragmentId() throws XBRLException;
	
    /**
     * Returns the current fragment index (the one before the next fragment index)
     * @return the next unique fragment index
     * @throws XBRLException
     */
    public String getCurrentFragmentId() throws XBRLException;	
	
	/**
	 * Get the URL for the document being parsed. 
	 * @return The original (non-cache) URL of the document being parsed.
	 */
	public String getDocumentURL();
	
	/**
	 * Return the entity resolver being used by the loader.
	 * @return the entity resolver being used by the loader.
	 */
	public EntityResolver getEntityResolver();
	
	/**
	 * The default behaviour is to ignore the content of XML Schema 
	 * instance schemaLocation attributes.
	 * @return true if the loader is required to discover documents
	 * identified in XML Schema instance schemaLocation attributes. 
	 */
	public boolean useSchemaLocationAttributes();
	
	/**
	 * @param useThem must be set to true if you want to discover documents identified
	 * in XML Schema instance schemaLocation attributes and false otherwise.
	 */
	public void setSchemaLocationAttributeUsage(boolean useThem);
	
    /**
     * Interrupts the loading process once the current 
     * document discovery has been completed.
     * This can be useful when the loader is shared among
     * several threads.
     */
    public void requestInterrupt();
    
    /**
     * Cancels a request for an interrupt.
     */
    public void cancelInterrupt();
    
    /**
     * Stores the stubs for the documents still to be analysed in
     * the data store being used by the loader.
     * @throws XBRLException
     */
    public void storeDocumentsToAnalyse() throws XBRLException;
	
}