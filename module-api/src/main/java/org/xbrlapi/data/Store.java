package org.xbrlapi.data;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcroleType;
import org.xbrlapi.Concept;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Item;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Language;
import org.xbrlapi.Relationship;
import org.xbrlapi.ReferenceResource;
import org.xbrlapi.RoleType;
import org.xbrlapi.Stub;
import org.xbrlapi.Tuple;
import org.xbrlapi.XML;
import org.xbrlapi.data.resource.Matcher;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.networks.Analyser;
import org.xbrlapi.networks.Networks;
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

public interface Store extends Serializable {

	/**
	 * Close the data store.
	 * This method must be synchronized.
	 * Throws XBRLException if the data store cannot be closed. 
	 */
	public void close() throws XBRLException;

	/**
	 * Store a fragment.
	 * Implementations of this method must always be synchronized. 
	 * @param xml The fragment to be added to the store.
	 * @throws XBRLException if the fragment cannot be added to the store.
	 */
    public void persist(XML xml) throws XBRLException;    

    /**
     * Test if a store contains a specific fragment, as identified by
     * its index.
     * Implementations of this method must be synchronized.
     * @param index The index of the fragment to test for.
     * @return true iff the store contains a fragment with the specified 
     * fragment index.
     * @throws XBRLException If the test cannot be conducted.
     */
    public boolean hasXMLResource(String index) throws XBRLException;
    
    /**
     * Retrieves an XML Resource from a data store.
     * Implementations of this method must be synchronized.
     * @param index The index of the XML resource.
     * @return The XML resource corresponding to the specified index.
     * @throws XBRLException if the XML resource cannot be retrieved.
     */
    public <F extends XML> F getXMLResource(String index) throws XBRLException;

	/**
	 * Remove a fragment from the underlying data structure.
	 * If a fragment with the same ID does not already exist in the 
	 * data store then no action is required.
     * Implementations of this method must be synchronized.
	 * @param index The index of the fragment to be removed from the DTS store.
	 * @throws XBRLException if the fragment cannot be removed from the store.
	 */
	public void remove(String index) throws XBRLException;
	
    /**
     * Remove a XML resource from the underlying data structure.
     * If a XML resource with the same ID does not already exist in the 
     * data store then no action is required.
     * @param xml The XML resource to remove.
     * @throws XBRLException if the XML resource cannot be removed from the store.
     */
    public void remove(XML xml) throws XBRLException;	
	
	/**
	 * @param namespace The namespace to bind a prefix to for querying
	 * @param prefix The prefix to bind to the namespace for querying
	 * @throws XBRLException if either argument is null.
	 */
    public void setNamespaceBinding(URI namespace, String prefix) throws XBRLException;

    /**
     * This deletion method does not ensure that all other documents that
     * link to the document being deleted are also deleted.  This can cause
	 * relationships in the data store to be non-resolvable.
     * @param uri The URI of the document to delete from the data store.
     * @throws XBRLException
     */
    public void deleteDocument(URI uri) throws XBRLException;

	 /**
	  * This deletion method ensures that all related documents
	  * are also deleted from the data store. 
	  * @param uri The URI of the document to delete.
	  * @throws XBRLException
	  */
    public void deleteRelatedDocuments(URI uri) throws XBRLException;

	/**
	 * Run a query against the collection of all fragments in the store.
     * Implementations of this method must be synchronized.
	 * @param query The XPath query to run against the set of fragments.
     * Any occurrences of the string #roots# in a query will be deemed to 
     * be a marker for the root elements of the fragments in an XML database collection 
     * and it will be substituted with the necessary
     * expression to identify those roots in the data store.
   	 * @return a list of matching fragments or the empty list if no matching fragments
	 * exist.
	 * @throws XBRLException if the query cannot be executed.
	 */
	public <F extends XML> List<F> queryForXMLResources(String query) throws XBRLException;

    /**
     * Run a query against the collection of all fragments in the store.
     * Implementations of this method must be synchronized.
     * @param query The XPath query to run against the set of fragments.
     * Any occurrences of the string #roots# in a query will be deemed to 
     * be a marker for the root elements of the fragments in an XML database collection 
     * and it will be substituted with the necessary
     * expression to identify those roots in the data store.
     * The query <strong>MUST</strong> return a sequence of XML resource metadata root elements
     * matching the query.  Otherwise, results from the query will be unpredictable.
     * @return a set of the indices of XML resources matching 
     * the query.
     * @throws XBRLException if the query cannot be executed.
     */
    public Set<String> queryForIndices(String query) throws XBRLException;
    
