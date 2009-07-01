package org.xbrlapi.loader;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import org.xbrlapi.cache.Cache;
import org.xbrlapi.data.Store;
import org.xbrlapi.networks.Storer;
import org.xbrlapi.networks.StorerImpl;
import org.xbrlapi.sax.ContentHandlerImpl;
import org.xbrlapi.sax.EntityResolverImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.ElementState;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xpointer.resolver.PointerResolver;
import org.xbrlapi.xpointer.resolver.PointerResolverImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Implementation of the XBRL API Loader interface. The loader is responsible
 * for managing the DTS discovery process. It manages:
 * <ul>
 * <li>the queue of documents to be explored</li>
 * <li>the document discovery process</li>
 * <li>the stack of fragments being built and stored</li>
 * <li>the fragment naming scheme</li>
 * </ul>
 * 
 * Features of the loader include:
 * <ul>
 * <li>The ability to interupt the document discovery process
 * between documents without losing track of the documents
 * remaining to be discovered.  Interrupts can be requested and 
 * cancelled.</li>
 * <li>The ability to continue discovery even when some documents
 * screw up and need to be eliminated from the data store. In such
 * cases, a stub fragment will be stored in the data store for 
 * each problematic document, containing the document URI and a 
 * brief explanation of the nature of the problem.  This 
 * simplifies data store cleansing and it helps with making repairs to 
 * the source data.</li>
 * </ul>
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LoaderImpl implements Loader {

    // Create the logger
    static Logger logger = Logger.getLogger(LoaderImpl.class);

    /**
     * The data store to be used to hold the DTS.
     */
    private Store store;

    /**
     * The stack of fragments that are being built
     */
    private Stack<Fragment> fragments = new Stack<Fragment>();

    /**
     * A stack of element states, one per root element
     * of a fragment currently undergoing construction.
     */
    Stack<ElementState> states = new Stack<ElementState>();
    
    /**
     * @return the stack of element states for the root
     * elements of the fragments currently being built.
     */
    private Stack<ElementState> getStates() {
        return this.states;
    }    
    
    /**
     * The cache to use when discovering XML materials specified as a String
     * rather than just via a URI that resolves to the required XML.
     */
    private Cache cache = null;

    /**
     * @see org.xbrlapi.loader.Loader#setCache(Cache)
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * @see org.xbrlapi.loader.Loader#getCache()
     */
    public Cache getCache() throws XBRLException {
        if (this.cache == null)
            throw new XBRLException(
                    "The loader cache is null and so cannot be used.");
        return this.cache;
    }

    /**
     * The absolute URI of the document currently being parsed. Used to record
     * this metadata in each fragment.
     */
    private URI documentURI = null;

    /**
     * The document Id (including the document hash and its counter)
     */
    private String documentId = null;



    /**
     * The Xlink processor
     */
    private XLinkProcessor xlinkProcessor;

    /**
     * The entity resolver to use for resolution of entities (URIs etc) during
     * the loading/discovery process.  Defaults to one without a caching system.
     */
    private EntityResolver entityResolver = new EntityResolverImpl();

    /**
     * The XPointer resolver
     */
    private PointerResolver pointerResolver;

    /**
     * The sorted map of documents that have failed to load.
     * Each URI points to value that is the reason for the failure.
     */
    private TreeMap<URI, String> failures = new TreeMap<URI, String>();
    
    /**
     * The sorted set of documents that have successfully been loaded.
     */
    private TreeSet<URI> successes = new TreeSet<URI>();    
    
    private TreeSet<URI> documentQueue = new TreeSet<URI>();

    /**
     * The unique fragment ID, that will be one for the first fragment. This is
     * incremented just before as it is retrieved for use with a new fragment
     * created during the loading process.
     */
    private int fragmentId = 0;

    /**
     * discovering equals false if the loader is not presently doing document
     * discovery and true otherwise.
     */
    private boolean discovering = false;

    private void setDiscovering(boolean value) {
        if (value) logger.debug(Thread.currentThread().getName() + " starting discovery.");
        else logger.debug(Thread.currentThread().getName() + " stopping discovery.");
        discovering = value;
    }
    
    /**
     * @see Loader#isDiscovering()
     */
    public boolean isDiscovering() {
        return discovering;
    }

    private boolean interrupt = false;

    /**
     * @see Loader#requestInterrupt()
     */
    public void requestInterrupt() {
        interrupt = true;
    }
    
    /**
     * @return true if an interrupt to the loading process 
     * has been requested and false otherwise.
     */
    private boolean interruptRequested() {
        return interrupt;
    }

    /**
     * @see org.xbrlapi.loader.Loader#cancelInterrupt()
     */
    public void cancelInterrupt() {
        interrupt = false;
    }
    
    /**
     * @param store The data store to hold the DTS
     * @param xlinkProcessor The XLink processor to use for link resolution
     * @throws XBRLException if the loader cannot be instantiated.
     */
    public LoaderImpl(Store store, XLinkProcessor xlinkProcessor)
            throws XBRLException {
        super();
        setStore(store);
        setXlinkProcessor(xlinkProcessor);
        this.setPointerResolver(new PointerResolverImpl(getStore()));
    }

    /**
     * @param store The data store to hold the DTS
     * @param xlinkProcessor The XLink processor to use for link resolution
     * @param uris The array of URIs for loading.
     * @throws XBRLException if the loader cannot be instantiated.
     */
    public LoaderImpl(Store store, XLinkProcessor xlinkProcessor, List<URI> uris)
            throws XBRLException {
        this(store, xlinkProcessor);
        setStartingURIs(uris);
    }



    /**
     * Set the data store to be used by the loader.
     * @throws XBRLException if the given store is null.
     */
    private void setStore(Store store) throws XBRLException {
        if (store == null) {
            throw new XBRLException("The data store must not be null.");
        }
        this.store = store;
    }

    /**
     * Get the data store used by a loader.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Set the URI of the document now being parsed and set the
     * document ID for the document being parsed by using the store
     * to generate the ID from the URI.  The document ID is used as
     * part of the fragment naming scheme in the data store.
     * @param uri The URI of the document now being parsed.
     * @throws XBRLException.
     */
    private void setDocumentURI(URI uri) throws XBRLException {
        documentURI = uri;
        documentId = getStore().getDocumentId(uri);
    }
    
    /**
     * @return the document ID for the document being analysed or
     * null if no document is being analysed.
     */
    private String getDocumentId() {
        return documentId;
    }

    /**
     * Get the URI for the document being parsed.
     * @return The original (non-cache) URI of the document being parsed.
     */
    public URI getDocumentURI() {
        return this.documentURI;
    }

    /**
     * Set the XLink processor used by the loader.
     * @throws XBRLException if the given XLink processor is null.
     */
    private void setXlinkProcessor(XLinkProcessor xlinkProcessor) throws XBRLException {
        if (xlinkProcessor == null) {
            throw new XBRLException("The XLink processor must not be null.");
        }
        this.xlinkProcessor = xlinkProcessor;
    }

    /**
     * Get the Xlink processor used by the loader. TODO Should the getter for
     * the loader Xlink Processor be public?
     */
    public XLinkProcessor getXlinkProcessor() {
        return xlinkProcessor;
    }

    /**
     * Set the XPointer resolver.
     * 
     * @param pointerResolver
     *            The XPointer resolver implementation.
     */
    private void setPointerResolver(PointerResolver pointerResolver) {
        this.pointerResolver = pointerResolver;
    }

    /**
     * Get the XPointer resolver.
     * 
     * @return The XPointer resolver used by the loader.
     */
    private PointerResolver getPointerResolver() {
        return pointerResolver;
    }











    /**
     * @see Loader#updateState(ElementState)
     */
    public void updateState(ElementState state) throws XBRLException {

        if (getStates().peek() == state) {
            this.removeFragment();
        }
    }

    /**
     * @see org.xbrlapi.loader.Loader#getFragment()
     */
    public Fragment getFragment() throws XBRLException {
        if (fragments.isEmpty()) return null;
        return fragments.peek();
    }
    /**
     * @see org.xbrlapi.loader.Loader#isBuildingAFragment()
     */
    public boolean isBuildingAFragment() {
        return (!fragments.isEmpty());
    }

    /**
     * @see org.xbrlapi.loader.Loader#add(Fragment, ElementState)
     */
    public void add(Fragment fragment, ElementState state)
            throws XBRLException {

        // Get the XPointer expressions that identify the root of this fragment
        // TODO Should the following xpointer code be contingent on children != null?
        Vector<String> pointers = state.getElementSchemePointers();
        for (String pointer : pointers) {
            fragment.appendElementSchemeXPointer(pointer);
        }

        // Set the document reconstruction metadata for the fragment
        Fragment parent = getFragment();
        if (parent != null) {
            String parentIndex = parent.getIndex();
            if (parentIndex == null) throw new XBRLException("The parent index is null.");
            fragment.setParentIndex(parentIndex);
            fragment.setSequenceToParentElement(parent);
        } else {
            fragment.setParentIndex("");
        }

        fragment.setURI(getDocumentURI());

        // Push the fragment onto the stack of fragments
        fragments.add(fragment);

        // Push the element state onto the stack of fragment root element states
        getStates().add(state);

    }
    


    /**
     * Remove a fragment from the stack of fragments that 
     * are being built by the loader.
     * @throws XBRLException if their are no fragments being built.
     */
    private Fragment removeFragment() throws XBRLException {
        try {
            
            getStates().pop();
//            getChildrenStack().pop();
            Fragment f = fragments.pop();
            getStore().persist(f);
            return f;
        } catch (EmptyStackException e) {
            throw new XBRLException(this.getDocumentURI() + " There are no fragments being built.  The stack of fragments is empty.",e);
        }
    }

    /**
     * @see Loader#discover(List)
     */
    public void discover(List<URI> startingURIs) throws XBRLException {
        for (URI uri: startingURIs) stashURI(uri);
        discover();
    }

    /**
     * @see org.xbrlapi.loader.Loader#discover(URI)
     */
    public void discover(URI uri) throws XBRLException {
        stashURI(uri);
        discover();
    }

    /**
     * @see org.xbrlapi.loader.Loader#discover(String)
     */
    public void discover(String uri) throws XBRLException {
        try {
            discover(new URI(uri));
        } catch (URISyntaxException e) {
            throw new XBRLException("The URI to discover, " + uri + " is malformed.", e);
        }
    }

    /**
     * @see org.xbrlapi.loader.Loader#getDocumentsStillToAnalyse()
     */
    public List<URI> getDocumentsStillToAnalyse() {
        List<URI> documents = new Vector<URI>();
        documents.addAll(documentQueue);
        return documents;
    }


    /**
     * @see org.xbrlapi.loader.Loader#discover()
     */
    public void discover() throws XBRLException {
        
        Set<URI> newDocuments = new TreeSet<URI>();
        
        int discoveryCount = 1;

        if (isDiscovering()) {
            logger.warn("The loader is already doing discovery so starting discovery achieves nothing.");
            return;
        }
        setDiscovering(true);

        for (URI uri: getStore().getDocumentsToDiscover()) {
            logger.info(uri + " stashed for discovery.");
            this.stashURI(uri);
        }
        
        URI uri = getNextDocumentToExplore();
        DOCUMENTS: while (uri != null) {

            long start = System.currentTimeMillis();

            if (!getStore().hasDocument(uri)) {
                setDocumentURI(uri);
                this.setNextFragmentId("1");
                try {
                    parse(uri);
                    long duration = (System.currentTimeMillis() - start) / 1000;
                    logger.info("#" + discoveryCount + " took " + duration + " seconds. " + (fragmentId-1) + " fragments in " + uri);
                    discoveryCount++;
                    markDocumentAsExplored(uri);
                    newDocuments.add(uri);
                    getStore().sync();
                } catch (XBRLException e) {
                    this.cleanupFailedLoad(uri,"XBRL Problem: " + e.getMessage(),e);
                } catch (SAXException e) {
                    this.cleanupFailedLoad(uri,"SAX Problem: " + e.getMessage(),e);
                } catch (IOException e) {
                    this.cleanupFailedLoad(uri,"IO Problem: " + e.getMessage(),e);
                } catch (ParserConfigurationException e) {
                    throw new XBRLException("The parser could not be correctly configured.",e);
                }
            } else {
                logger.debug(uri + " is already in the data store.");
                markDocumentAsExplored(uri);
            }

            if (interruptRequested()) {
                cancelInterrupt();
                break DOCUMENTS;
            }

            uri = getNextDocumentToExplore();
        }

        storeDocumentsToAnalyse();
        setDiscovering(false);

        if (documentQueue.size() == 0 && failures.size() == 0) {
            logger.info("Document discovery completed successfully.");
        } else {
            if (failures.size() > 0) {
                logger.warn("Some documents failed to load.");
            }
            if (documentQueue.size() > 0) {
                logger.info("Document discovery exited without completing.");
            }
        }
        
        if (getStore().isUsingPersistedNetworks()) {
            logger.info("Starting to persist the relationships.");
            Storer storer = new StorerImpl(getStore());
            storer.storeRelationships(newDocuments);
            getStore().sync();
            logger.info("Done with persisting the relationships.");
        }

        failures = new TreeMap<URI,String>();
        documentQueue = new TreeSet<URI>();
        
    }

    /**
     * @see org.xbrlapi.loader.Loader#discoverNext()
     */
    public void discoverNext() throws XBRLException {

        Store store = this.getStore();
        
        if (isDiscovering()) {
            logger.warn("Already discovering data with this loader so loader will not discover next document as requested.");
            return;
        }
        setDiscovering(true);

        URI uri = getNextDocumentToExplore();
        while (store.hasDocument(uri) && (uri != null)) {
            this.markDocumentAsExplored(uri);
            uri = getNextDocumentToExplore();
        }

        if (uri != null) {
            logger.debug("Now parsing " + uri);
            setDocumentURI(uri);
            this.setNextFragmentId("1");
            try {
                parse(uri);
                markDocumentAsExplored(uri);
                getStore().sync();
                logger.info(this.fragmentId + " fragments in " + uri);
            } catch (XBRLException e) {
                this.cleanupFailedLoad(uri,"XBRL Problem: " + e.getMessage(),e);
            } catch (SAXException e) {
                this.cleanupFailedLoad(uri,"SAX Problem: " + e.getMessage(),e);
            } catch (IOException e) {
                this.cleanupFailedLoad(uri,"IO Problem: " + e.getMessage(),e);
            } catch (ParserConfigurationException e) {
                throw new XBRLException("The parser could not be correctly configured.",e);
            }
        }

        logger.info("Finished discovery of " + uri);
        this.storeDocumentsToAnalyse();
        
        setDiscovering(false);

    }

    /**
     * Perform a discovery starting with an XML document that is represented as
     * a string.
     * 
     * @param uri
     *            The URI to be used for the document that is supplied as a
     *            string. This URI MUST be an absolute URI.
     * @param xml
     *            The string representation of the XML document to be parsed.
     * @throws XBRLException
     *             if the discovery process fails or if the supplied URI is not
     *             absolute or is not a valid URI syntax or the loader does not
     *             have a cache.
     */
    public void discover(URI uri, String xml) throws XBRLException {

        logger.debug("Discovering a resource supplied as a string and with URI: " + uri);

        if (!uri.isAbsolute()) throw new XBRLException("The URI " + uri + " must be absolute.");

        if (uri.isOpaque()) throw new XBRLException("The URI " + uri + " must NOT be opaque.");

        // Copy the XML to the local cache even if it is there already (possibly over-writing existing documents)
        this.getCache().copyToCache(uri, xml);

        try {
            this.stashURI(new URI("http://www.xbrlapi.org/xbrl/xbrl-2.1-roles.xsd"));
        } catch (URISyntaxException e) {
            throw new XBRLException("The standard roles URI could not be formed for discovery.",e);
        }
        
        discover(uri);
    }


    /**
     * Retrieve URI of the next document to parse from the list of starting
     * point URIs provided or URIs found during the discovery process.
     * @return the URI of the next document to explore or null if there are none.
     * @throws XBRLException
     */
    private URI getNextDocumentToExplore() throws XBRLException {
        if (documentQueue.isEmpty()) return null;
        return documentQueue.first();
    }
    
    protected void markDocumentAsExplored(URI uri) {
        documentQueue.remove(uri);
        successes.add(uri);
    }
    

    

    


    /**
     * Parse an XML Document supplied as a URI the next part of the DTS.
     * @param uri The URI of the document to parse.
     * @throws XBRLException IOException ParserConfigurationException SAXException
     */
    protected void parse(URI uri) throws XBRLException, ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = null;
        inputSource = this.getEntityResolver().resolveEntity("", uri.toString());
        ContentHandler contentHandler = new ContentHandlerImpl(this, uri);
        parse(uri, inputSource, contentHandler);
    }

    /**
     * Parse an XML Document supplied as a string the next part of the DTS.
     * @param uri The URI to associate with the supplied XML.
     * @param xml The XML document as a string.
     * @throws XBRLException IOException SAXException ParserConfigurationException
     */
    protected void parse(URI uri, String xml) throws XBRLException, ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = new InputSource(new StringReader(xml));
        ContentHandler contentHandler = new ContentHandlerImpl(this, uri, xml);
        parse(uri, inputSource, contentHandler);
    }

    /**
     * Parse the supplied input source.
     * @param uri The URI to be associated with the supplied input source.
     * @param inputSource The input source to parse.
     * @param contentHandler The content handler to use for SAX parsing.
     * @throws XBRLException ParserConfigurationException SAXException IOException
     */
    protected void parse(URI uri, InputSource inputSource, ContentHandler contentHandler) throws XBRLException, ParserConfigurationException, SAXException, IOException {

        logger.debug("about to parse " + uri);        
        // Create and configure the SAX parser factory
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false); // Turn off standard XML validation
        factory.setNamespaceAware(true);

        /*
         * // Turn on XML Schema validation
         * factory.setFeature("http://xml.org/sax/features/validation",true);
         * factory.setFeature("http://apache.org/xml/features/validation/schema",true);
         * factory.setFeature("http://apache.org/xml/features/validation/schema-full-checking",false);
         */

        // Create the SAX parser to use
        SAXParser parser = null;
        parser = factory.newSAXParser();

        XMLReader reader = null;
        reader = parser.getXMLReader();
        reader.setEntityResolver(this.entityResolver);
        reader.setContentHandler(contentHandler);
        reader.setErrorHandler((ErrorHandler) contentHandler);
        reader.setFeature("http://xml.org/sax/features/namespace-prefixes",true);

        reader.setProperty(Constants.JAXP_SCHEMA_LANGUAGE,Constants.W3C_XML_SCHEMA);

        logger.debug("Parsing " + uri);
        reader.parse(inputSource);
        
    }

    /**
     * Set the starting points for DTSImpl discovery using a linked list
     * @param uris A list of starting point document URIs for DTSImpl discovery
     * @throws XBRLException
     */
    protected void setStartingURIs(List<URI> uris) throws XBRLException {
        if (uris == null)
            throw new XBRLException("Null list of URIs is not permitted.");

        for (int i = 0; i < uris.size(); i++) {
            stashURI(uris.get(i));
        }
    }

    /**
     * @see org.xbrlapi.loader.Loader#stashURI(URI)
     */
    public synchronized void stashURI(URI uri) throws XBRLException {

        // Validate the URI
        if (!uri.isAbsolute()) {
            throw new XBRLException("The URI: " + uri + " must be absolute.");                
        }
        if (uri.isOpaque()) {
            throw new XBRLException("The URI: " + uri + " must not be opaque.");                
        }

        URI dereferencedURI = null;
        try {
            dereferencedURI = new URI(uri.getScheme(),null,uri.getHost(), uri.getPort(), uri.getPath(),null,null);
        } catch (URISyntaxException e) {
            throw new XBRLException("Malformed URI found in DTS discovery process: " + uri, e);
        }

        // Stash the URI if it has not already been stashed
        if (!successes.contains(dereferencedURI)) {

            // Queue up the original URI - ignoring issues of whether it matches another document.
            documentQueue.add(dereferencedURI);
            
/*            // Only stash if the document does not already have a match.
            URI matchURI = getStore().getMatcher().getMatch(dereferencedURI);
            if (matchURI.equals(dereferencedURI)) {
                documentQueue.add(dereferencedURI);
            } else {
                logger.debug("No need to stash " + dereferencedURI + " because it has match " + matchURI);
            }
*/
        }

    }

    /**
     * @see Loader#stashURIs(List)
     */
    public void stashURIs(List<URI> uris) throws XBRLException {
        for (URI uri: uris) {
            this.stashURI(uri);
        }
    }

    /**
     * Set the resolver for the resolution of entities found during the loading
     * and XLink processing
     * 
     * @param resolver
     *            An entity resolver implementation
     */
    public void setEntityResolver(EntityResolver resolver) {
        this.entityResolver = resolver;
    }

    public String getNextFragmentId() throws XBRLException {
        String id = getCurrentFragmentId();
        incrementFragmentId();
        return id;
    }

    public String getCurrentFragmentId() {
        return getDocumentId() + "_" + (new Integer(fragmentId)).toString();
    }

    public void incrementFragmentId() {
        fragmentId++;
    }

    /**
     * Used to set the next fragment id using the information in the data store.
     * This is useful when coming back to an existing data store to add
     * additional documents.
     * 
     * @param id
     * @throws XBRLException
     */
    private void setNextFragmentId(String id) {
        fragmentId = (new Integer(id)).intValue();
    }

    /**
     * Return the entity resolver being used by the loader.
     * 
     * @return the entity resolver being used by the loader.
     */
    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    private boolean useSchemaLocationAttributes = false;

    /**
     * @see org.xbrlapi.loader.Loader#useSchemaLocationAttributes()
     */
    public boolean useSchemaLocationAttributes() {
        return this.useSchemaLocationAttributes;
    }

    /**
     * @see org.xbrlapi.loader.Loader#setSchemaLocationAttributeUsage(boolean)
     */
    public void setSchemaLocationAttributeUsage(boolean useThem) {
        this.useSchemaLocationAttributes = useThem;
    }

    /**
     * @see org.xbrlapi.loader.Loader#storeDocumentsToAnalyse()
     */
    public void storeDocumentsToAnalyse() throws XBRLException {
        Map<URI,String> map = new HashMap<URI,String>();
        for (URI document : documentQueue) {
            if (document.equals(getStore().getMatcher().getMatch(document))) {
                map.put(document,"Document has not yet been analysed");
            }
        }
        for (URI document : failures.keySet()) {
            map.put(document,failures.get(document));
        }
        logger.info("Storing " + map.size() + " documents that are still to be analysed.");
        getStore().persistLoaderState(map);
    }
    
    private void cleanupFailedLoad(URI uri, String reason, Exception e) {
        logger.error(getDocumentURI() + "encountered loading problem: " + e.getMessage());
        failures.put(uri,reason);
        documentQueue.remove(uri);
        try {
            getStore().deleteDocument(uri);
            // getCache().purge(uri);
            logger.info("Purged " + uri + " from the data store and cache.");
        } catch (Exception exception) {
            logger.error("Failed to clean up the document from the data store or cache. " + exception.getMessage());
        }
        fragments = new Stack<Fragment>();
        states = new Stack<ElementState>();        
    }




}
