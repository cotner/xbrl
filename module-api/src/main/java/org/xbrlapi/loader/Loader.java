package org.xbrlapi.loader;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import org.w3c.dom.Document;
import org.xbrlapi.Fragment;
import org.xbrlapi.cache.Cache;
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

public interface Loader extends Serializable {

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
	 * exists at a specified URI.  This is because generally the
	 * caching mechanism available via the entityResolver will handle
	 * caching for documents that actually exist at the specified URI.
	 * care must be taken to ensure that the cache used by the loader 
	 * is using the same local root folder as the cache used by the 
	 * entityResolver.  This is because the documents cached by the 
	 * loader can then be sought in the cache by the entity resolver.
	 * @param cache The cache to use in the loader.
	 */
	public void setCache(Cache cache);
	
	/**
	 * @return The cache to be used by the loader.
	 * @throws XBRLException if the cache is null.
	 */
	public Cache getCache() throws XBRLException;
	
	/**
	 * @return The document node of the XML DOM used
	 * by this loader's fragment builders.
	 * This should only be used when creating a new
	 * fragment using the loader.
	 * @throws XBRLException
	 */
	public Document getBuilderDOM() throws XBRLException;

	/**
	 * @return the list of documents that are known to still need parsing
	 * into the data store.
	 */
	public List<URI> getDocumentsStillToAnalyse();
	
	/**
	 * Begin the XBRL DTS discovery process with the URIs that
	 * are already in the loading/discovery queue.
	 * If the store is using persisted networks, then once a discovery
	 * process completes without having any failures and without being
	 * interrupted, then it will automatically update the persisted
	 * networks to reflect relationships defined by 
	 * all newly added XLink arcs.
	 * @throws XBRLException if the discovery process fails.
	 */
	public void discover() throws XBRLException;
	
	/**
	 * Parses the next document in the queue of documents
	 * to be discovered.  This updates the data store and
	 * updates the queue with any new documents that are 
	 * identified.
	 * Note that this does not update the persisted relationships
	 * because there is no guarantee that when the method finishes
	 * the data store represents an actual DTS.
	 * @throws XBRLException if the discovery process fails.
	 */
	public void discoverNext() throws XBRLException;
	
	/**
	 * Perform a discovery starting with an XML document that is represented as a string.
	 * @param uri The URI to be used for the document that is supplied as a string.
	 * @param xml The string representation of the XML document to be parsed.
	 * @throws XBRLException if the discovery process fails.
	 */
	public void discover(URI uri, String xml) throws XBRLException;
	
	/**
	 * Begin the XBRL DTS discovery process with the specified
	 * URIs given in the provided list.
	 * @param startingURIs The starting point URIs for the DTS
	 * discovery process
	 * Trigger the discovery process given the starting URIs.
	 * @throws XBRLException if the input list contains objects 
	 * other than java.net.URIs.
	 */
	public void discover(List<URI> startingURIs) throws XBRLException;
	
	/**
	 * Trigger the discovery process given a single URI.
	 * @param uri The URI to discover.
	 * @throws XBRLException
	 */
	public void discover(URI uri) throws XBRLException;

	/**
	 * Trigger the discovery process given a single URI.
	 * @param uri The URI to discover.
	 * @throws XBRLException
	 */
	public void discover(String uri) throws XBRLException;	




	
	/**
	 * Stash a URI to await loading into DTS.
	 * @param uri The absolute URI to be stashed (any relative
	 * URI gets resolved against the Base URI before stashing.
	 * @throws XBRLException if the URI cannot be stored for 
	 * later exploration or if the URI is not absolute.
	 */
	public void stashURI(URI uri) throws XBRLException;
	
    /**
     * @param uris The list of URIs to be stashed.
     * @throws XBRLException
     */
    public void stashURIs(List<URI> uris) throws XBRLException;


	/**
	 * @return the fragment being built currently by the loader 
	 * or null if no fragments are currently being build by the loader.
	 * @throws XBRLException
	 */
	public Fragment getFragment() throws XBRLException;
	
    /**
     * This is particularly useful when you can only fully determine the fragment type
     * by reading in the complex element content of the data corresponding to the fragment.
     * A case in point is the fractionItem fragment type.
     * @param replacement the fragment to replace the current fragment being build with.
     * @throws XBRLException if there is no current fragment.
     */
    public void replaceCurrentFragment(Fragment replacement) throws XBRLException;	
	
	/**
	 * @return true if and only if the loader has one or more fragments
	 * on the stack of fragments being built.
	 */
	public boolean isBuildingAFragment();
	
	/**
	 * @return true if the loader is current engaged in 
	 * document discovery and false otherwise.
	 */
    public boolean isDiscovering();

	/**
	 * Push a new fragment onto the stack of fragments that are being built
	 * by the loader.
	 * @param fragment The fragment to be added to the stack of fragments
	 * being built by the loader.
	 * @param state The state of the element that is the root of the fragment.
	 * @throws XBRLException
	 */
	public void add(Fragment fragment, ElementState state) throws XBRLException;

	/**
	 * Tests if the element that has just been found has triggered the addition of a fragment.
	 * Sets the flag to false once it has been tested, ready for the next element to be parsed.
	 * @return true iff the element that has just been found has triggered the addition of a fragment.
	 * @throws XBRLException.
	 */
/*	public boolean addedAFragment();*/
	
	/**
     * If a fragment is completed, remove the fragment from the 
	 * stack being maintained by the loader, store it in the data store and
	 * make the necessary update to the stack of child counts for the fragments.
	 * @param state The element state for the element currently being parsed.
	 * @throws XBRLException
	 */
	public void updateState(ElementState state) throws XBRLException;
	

	
	/**
	 * Also increments the fragment index as a side-effect.
	 * TODO eliminate the side-effect of the getNextFragmentId method.
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
	 * Get the URI for the document being parsed. 
	 * @return The original (non-cache) URI of the document being parsed.
	 */
	public URI getDocumentURI();
	
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