    /**
     * This method must be synchronised
     * @param query The XPath query to run.
     * Any occurrences of the string #roots# in a query will be deemed to 
     * be a marker for the root elements of the fragments in an XML database collection 
     * and it will be substituted with the necessary
     * expression to identify those roots in the data store.
     * @return a count of the number of results returned by the query.
     * @throws XBRLException if the query cannot be executed.
     */
    public long queryCount(String query) throws XBRLException;    
    
    /**
     * Run a query that is required to return a list of strings.
     * Implementations of this method must be synchronized.
     * @param query The XPath query to run against the set of fragments.
     * Any occurrences of the string #roots# in a query will be deemed to 
     * be a marker for the root elements of the fragments in an XML database collection 
     * and it will be substituted with the necessary
     * expression to identify those roots in the data store.
     * @return a list of strings, each of which is a query result.
     * @throws XBRLException if the query cannot be executed or if the
     * query results are not strings.
     */
    public Set<String> queryForStrings(String query) throws XBRLException;
    
    /**
     * Run a query that is required to return a single string.
     * Implementations of this method must be synchronized.
     * @param query The XPath query to run against the set of fragments.
     * Any occurrences of the string #roots# in a query will be deemed to 
     * be a marker for the root elements of the fragments in an XML database collection 
     * and it will be substituted with the necessary
     * expression to identify those roots in the data store.
     * @return a single string that is the query result or null if the query
     * does not return any strings.
     * @throws XBRLException if the query cannot be executed or if the
     * query result is not a single string.
     */
    public String queryForString(String query) throws XBRLException;

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
    public void serialize(XML fragment) throws XBRLException;
    
    /**
     * Serialize the specified XML DOM node.
     * @param what the root element of the DOM to be serialised.
     * @return a string containing the serialized XML.
     * @throws XBRLException
     */
    public String serializeToString(Element what) throws XBRLException;    

    /**
     * Get a single document in the store as a DOM.
     * @param uri The URI of the document to be retrieved.
     * @return a DOM Document containing the XML representation of the
     * file at the specified URI.  Returns null if the store does not
     * contain a document with the given URI.
     * @throws XBRLException if the document cannot be constructed as a DOM.
     */
    public Element getDocumentAsDOM(URI uri) throws XBRLException;
    
	/**
	 * Serializes the individual documents in the data store, 
	 * saving them into a directory structure that is placed into
	 * the specified directory.  The directory structure that is 
	 * created mirrors the structure of the URIs of the documents. 
	 * Note that the URIs of the documents that are written out
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
	 * Serializes those documents in the data store with a URI that
	 * begins with the specified URI prefix. They are saved to the local
	 * file system in the same manner as is applied for the saveDocuments
	 * method that operates on all documents in the data store.
	 * @param destination The folder in which the directory structure and
	 * the documents in the data store are to be saved.
	 * @param uriPrefix All documents in the data store with a URI that begins 
	 * with the string specified by uriPrefix will be saved to the local
	 * file system.
	 * @throws XBRLException If the root folder does not exist or 
	 * is not a directory or if the documents in the store cannot 
	 * be saved to the local file system.
	 */
	public void saveDocuments(File destination, String uriPrefix) throws XBRLException;
	
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
    public Document getCompositeDocument() throws XBRLException;
    
    /**
     * Get a list of the URIs that have been stored.
     * @return a list of the URIs in the data store.
     * @throws XBRLException if the list cannot be constructed.
     */
    public Set<URI> getDocumentURIs() throws XBRLException;
    
    /**
     * Test if a particular URI is already in the data store.
     * @param uri the string representation of the URI to be tested for.
     * @return true if the document is in the store and false otherwise.
     * @throws XBRLException.
     */
    public boolean hasDocument(URI uri) throws XBRLException;

    /**
     * Stores the state of the document discovery process.
     * @param documents The map from URIs of the documents 
     * remaining to be discovered to the textual reason why 
     * the document has not yet been discovered.
     * @throws XBRLException
     */
    public void persistLoaderState(Map<URI,String> documents) throws XBRLException;    

    /**
     * @return the number of fragments in the data store.
     * @throws XBRLException if the number of fragments cannot be determined.
     */
    public int getSize() throws XBRLException;
    
    /**
     * @return the list of URIs of the documents remaining to be analysed.
     * @throws XBRLException if any of the document URIs are malformed.
     */
    public List<URI> getDocumentsToDiscover() throws XBRLException;
    
