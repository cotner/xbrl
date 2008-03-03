package org.xbrlapi.data;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Networks;
import org.xbrlapi.utilities.XBRLException;

/**
 * The data store interface, defining all methods that need to be 
 * implemented by a data store to support the XBRLAPI.
 * 
 * The store constructor needs to initialise the data structure.  
 * For example, the constructor would be required to
 * establish a database connection if an XML database is being
 * used to handle the underlying data.  Alternatively, the constructor
 * would establish an XML DOM document if an XML DOM were being used
 * as the underlying data structure.  Similarly, initialisation
 * steps would be taken if XML data binding to Java objects were
 * being used to handle the underlying data.
 *
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Store {

	/**
	 * Close the data store.
	 * Throws XBRLException if the data store cannot be closed. 
	 */
	public void close() throws XBRLException;

	/**
	 * Store a fragment.  
	 * @param fragment The fragment to be added to the store.
	 * @throws XBRLException if the fragment cannot be added to the store.
	 */
    public void storeFragment(Fragment fragment) throws XBRLException;    

    /**
     * Test if a store contains a specific fragment, as identified by
     * its index.
     * @param index The index of the fragment to test for.
     * @return true iff the store contains a fragment with the specified 
     * fragment index.
     * @throws XBRLException If the test cannot be conducted.
     */
    public boolean hasFragment(String index) throws XBRLException;
    
    /**
     * Retrieves a fragment from a data store.
     * @param index The index of the fragment.
     * @return The fragment corresponding to the specified index.
     * @throws XBRLException if the fragment cannot be retrieved.
     */
    public Fragment getFragment(String index) throws XBRLException;

	/**
	 * Remove a fragment from the underlying data structure.  
	 * If a fragment with the same ID does not already exist in the 
	 * DTs then no action is required.
	 * @param index The index of the fragment to be removed from the DTS store.
	 * @throws XBRLException if the fragment cannot be removed from the store.
	 */
	public void removeFragment(String index) throws XBRLException;
	

    
    /**
     * This deletion method does not ensure that all other documents that
     * link to the document being deleted are also deleted.  This can cause
	 * relationships in the data store to be non-resolvable.
     * @param url The URL of the document to delete from the data store.
     * @throws XBRLException
     */
    public void deleteDocument(String url) throws XBRLException;
    

	 /**
	  * This deletion method ensures that all related documents
	  * are also deleted from the data store. 
	  * @param url The URL of the document to delete.
	  * @throws XBRLException
	  */
    public void deleteRelatedDocuments(String url) throws XBRLException;
    

    




	/**
	 * Run a query against the collection of all fragments in the store.
	 * @param query The XPath query to run against the set of fragments.
	 * @return a list of matching fragments or the empty list if no matching fragments
	 * exist.
	 * @throws XBRLException if the query cannot be executed.
	 */
	public <F extends Fragment> FragmentList<F> query(String query) throws XBRLException;	

    /**
     * Serialize the specified XML DOM to the specified destination.
     * @param what the root element of the DOM to be serialised.
     * @param destination The destination output stream to be serialised to.
     * @throws XBRLException if the DOM cannot be serialised
     * because the destination cannot be written to or some other
     * different problem occurs during serialisation.
     */
    public void serialize(Element what, OutputStream destination) throws XBRLException;

    /**
     * Serialize the specified XML DOM to the specified destination.
     * @param what the root element of the DOM to be serialised.
     * @param destination The destination file to be serialised to.
     * @throws XBRLException if the DOM cannot be serialised
     * because the destination cannot be written to or some other
     * different problem occurs during serialisation.
	 */
	public void serialize(Element what, File destination) throws XBRLException;
    
    /**
     * Serialize the specified XML DOM to the specified destination.
     * @param what the root element of the DOM to be serialised.
     * @param destination The destination file to be serialised to.
     * @throws XBRLException if the DOM cannot be serialised
     * because the destination cannot be written to or some other
     * different problem occurs during serialisation.
	 */
	public void serialize(Element what, String destination) throws XBRLException;
	
    /**
     * Serialize the specified XML DOM to System.out.
     * @param what the root element of the DOM to be serialised.
     * @throws XBRLException
     */
    public void serialize(Element what) throws XBRLException;
    
    /**
     * Serialize the specified fragment
     * @param fragment The fragment to be serialised.
     * @throws XBRLException
     */
    public void serialize(Fragment fragment) throws XBRLException;
    
    /**
     * Serialize the specified XML DOM node.
     * @param what the root element of the DOM to be serialised.
     * @return a string containing the serialized XML.
     * @throws XBRLException
     */
    public String serializeToString(Element what) throws XBRLException;    

    /**
     * Get a single document in the store as a DOM.
     * @param url The string representation of the URL of the 
     * document to be retrieved.
     * @return a DOM Document containing the XML representation of the
     * file at the specified URL.  Returns null if the store does not
     * contain a document with the given URL.
     * @throws XBRLException if the document cannot be constructed as a DOM.
     */
    public Element getDocumentAsDOM(String url) throws XBRLException;
    
	/**
	 * Serializes the individual documents in the data store, 
	 * saving them into a directory structure that is placed into
	 * the specified directory.  The directory structure that is 
	 * created mirrors the structure of the URLs of the documents. 
	 * Note that the URLs of the documents that are written out
	 * will be reflected in the paths to those documents
	 * using the same rules as those applied for document caching.
	 * @param destination The folder in which the directory structure and
	 * the documents in the data store are to be saved.  
	 * @throws XBRLException If the root folder does not exist or 
	 * is not a directory or if the documents in the store cannot 
	 * be saved to the local file system.
	 */
	public void saveDocuments(File destination) throws XBRLException;
	
	/**
	 * Serializes those documents in the data store with a URL that
	 * begins with the specified URL prefix. They are saved to the local
	 * file system in the same manner as is applied for the saveDocuments
	 * method that operates on all documents in the data store.
	 * @param destination The folder in which the directory structure and
	 * the documents in the data store are to be saved.
	 * @param urlPrefix All documents in the data store with a URL that begins 
	 * with the string specified by urlPrefix will be saved to the local
	 * file system.
	 * @throws XBRLException If the root folder does not exist or 
	 * is not a directory or if the documents in the store cannot 
	 * be saved to the local file system.
	 */
	public void saveDocuments(File destination, String urlPrefix) throws XBRLException;
	
	/**
	 * Creates a single DOM structure from all documents in the 
	 * data store and saves this single XML structure in the
	 * specified file.
	 * @param file The file to save the Store content to.
	 * @throws XBRLException if the documents in the store cannot be
	 * saved to the single file.
	 */
	public void saveStoreAsSingleDocument(File file) throws XBRLException;
	
	/**
	 * Returns the root element of the subtree starting with the
	 * fragment with the specified index.  All subtrees for a given store
	 * instance are produced from the one XML DOM and so can be appended
	 * to eachother as required.
	 * @param f The fragment at the root of the subtree.
	 * @return The root element of the subtree headed by the fragment
	 * with the specified index.
	 * @throws XBRLException if the subtree cannot be constructed.
	 */
	public Element getSubtree(Fragment f) throws XBRLException;
	
	/**
     * Get all data in the store as a single XML DOM object.
     * @return the XML DOM representation of the XBRL information in the 
     * data store.
     * @throws XBRLException if the DOM cannot be constructed.
     */
    public Document getStoreAsDOM() throws XBRLException;
    
	/**
     * Get all data in the store as a single XML DOM object including
     * the annotations used in the 
     * <a href="http://www.sourceforge.net/xbrlcomposer/">XBRLComposer</a> project.
     * @return the composed data store as a DOM object.
     * @throws XBRLException if the composed data store cannot be constructed.
     */
    public Document formCompositeDocument() throws XBRLException;
    
    /**
     * Get a list of the URLs that have been stored.
     * @return a list of the URLs in the data store.
     * @throws XBRLException if the list cannot be constructed.
     */
    public List<String> getStoredURLs() throws XBRLException;
    
    /**
     * Test if a particular URL is already in the data store.
     * @param url the string representation of the URL to be tested for.
     * @return true if the document is in the store and false otherwise.
     * @throws XBRLException if the document cannot be constructed as a DOM.
     */
    public boolean hasDocument(String url) throws XBRLException;
    

    
    /**
     * Stores the state of the document discovery process.
     * @param id  The next ID to use for the next fragment to be added to the DTS.
     * @param documents The list of URLs of the documents remaining to be
     * discovered.
     * @throws XBRLException
     */
    public void storeLoaderState(String id,List<String> documents) throws XBRLException;    

    /**
     * Get the next fragment ID to use when extending a DTS, instead of starting at 1 again
     * and corrupting the DTS data store with duplicate fragment IDs.
     * @return The next ID to use for the next fragment to be added to the DTS.
     * @throws XBRLException
     */
    public String getNextFragmentId() throws XBRLException;
    
    /**
     * @return the list of URLs of the documents remaining to be analysed.
     * @throws XBRLException if any of the document URLs are malformed.
     */
    public List<URL> getDocumentsToDiscover() throws XBRLException;
    
    /**
     * @return a fragment summarising the state of the data store.
     * The fragment provides information about the next fragment index
     * and the documents that remain to be analysed/discovered.
     * The summary is only accurate if the store is not currently being
     * operated on by a data loader.
     */
    public Fragment getStoreState() throws XBRLException;
    




    /**
     * Utility method to return a list of fragments in a data store
     * that have a type corresponding to the specified fragment interface name.
     * @param interfaceName The name of the interface.  EG: If a list of
     *  org.xbrlapi.impl.ReferenceArcImpl fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an Arc would not return all ReferenceArcs as well as other
     *  types of arcs.
     * @return a list of fragments with the given fragment type.
     * @throws XBRLException
     */
    public <F extends Fragment> FragmentList<F> getFragments(String interfaceName) throws XBRLException;
    
    /**
     * @param interfaceName The name of the interface.  EG: If a list of
     *  org.xbrlapi.impl.ReferenceArcImpl fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an Arc would not return all ReferenceArcs as well as other
     *  types of arcs.
     *  @param parentIndex The index of the parent fragment.
     * @return a list of fragments with the given fragment type and with the given parent fragment.
     * @throws XBRLException
     */
    public <F extends Fragment> FragmentList<F> getChildFragments(String interfaceName, String parentIndex) throws XBRLException;
    
    
    /**
     * get the collection of networks expressed using arcs that involve this 
     * arc role.
     */
    public Networks getNetworks(String arcRole) throws XBRLException;  
    
    /**
     * Utility method to return a list of fragments in a data store
     * that have a type corresponding to the specified fragment interface name and
     * that are in the document with the specified URL.
     * @param url The URL of the document to get the fragments from.
     * @param interfaceName The name of the interface.  EG: If a list of
     *  org.xbrlapi.impl.ReferenceArcImpl fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an Arc would not return all ReferenceArcs as well as other
     *  types of arcs.
     * @return a list of fragments with the given fragment type and in the given document.
     * @throws XBRLException
     */
    public <F extends Fragment> FragmentList<F> getFragmentsFromDocument(URL url, String interfaceName) throws XBRLException;
    
    /**
     * @param <F> The fragment extension class
     * @param url The URL of the document to get the root fragment for.
     * @return the root fragment of the document with the given URL or null if no
     * root fragment is available for the given URL.
     * @throws XBRLException if more than one root fragment is found in the data store.
     */
    public <F extends Fragment> F getRootFragmentForDocument(String url) throws XBRLException;

    
    /**
     * @param <F> The fragment extension class
     * @return the list of root fragments of the documents in the store.
     * @throws XBRLException if more than one root fragment is found in the data store.
     */
    public <F extends Fragment> FragmentList<F> getRootFragments() throws XBRLException;    
    
    /**
     * Delete and close the data store.
     * @throws XBRLException if the data store cannot be deleted.
     */
    public void delete() throws XBRLException;    
    
}