    /**
     * @return a list of stub fragments (Those fragments indicating a 
     * document that needs to be added to the data store).
     * @throws XBRLException
     */
    public List<Stub> getStubs() throws XBRLException;

    /**
     * @param uri The string value of the URI of the document to get the stub for.
     * @return the stub fragment or null if none exists.
     * @throws XBRLException if there is more than one stub.
     */
    public Stub getStub(URI uri) throws XBRLException;
    
    /**
     * @param document The document to store a stub for.
     * @param reason The reason the document has not been stored.
     * @throws XBRLException
     */
    public void persistStub(URI document, String reason) throws XBRLException;    
    
    /**
     * @param uri The URI of the document for which 
     * the stub fragment is to be removed from the data store.
     * @throws XBRLException
     */
    public void removeStub(String uri) throws XBRLException;

    /**
     * Return a list of XML resources in a data store
     * that have a type corresponding to the specified XML resource interface name.
     * @param interfaceName The name of the interface.  EG: If a list of
     *  org.xbrlapi.impl.ReferenceArcImpl fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that if the parameter contains full stops, then it is used directly
     *  as the value for the fragment type, enabling fragment extensions to exploit this
     *  method without placing fragment implementations in the org.xbrlapi package.
     *  
     * @return a list of XML resources with the given fragment type.
     * @throws XBRLException
     */
    public <F extends XML> List<F> getXMLResources(String interfaceName) throws XBRLException;
    
    /**
     * @param interfaceName The name of the interface.  EG: If a list of
     *  org.xbrlapi.impl.ReferenceArcImpl fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an Arc would not return all ReferenceArcs as well as other
     *  types of arcs.
     *  @param parentIndex The index of the parent fragment.
     * @return a list of fragments with the given fragment type 
     * and with the given parent fragment.  The list is empty if there
     * are not child fragments.
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getChildFragments(String interfaceName, String parentIndex) throws XBRLException;

    /**
     * @param linkRole The linkrole of the networks.
     * @param arcrole The arcrole of the networks.
     * @return the collection of networks expressed using arcs that involve this 
     * linkrole and arcrole.
     * @throws XBRLException
     */
    public Networks getNetworks(URI linkRole, URI arcrole) throws XBRLException;
    

    

    /**
     * @param arcrole The XLink arcrole value.
     * @return the collection of networks expressed using arcs 
     * that involve this arc role.
     * @throws XBRLException
     */
    public Networks getNetworks(URI arcrole) throws XBRLException;
    
    /**
     * Note that this can massively overload resources if the 
     * data store is large because much of the information in the 
     * store will be reflected in -in-memory objects.  Use this method
     * with care.
     * @return the collection of all networks in the store.
     * @throws XBRLException
     */
    public Networks getNetworks() throws XBRLException;    


    

    /**
     * @param <F> The type of fragment.
     * @param sourceIndex The index of the source fragment.
     * @param linkRole The XLink link role.
     * @param arcrole The XLink arcrole. 
     * @return the list of targets from the specified source where the
     * relationship has the given link role and arcrole. If the link role
     * is null then targets of relationships with any linkrole are returned. If the 
     * arcrole is null then targets of relationships with any arcrole are returned.
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getTargets(String sourceIndex, URI linkRole, URI arcrole) throws XBRLException;
    
    /**
     * @param <F> The type of fragment.
     * @param targetIndex The index of the target fragment.
     * @param linkRole The XLink link role.
     * @param arcrole The XLink arcrole. 
     * @return the set of sources related to the specified target where the
     * relationship has the given link role and arcrole. If the link role
     * is null then sources of relationships with any linkrole are returned. If the 
     * arcrole is null then sources of relationships with any arcrole are returned.
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getSources(String targetIndex, URI linkRole, URI arcrole) throws XBRLException;    
    
    /**
     * @param analyser The persisted network analyser
     * to use for network analysis when building the
     * aspect model.  Set to null if you do not want to 
     * build the aspect model using persisted network information.
     */
    public void setAnalyser(Analyser analyser);
    

    
    /**
     * @return the persisted network analyser if one is being used
     * and null otherwise.
     */
    public Analyser getAnalyser(); 
    
    /**
     * @return true if the store is using
     * persisted network information rather than using the 
     * raw network information embodied in XBRL fragments. Returns
     * false otherwise.
     * @see org.xbrlapi.networks.Analyser
     */
    public boolean isPersistingRelationships();    

    /**
     * Utility method to return a list of fragments in a data store
     * that have a type corresponding to the specified fragment interface name and
     * that are in the document with the specified URI.
     * @param uri The URI of the document to get the fragments from.
     * @param interfaceName The name of the interface.  EG: If a list of
     *   fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an Arc would not return all ReferenceArcs as well as other
     *  types of arcs.
     * @return a list of fragments with the given fragment type and in the given document.
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getFragmentsFromDocument(URI uri, String interfaceName) throws XBRLException;
    

    /**
     * @param interfaceName The name of the interface.  EG: If a list of
     *  Concept fragments is required then this parameter would have a value of "Concept".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an ElementDeclaration would not return all concepts as well as
     *  other XML Schema element declarations.
     * @return a list of fragment indices with the given fragment type and in the given document.
     * @throws XBRLException
     */    public Set<String> getFragmentIndices(String interfaceName) throws XBRLException;
    
    /**
     * @param uri The URI of the document to get the fragments from.
     * @param interfaceName The name of the interface.  EG: If a list of
     *  concept fragments is required then
     *  this parameter would have a value of "Concept".
     *  Note that this method does not yet recognise fragment subtypes so 
     *  a request for an ElementDeclaration would not return all concepts as well as
     *  other XML Schema element declarations.
     * @return a list of fragment indices with the given fragment type and in the given document.
     * @throws XBRLException
     */
    public Set<String> getFragmentIndicesFromDocument(URI uri, String interfaceName) throws XBRLException;  

    /**
     * @param <F> The fragment extension class
     * @param uri The URI of the document to get the root fragment for.
     * @return the root fragment of the document with the given URI or null if no
     * root fragment is available for the given URI.
     * @throws XBRLException if more than one root fragment is found in the data store.
     */
    public <F extends Fragment> F getRootFragmentForDocument(URI uri) throws XBRLException;

    /**
     * @param <F> The fragment extension class
     * @return the list of root fragments of the documents in the store.
     * @throws XBRLException if more than one root fragment is found in the data store.
     */
    public <F extends Fragment> List<F> getRootFragments() throws XBRLException;
    
    /**
     * @param string The type of fragment to select by.
     * @see {@link Store#getXMLResources(String)} for details on how the type
     * parameter is to be used.
     * @param <F> The fragment extension class
     * @return the list of root fragments of the documents in the store where the root fragments
     * are of the specified type.
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getRootFragments(String type) throws XBRLException;    
    
    /**
     * Close and then delete the data store.
     * This method must be synchronized.
     * @throws XBRLException if the data store cannot be deleted.
     */
    public void delete() throws XBRLException;    

    /**
     * @param input The string that may be used to generate the id.
     * @return the ID to be used for storing XML resources in the data store.
     * One of these IDs is generated for each document in the data store and 
     * then a counter is appended to that ID to get uniqueness for each fragment
     * in that document.  These IDs are also used for a variety of other kinds
     * of XML resources stored in the data store.
     * @throws XBRLException
     */
    public String getId(String input) throws XBRLException;
    
    /**
     * @param encoding The code identifying the language that the name of the
     * language is expressed in.
     * @param code The code that identifies the language being named.
     * @return the Language fragment that specifies the name of the language
     * for the given code, expressed in the language identified by the encoding or 
     * null or if there is no matching language name 
     * in the data store.  The input parameters are converted to upper case before
     * processing.
     * @throws XBRLException if either parameter equals null.
     */
    public Language getLanguage(String encoding, String code) throws XBRLException;
    
    /**
     * @param code The language code to get the language fragments for.  The code
     * is converted to upper case before processing.
     * @return the list of language fragments giving names for the language
     * associated with the specified language code.
     * @throws XBRLException if the language code is null.
     */
    public List<Language> getLanguages(String code) throws XBRLException;    
    
    /**
     * Sets the matcher for the store to use.  Care should be taken to ensure
     * that the one matcher is used for all documents in the store.
     * @param matcher the matcher to use to identify identical resources.
     * @throws XBRLException if the matcher is null;
     */
    public void setMatcher(Matcher matcher) throws XBRLException;
    
    /**
     * @return the matcher used by the store to identify identical resources.
     */
    public Matcher getMatcher();
    
    /**
     * Override this method in a data store implementation if the data store 
     * implementation supports XQuery (rather than XPath).
     * 
     * @param uri The URI of the referenced document.
     * @return a list of the URIs of the documents directly referencing
     * the specified document as targets of their XLinks (custom or otherwise).
     * @throws XBRLException if the list of referencing documents cannot be populated.
     */
    public List<URI> getReferencingDocuments(URI uri) throws XBRLException;
    
    /**
     * Override this method in a data store implementation if the data store 
     * implementation supports XQuery (rather than XPath).
     * 
     * @param uri The URI of the referencing document.
     * @return a list of the documents directly referenced by this document.
     * @throws XBRLException if the list of referenced documents cannot be populated.
     */
    public List<URI> getReferencedDocuments(URI uri) throws XBRLException;    
    
    /**
     * @param uris The list of URIs to restrict query results to coming from.
     */
    public void setFilteringURIs(List<URI> uris);

    /**
     * @return the list of URIs to filter query results using or
     * null if no such list of URIs is being used by the data store.
     */
    public List<URI> getFilteringURIs();
    
    /**
     * Specify that the data store is not to filter query results to only come
     * from a specified set of URIs.
     */
    public void clearFilteringURIs();

    /**
     * @return true if the data store is restricting query results to come 
     * from a specific set of documents and false otherwise.
     */
    public boolean isFilteringByURIs();


    



 
    /**
     * Flush all database updates to the data store. 
     * @throws XBRLException if the sync operation fails.
     */
    public void sync() throws XBRLException;


    /**
     * @return a list of all of the root-level facts in the data store (those facts
     * that are children of the root element of an XBRL instance).  Returns an empty list 
     * if no facts are found.
     * @throws XBRLException
     */
    public List<Fact> getFacts() throws XBRLException;
    
    /**
     * @return a list of all of the items in the data store.
     * @throws XBRLException
     */
    public List<Item> getItems() throws XBRLException;
    
    /**
     * @return a list of all of the tuples in the data store.
     * @throws XBRLException
     */
    public List<Tuple> getTuples() throws XBRLException;

    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level facts in the specified document.
     * @throws XBRLException
     */
    public List<Fact> getFacts(URI uri) throws XBRLException;
    
    /**
     * @param uri The URI of the document to get the items from.
     * @return a list of all of the root-level items in the data store.
     * @throws XBRLException
     */
    public List<Item> getItems(URI uri) throws XBRLException;
    
    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level tuples in the specified document.
     * @throws XBRLException
     */
    public List<Tuple> getTuples(URI uri) throws XBRLException;
    
    /**
     * This implementation is not as strict as the XBRL 2.1 specification
     * requires but it is generally faster and delivers sensible results.
     * It will only fail if people use the same link role and arc role but
     * rely on arc or link element differences to distinguish networks.<br/><br/>
     * 
     * Implementation strategy is:<br/>
     * 1. Get all extended link elements with the given link role.<br/>
     * 2. Get all arcs with the given arc role.<br/>
     * 3. Get all resources at the source of the arcs.<br/>
     * 4. Return only those source resources that that are not target resources also.<br/>
     * 
     * @param linkRole the role on the extended links that contain the network arcs.
     * @param arcrole the arcrole on the arcs describing the network.
     * @return The list of fragments for each of the resources that is identified as a root
     * of the specified network (noting that a root resource is defined as a resource that is
     * at the source of one or more relationships in the network and that is not at the target 
     * of any relationships in the network).
     * @throws XBRLException
     */
    public <F extends Fragment> Set<F> getNetworkRoots(URI linkRole, URI arcrole) throws XBRLException;
   
    /**
     * @param namespace The namespace for the concept.
     * @param name The local name for the concept.
     * @return the concept fragment for the specified namespace and name.
     * @throws XBRLException if more than one matching concept is found in the data store
     * or if no matching concepts are found in the data store.
     */
    public Concept getConcept(URI namespace, String name) throws XBRLException;

    /**
     * @return a list of arcroleType fragments
     * @throws XBRLException
     */
    public List<ArcroleType> getArcroleTypes() throws XBRLException;
    
    /**
     * @return a list of arcroleType fragments with a given arcrole
     * @throws XBRLException
     */
    public List<ArcroleType> getArcroleTypes(URI uri) throws XBRLException;
    
    /**
     * @return a list of roleType fragments
     * @throws XBRLException
     */
    public List<RoleType> getRoleTypes() throws XBRLException;
    

    
    /**
     * @return a list of RoleType fragments with a given role
     * @throws XBRLException
     */
    public List<RoleType> getRoleTypes(URI uri) throws XBRLException;    
    
    /**
     * @return a hash map indexed by resource roles that are used in extended links in the data store.
     * @throws XBRLException
     */
    public List<URI> getResourceRoles() throws XBRLException;    
    
    /**
     * @param starters The list of URIs of the documents to use as 
     * starting points for analysis.
     * @return list of URIs for the documents in the data store
     * that are referenced, directly or indirectly, by any of the documents
     * identified by the supplied list of document URIs.  Each entry in the list is a String.
     * @throws XBRLException if some of the referenced documents are not in
     * the data store.
     */
    public List<URI> getMinimumDocumentSet(List<URI> starters) throws XBRLException;
    
    
    /**
     * This is just a convenience method.
     * @param uri The single document URI to use as 
     * starting points for analysis.
     * @return list of URIs for the documents in the data store
     * that are referenced, directly or indirectly, by the document
     * identified by the supplied URI.  Each entry in the list is a String.
     * @throws XBRLException if some of the referenced documents are not in
     * the data store.
     */
    public List<URI> getMinimumDocumentSet(URI uri) throws XBRLException;


 
    /**
     * @param linkrole The required linkrole value.
     * @return the list of extended links with the specified linkrole.
     * @throws XBRLException
     */
    public List<ExtendedLink> getExtendedLinks(URI linkrole) throws XBRLException;
    
    /**
     * @param linkRole The link role to use to identify the extended links to retrieve.
     * @return the list of indices of extended links with the given link role value.
     * @throws XBRLException
     */
    public Set<String> getExtendedLinkIndices(URI linkRole) throws XBRLException;
    
    /**
     * @param arcrole The arcrole to use to identify the arcs to retrieve.
     * @param linkIndex The index of the extended link containing the arcs to retrieve.
     * @return the list of indices of arcs matching the selection criteria.
     * @throws XBRLException
     * @return the list of arc fragments matching the selection criteria.
     * @throws XBRLException
     */
    public List<Arc> getArcs(URI arcrole, String linkIndex) throws XBRLException;
    
    /**
     * @param linkIndex The index of the extended link containing the arcs to retrieve.
     * @return the list of indices of arcs matching the selection criteria.
     * @throws XBRLException
     */
    public Set<String> getArcIndices(String linkIndex) throws XBRLException;
    
    /**
     * @param arcrole The arcrole to use to identify the arcs to retrieve.
     * @return the list of indices of arcs with a given arc role value.
     * @throws XBRLException
     */
    public Set<String> getArcIndices(URI arcrole) throws XBRLException;    
    
    
    /**
     * @param linkIndex The index of the extended link containing the arcs to retrieve.
     * @return the list of indices of arcs matching the selection criteria.
     * @throws XBRLException
     * @return the list of arc fragments matching the selection criteria.
     * @throws XBRLException
     */
    public List<Arc> getArcs(String linkIndex) throws XBRLException;    
    

    /**
     * Get the networks that, at a minimum, contain the relationships
     * from each of the given fragments working back through ancestor relationships
     * as far as possible.  This is useful for building up networks of relationships
     * where you know the leaf nodes you want and need to get the necessary branches back to the
     * relevant heirarchy roots but do not want any branches leading to other leaf nodes.
     * The method only generates networks using active relationships (not overridden or
     * prohibited relationships).
     * @param fragments The fragments to analyse.
     * @param arcrole The required arcrole.
     * @return The networks containing the relationships.
     * @throws XBRLException
     */
    public Networks getMinimalNetworksWithArcrole(Set<Fragment> fragments, URI arcrole) throws XBRLException;
    
    /**
     * Convenience method for a single fragment.
     * @see Store#getMinimalNetworksWithArcrole(Set,URI)
     */
    public Networks getMinimalNetworksWithArcrole(Fragment fragment, URI arcrole) throws XBRLException;

    /**
     * @return a list of arc roles that are used in extended links in the data store.
     * @throws XBRLException
     */
    public Set<URI> getArcroles() throws XBRLException;
    
    /**
     * @param linkRole the specified linkrole to use in selecting arcroles.
     * @return a list of arc roles that are used in extended links 
     * with the given link role.
     * @throws XBRLException if any of the arcroles is not a valid URI.
     */
    public Set<URI> getArcroles(URI linkRole) throws XBRLException;    

    /**
     * @return a hash map indexed by link roles that are used in extended links in the data store.
     * @throws XBRLException
     */
    public Set<URI> getLinkRoles() throws XBRLException;

    /**
     * @param arcrole The arcrole determining the extended links that are to be examined for
     * linkroles that are used on links containing arcs with the required arcrole.
     * @return a hashmap of link roles, with one entry for each link role that is used on an
     * extended link that contains an arc with the required arcrole.
     * @throws XBRLException
     */
    public Set<URI> getLinkRoles(URI arcrole) throws XBRLException;    

    /**
     * Implemented by {@link Store#getNetworksFrom(String,URI,URI)}.
     * @param sourceIndex The source fragment index
     * @param arcrole The XLink arcrole
     * @return a set of networks comprising the relationships
     * from the source fragment with the given arcrole.
     * @throws XBRLException
     */
    public Networks getNetworksFrom(String sourceIndex, URI arcrole) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getNetworksFrom(String,URI,URI)}.
     * @param sourceIndex The source fragment index
     * @return a set of networks comprising the relationships
     * from the source fragment.
     * @throws XBRLException
     */
    public Networks getNetworksFrom(String sourceIndex) throws XBRLException;    
    
    /**
     * @param sourceIndex The source fragment index
     * @param linkRole The XLink link role
     * @param arcrole The XLink arcrole
     * @return a set of networks comprising the relationships
     * from the source fragment with the given link role and arcrole.
     * @throws XBRLException
     */
    public Networks getNetworksFrom(String sourceIndex, URI linkRole, URI arcrole) throws XBRLException;    
    
    /**
     * If using persisted relationships then the set of relationships used to 
     * generate the results can be modified by appropriate choice of 
     * @link org.xbrlapi.networks.Analyser implementation.  Otherwise only active
     * relationships are used (those that are not prohibited or over-ridden).
     * @param targetIndex The target fragment index
     * @param linkRole The XLink link role or null if networks for 
     * all link roles are sought
     * @param arcrole The XLink arcrole  or null if networks for 
     * all arcroles are sought
     * @return a set of networks comprising the relationships
     * to the target fragment meeting the specified criteria.
     * @throws XBRLException
     */
    public Networks getNetworksTo(String targetIndex, URI linkRole, URI arcrole) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getNetworksTo(String,URI,URI)}.
     * @param targetIndex The target fragment index
     * @param arcrole The XLink arcrole
     * @return a set of networks comprising the relationships
     * to the target fragment with the given arcrole.
     * @throws XBRLException
     */
    public Networks getNetworksTo(String targetIndex, URI arcrole) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getNetworksTo(String,URI,URI)}.
     * @param targetIndex The target fragment index
     * @return a set of networks comprising the relationships
     * to the target fragment.
     * @throws XBRLException
     */
    public Networks getNetworksTo(String targetIndex) throws XBRLException;    
    
    


    
    /**
     * If using persisted relationships then the set of relationships used to 
     * generate the results can be modified by appropriate choice of 
     * @link org.xbrlapi.networks.Analyser implementation.  Otherwise only active
     * relationships are used (those that are not prohibited or over-ridden).
     * @param sourceIndex The source fragment index
     * @param linkRole The XLink link role
     * @param arcrole The XLink arcrole
     * @return a sorted set of active relationships from the 
     * source fragment with the given link role and arcrole that
     * have been persisted.  The relationships are ordered by the order
     * attributes on the arcs expressing the relationships.
     * @throws XBRLException
     */
    public SortedSet<Relationship> getRelationshipsFrom(String sourceIndex,URI linkRole, URI arcrole) throws XBRLException;
    
    /**
     * @param document The document URI.
     * @return true if the store contains persisted relationships
     * for all of the relationships expressed by the arcs in the 
     * specified document and false otherwise.
     * @throws XBRLException
     */
    public boolean hasAllRelationships(URI document) throws XBRLException;    
    

    
    /**
     * @param targetIndex The target fragment index
     * @param linkRole The XLink link role
     * @param arcrole The XLink arcrole
     * @return a sorted set of active relationships to the 
     * target fragment with the given link role and arcrole.
     * The relationships are ordered by the order attribute on 
     * the arcs expressing them.
     * @throws XBRLException
     */
    public SortedSet<Relationship> getRelationshipsTo(String targetIndex,URI linkRole, URI arcrole) throws XBRLException;    



    /**
     * If using persisted relationships then the set of relationships used to 
     * generate the results can be modified by appropriate choice of 
     * @link org.xbrlapi.networks.Analyser implementation.  Otherwise only active
     * relationships are used (those that are not prohibited or over-ridden).
     * @param fragment the index of the fragment that we are getting labels for
     * @param linkRole The required link role or null if not used.
     * @param resourceRole The required resource role or null if not used.
     * @param language The required language code or null if not used.
     * @return the set of labels matching the specified criteria.
     * @throws XBRLException
     */
    public List<LabelResource> getLabels(String fragment, URI linkRole, URI resourceRole, String language) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getLabels(String,URI,URI,String)}.
     * @param fragment the index of the fragment that we are getting labels for
     * @param resourceRole The required resource role or null if not used.
     * @param language The required language code or null if not used.
     * @return the set of labels matching the specified criteria.
     * @throws XBRLException
     */
    public List<LabelResource> getLabels(String fragment, URI resourceRole, String language) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getLabels(String,URI,URI,String)}.
     * @param fragment the index of the fragment that we are getting labels for
     * @param language The required language code or null if not used.
     * @return the set of labels matching the specified criteria.
     * @throws XBRLException
     */
    public List<LabelResource> getLabels(String fragment, String language) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getLabels(String,URI,URI,String)}.
     * @param fragment the index of the fragment that we are getting labels for
     * @return the set of labels matching the specified criteria.
     * @throws XBRLException
     */
    public List<LabelResource> getLabels(String fragment) throws XBRLException;    
    
    /**
     * Implemented by {@link Store#getLabels(String,URI,URI,String)}.
     * @param fragment the index of the fragment that we are getting labels for
     * @param resourceRole The required resource role or null if not used.
     * @return the set of labels matching the specified criteria.
     * @throws XBRLException
     */
    public List<LabelResource> getLabels(String fragment, URI resourceRole) throws XBRLException;
    

    
    /**
     * If using persisted relationships then the set of relationships used to 
     * generate the results can be modified by appropriate choice of 
     * @link org.xbrlapi.networks.Analyser implementation.  Otherwise only active
     * relationships are used (those that are not prohibited or over-ridden).
     * @param fragment the index of the fragment that we are getting references for
     * @param linkRole The required link role or null if not used.
     * @param resourceRole The required resource role or null if not used.
     * @param language The required language code or null if not used.
     * @return the set of references matching the specified criteria.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferences(String fragment, URI linkRole, URI resourceRole, String language) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getReferences(String,URI,URI,String)}.
     * @param fragment the index of the fragment that we are getting references for
     * @param resourceRole The required resource role or null if not used.
     * @param language The required language code or null if not used.
     * @return the set of references matching the specified criteria.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferences(String fragment, URI resourceRole, String language) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getReferences(String,URI,URI,String)}.
     * @param fragment the index of the fragment that we are getting references for
     * @param language The required language code or null if not used.
     * @return the set of references matching the specified criteria.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferences(String fragment, String language) throws XBRLException;
    
    /**
     * Implemented by {@link Store#getReferences(String,URI,URI,String)}.
     * @param fragment the index of the fragment that we are getting references for
     * @return the set of references matching the specified criteria.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferences(String fragment) throws XBRLException;    
    
    /**
     * Implemented by {@link Store#getReferences(String,URI,URI,String)}.
     * @param fragment the index of the fragment that we are getting references for
     * @param resourceRole The required resource role or null if not used.
     * @return the set of references matching the specified criteria.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferences(String fragment, URI resourceRole) throws XBRLException;    
    
    /**
     * @return a list of the URIs of documents that are discoverable given the 
     * content of the data store but that are not themselves in the data store.
     * @throws XBRLException
     */
    public Set<URI> getMissingDocumentURIs() throws XBRLException;

    /**
     * Loaders need to call this method to indicate that they are going to take 
     * responsibility for loading the document.
     * This can be used to prevent the same document from being loaded by several
     * loaders operating in parallel.
     * @param loader The loader claiming loading rights.
     * @param document The URI of the document that a loader is about
     * to start loading.
     * @return false if the document is already claimed by a different loader and true otherwise.  
     * Only start loading if this function returns true.
     * @throws XBRLException
     */
    public boolean requestLoadingRightsFor(Loader loader, URI document) throws XBRLException;
    
    /**
     * Loaders need to call this method to indicate that they are recinding 
     * responsibility for loading the document.
     * This can be used to prevent the same document from being loaded by several
     * loaders operating in parallel.
     * @param loader The loader recinding loading rights.
     * @param document The URI of the document whose loading rights are being recinded.
     * @link Store#loadingAboutToStart(URI)
     */
    public void recindLoadingRightsFor(Loader loader, URI document);
}
