package org.xbrlapi.data.exist;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.exist.xmldb.XQueryService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.XML;
import org.xbrlapi.data.BaseStoreImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

/**
 * Implementation of the XBRL data store using eXist
 * as the underlying database.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class StoreImpl extends BaseStoreImpl implements Store {

    private final static Logger logger = Logger.getLogger(StoreImpl.class);
    protected final static String configurationRoot = "/system/config";        
    
    private String computerId, host, port, database, username, password, storeParentPath, dataCollectionName;
    
	/**
	 * The database connection used by the data store.
	 */
	transient private DBConnection connection;
	
	/**
	 * The collection that holds the data in the data store.
	 */
	transient private Collection collection;
	
	/**
	 * The collection manager that operates on the data store collection (for indexing etc.).
	 */
	transient private CollectionManagementService manager;

	/**
	 * The XQuery service for the data collection.
	 */
    transient private XQueryService xqueryService;
    	
    /**
     * @return the database connection or null if there is none.
     */
    public DBConnection getConnection() {
        return connection;
    }
    
    private class Connection implements org.xbrlapi.data.exist.DBConnection {
            
        // Collection manager path to instantiate
        private final String SCHEME = "xmldb:exist";
        
        // The database instance
        private Database database = null;

        // The URI for the database connection
        private String databaseURI = null;
        
        // The user name and password for accessing the data store.
        private String username = null;
        private String password = null;
        
        /**
         * Constructor registers the database with the XML:DB
         * database manager.
         * @param host Host DB server IP address or domain name 
         * @param port Host DB server port
         * @param database Database name
         * @param username Username of the database user
         * @param password Password of the database user
         * @throws XBRLException when a connection cannot be established
         */
        public Connection(String host, String port, String database, String username, String password) throws XBRLException {

            // Store the username and password
            this.username = username;
            this.password = password;
            
            try {
                this.database = new org.exist.xmldb.DatabaseImpl();         
                DatabaseManager.registerDatabase(this.database);
            } catch (XMLDBException e) {
                throw new XBRLException("No connection could be established to the EXIST database.", e);
            }

            if (! port.equals("")) port = ":" + port;
            databaseURI = SCHEME + "://" + host + port + "/" + database;

            //logger.info("The database URI is " + databaseURI);
            
            // Force a failure if the root collection is a dud.
            Collection rootCollection = this.getRootCollection();
            try {
                rootCollection.getChildCollectionCount();
            } catch (XMLDBException e) {
                throw new XBRLException("Check the EXIST database URI.  The connection failed.", e);
            }
            
            if (rootCollection == null) {
                throw new XBRLException("The root collection of the EXIST database connection could be obtained.");
            }
            
        }

        /**
         * Get the root collection for the database.
         * @return the root collection for the database.
         * @throws XBRLException if the root collection cannot be retrieved.
         */
        private Collection getRootCollection() throws XBRLException {
            return this.getCollection("");
        }
        
        /**
         * Create a collection.
         * @param collectionName The name of the collection
         * @param containerCollection The container collection itself
         * @throws XBRLException
         */
        public Collection createCollection(String collectionName, Collection containerCollection) throws XBRLException {
            try {
                if (hasCollection(collectionName,containerCollection)) {
                    throw new XBRLException("The collection " +  collectionName + " already exists in " + containerCollection.getName());
                }
                
                CollectionManagementService service = (CollectionManagementService) containerCollection.getService("CollectionManagementService", "1.0");
                Collection collection = service.createCollection(collectionName);
                
                // Make sure that collection names do not involve characters that require escaping
                String name = collection.getName();
                name = name.substring(name.length()-collectionName.length(),name.length());
                if (! name.equals(collectionName)) {
                    service.removeCollection(collection.getName());
                    throw new XBRLException("The collection name " + collectionName + " contains illegal characters.");
                }
                
                return collection;
                
            } catch (Exception e) {
                throw new XBRLException("The collection " + collectionName + " could not be created in " + containerCollection,e);
            }
       }
        
        /**
         * Convenience method for creating a root collection.
         * @param collectionName The name of the collection.
         * @throws XBRLException
         */
        public Collection createRootCollection(String collectionName) throws XBRLException {
            return createCollection(collectionName, getRootCollection());       
        }

        /**
         * Delete a collection.
         * @param collectionPath The identifier of the collection.
         * @throws XBRLException if the collection does not exist, has no parent collection or 
         * if a technical issue arose during the deletion process.
         */
        public void deleteCollection(String collectionPath) throws XBRLException {

            try {
                Collection target = getCollection(collectionPath);
                if (target == null) {
                    throw new XBRLException("The collection to be deleted, " + collectionPath + " does not exist.");
                }
                String targetName = target.getName();

                Collection parent = target.getParentCollection();
                if (parent == null) {
                    throw new XBRLException("The collection to be deleted, " + collectionPath + " has no parent collection.");
                }
                CollectionManagementService service = (CollectionManagementService) parent.getService("CollectionManager", "1.0");
                if (service == null) {
                    throw new XBRLException("The collection management service of the parent collection could not be instantiated.");
                }
                service.removeCollection(targetName);
            } catch (XMLDBException e) {
                throw new XBRLException("Collection " + collectionPath + " could not be deleted. ",e);
            }
        }    
        
        /**
         * Delete a collection.
         * @param collectionName The name of the collection.
         * @param containerCollection The collection containing the collection to be deleted.
         * @throws XBRLException
         */
        public void deleteCollection(String collectionName, Collection containerCollection) throws XBRLException {
            try {
                CollectionManagementService service = (CollectionManagementService) containerCollection.getService("CollectionManagementService", "1.0");
                service.removeCollection(collectionName);
            } catch (Exception e) {
                throw new XBRLException("Collection " + collectionName + " could not be deleted.",e);
            }
        }    
        
        /**
         * Get a specific collection from the database connection.
         * @param collectionPath The collection identifier.
         * @return the specified collection or null if it does not exist.
         * @throws XBRLException if the XMLDB operations fail.
         */
        public Collection getCollection(String collectionPath) throws XBRLException {
            
            collectionPath = (collectionPath.equals("")) ? "/" : collectionPath;
            collectionPath = (collectionPath.charAt(0) != '/') ? "/" + collectionPath : collectionPath;
            
            String collectionURI = databaseURI + collectionPath;
            logger.debug("Getting collection " + collectionURI);
            try {
                return DatabaseManager.getCollection(collectionURI, username, password);
            } catch ( Exception e) {
                throw new XBRLException("Could not get collection " + collectionPath + " from the EXIST database.  Perhaps a username and password would help.", e);
            }
        }

        /**
         * Get a collection given the name of the collection (not its full path) and its container collection.
         * @param collectionName The name of the collection (eg: for collection /db/shakespeare/plays this would be plays).
         * @param container The container collection (eg: for collection /db/shakespeare/plays this would be the /db/shakespeare collection).
         * @return The required collection or null if it is not available.
         * @throws XBRLException if the required collection cannot be retrieved.
         */
        public Collection getCollection(String collectionName, Collection container) throws XBRLException {
            try {
                if (container == null) return null;
                return container.getChildCollection(collectionName);
            } catch (XMLDBException e) {
                try {
                    throw new XBRLException("Could not get collection " + collectionName + " from the container collection, " + container.getName(), e);
                } catch (XMLDBException xmldbe) {
                    throw new XBRLException("Failed to get collection, " + collectionName + ", from its container collection.", xmldbe);
                }
            }
        }

        /**
         * Test if a specific collection exists.
         * @param collectionPath The name of the collection including its full path.
         * @return true if the collection exists and false otherwise.
         * @throws XBRLException if the existence of the collection cannot be ascertained.
         */
        public boolean hasCollection(String collectionPath) throws XBRLException {
            Collection c = this.getCollection(collectionPath);
            if (c == null) {
                return false;
            }
            return true;
        }   
        
        /**
         * Test if a specific collection exists.
         * @param collectionName The name of the collection
         * @param containerCollection The container collection for the collection being tested for.
         * @return true if the collection exists and false otherwise.
         * @throws XBRLException if the existence of the collection cannot be ascertained.
         */
        public boolean hasCollection(String collectionName, Collection containerCollection) throws XBRLException {
            Collection c = null;
            try {
                c = this.getCollection(collectionName, containerCollection);
            } catch (XBRLException e) {
                return false;
            }
            if (c == null) {
                return false;
            }
            return true;
        }
        
        /**
         * Returns the URI to access the database for this connection.
         * @return The URI for accessing the database for this connection.
         */
        public String getDatabaseURI() {
            return databaseURI;
        }

        /** 
         * @see org.xbrlapi.data.exist.DBConnection#close()
         */
        public void close() throws XBRLException {
        }   
        
    }    
    
    
	/**
	 * Initialise the database connection.
     * @param host Host DB server IP address or domain name 
     * @param port Host DB server port
     * @param database Database name
     * @param username Username of the database user
     * @param password Password of the database user
	 * @param storeParentPath The full path to the container collection, which is the
	 * collection that will hold the data collection when it are created.
	 * @param dataCollectionName The name of the data collection.
	 * @throws XBRLException
	 */
	public StoreImpl(
	        String host, 
	        String port, 
	        String database, 
	        String username, 
	        String password,     
			String storeParentPath,
			String dataCollectionName
			) throws XBRLException {
		
	    super();
	    initialize(host, port, database, username, password, storeParentPath, dataCollectionName);
	}
	
	private void initialize(
            String host, 
            String port, 
            String database, 
            String username, 
            String password,     
            String storeParentPath,
            String dataCollectionName
	        ) throws XBRLException {
	    if (host == null) throw new XBRLException("Host must not be null.");
        if (port == null) throw new XBRLException("Port must not be null.");
        if (database == null) throw new XBRLException("Database must not be null.");
        if (password == null) throw new XBRLException("Password must not be null.");
        if (storeParentPath == null) throw new XBRLException("Parent path must not be null.");
        if (dataCollectionName == null) throw new XBRLException("Collection name must not be null.");
	    this.host = host;
	    this.port = port;
	    this.database = database;
	    this.password = password;
        this.storeParentPath = (storeParentPath.charAt(storeParentPath.length()-1) != '/') ? storeParentPath + "/" : storeParentPath;
        this.dataCollectionName = dataCollectionName;
        this.connection = new Connection(host,port,database, username, password);
        

        Collection parentCollection = connection.getCollection(this.storeParentPath);
        if (parentCollection == null) throw new XBRLException("The collection to contain the data store does not exist.");
                
        boolean storeAlreadyExisted = false;
        if (connection.hasCollection(this.dataCollectionName,parentCollection)) {
            storeAlreadyExisted = true;
            collection = connection.getCollection(dataCollectionName, parentCollection);
        } else {
            collection = connection.createCollection(dataCollectionName,parentCollection);
        }
        
        if (collection == null) throw new XBRLException("Collection " + dataCollectionName + " could not be created in collection " + storeParentPath + ".");

        try {
            manager = (CollectionManagementService) collection.getService("CollectionManagementService", "1.0");
        } catch (XMLDBException e) {
            throw new XBRLException("The collection manager could not be instantiated.",e);
        }
                
        if (! storeAlreadyExisted) {
            
            // Add the collection configuration (indexing information) if the store uses a new collection.
            Collection configParentCollection = connection.getCollection("system/config");
            Collection dataConfigCollection = null;
            try {
                dataConfigCollection = configParentCollection.getChildCollection(dataCollectionName); 
            if (dataConfigCollection == null)
                dataConfigCollection = connection.createCollection(dataCollectionName, configParentCollection);
            } catch (XMLDBException e) {
                throw new XBRLException("The data configuration collection could not be instantiated.",e);
            }
            
            String dataXconf = 
                "<collection xmlns=\"http://exist-db.org/collection-config/1.0\">\n"
                + "<index xmlns:xlink=\"" + Constants.XLinkNamespace + "\" >\n"
                + "    <fulltext default=\"none\" attributes=\"false\" alphanum=\"false\" />\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/"+ Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:label\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/"+ Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type\" type=\"xs:string\"/>\n"                
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@type\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@id\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@targetDocumentURI\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@targetPointerValue\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@parentIndex\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@uri\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@absoluteHref\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@resourceURI\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@value\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/"+ Constants.XBRLAPIPrefix + ":" + "xptr/@value\" type=\"xs:string\"/>\n"
                + "</index>\n"
                + "</collection>";
            
            try {
                
                XMLResource dataXconfResource = (XMLResource) dataConfigCollection.createResource("indexes.xconf", XMLResource.RESOURCE_TYPE);
                dataXconfResource.setContent(dataXconf);
                dataConfigCollection.storeResource(dataXconfResource);
                
            } catch (XMLDBException e) {
                throw new XBRLException("The system index configuration resource could not be added.",e);
            }

        }

        try {
            xqueryService = (XQueryService) collection.getService("XQueryService","1.0");
            xqueryService.setNamespace(Constants.XLinkPrefix, Constants.XLinkNamespace.toString());
            xqueryService.setNamespace(Constants.XMLSchemaPrefix, Constants.XMLSchemaNamespace.toString());
            xqueryService.setNamespace(Constants.XBRL21Prefix, Constants.XBRL21Namespace.toString());
            xqueryService.setNamespace(Constants.XBRL21LinkPrefix, Constants.XBRL21LinkNamespace.toString());
            xqueryService.setNamespace(Constants.XBRLAPIPrefix, Constants.XBRLAPINamespace.toString());
            xqueryService.setNamespace(Constants.XBRLAPILanguagesPrefix, Constants.XBRLAPILanguagesNamespace.toString());
            
        } catch (XMLDBException e) {
            throw new XBRLException("The XPath services could not be initialised.",e);
        }        
	}
    
	/**
	 * Close the data store.
	 * Throws XBRLException if the data store cannot be closed. 
	 */
	public synchronized void close() throws XBRLException {
		try {
			collection.close();
			connection.close();
			connection = null;
		} catch (XMLDBException e) {
			throw new XBRLException("The Xindice DTS collection could not be closed.",e);
		}
	}
	
	/**
	 * @see org.xbrlapi.data.Store#delete()
	 */
	public synchronized void delete() throws XBRLException {

	    try {
			if (connection.hasCollection(dataCollectionName)) {
	            connection.deleteCollection(dataCollectionName,collection.getParentCollection());
			} else {
                logger.error("No " + dataCollectionName + " collection to delete.");
			}
			
			// Delete the index specification
	        if (connection.hasCollection(configurationRoot + storeParentPath + dataCollectionName)){
	            connection.deleteCollection(configurationRoot + storeParentPath + dataCollectionName);
	        }

	        connection.close();
	        connection = null;

	    } catch (XMLDBException e) {
	        logger.error("Darn!");
			throw new XBRLException("The data collection could not be deleted.",e);
		}
	}	
	
    /**
     * @see org.xbrlapi.data.Store#persist(XML)
     */
    public synchronized int getSize() throws XBRLException {
        try {
            return this.collection.getResourceCount();
        } catch (XMLDBException e) {
            throw new XBRLException("Failed to get the number of fragments in the data store.",e);
        }
    }	

    /**
	 * Add a fragment to the DTS.  Sets the fragment builder to null because it
	 * is no longer required, stores the fragment data and metadata in the 
	 * data store, sets the store property in the fragment.
	 * @param xml The fragment to be added to the DTS store.
	 * @throws XBRLException if the fragment cannot be added to the store 
	 * (eg: because one with the same index is already in the store).
	 */
    public synchronized void persist(XML xml) throws XBRLException {

		if (xml == null) throw new XBRLException("The fragment is null so it cannot be added.");
		String index = xml.getIndex();
		
		if (hasXMLResource(index)) {
            this.remove(index);
        }

        if (xml.getStore() != null) {
	    	try {
	    		XMLResource resource = (XMLResource) collection.createResource(index, XMLResource.RESOURCE_TYPE);
		        resource.setContent(DOM2String(xml.getMetadataRootElement()));
		        collection.storeResource(resource);
	        } catch (XMLDBException e) {
	        	throw new XBRLException("The fragment data could not be added to the eXist data store.", e);
	        }			
			return;
		}
		
		try {
			XMLResource resource = (XMLResource) collection.createResource(index, XMLResource.RESOURCE_TYPE);
            resource.setContent(DOM2String(xml.getBuilder().getMetadata()));
	        collection.storeResource(resource);
        } catch (XMLDBException e) {
        	throw new XBRLException("The fragment data could not be added to the eXist data store.", e);
        }

        // Finalise the fragment, ready for use
        if (xml.getStore() == null) {
            if (xml.getBuilder() != null) xml.setResource(xml.getBuilder().getMetadata());
            xml.setStore(this);
        }

	}
	
    /**
     * Test if a store contains a specific fragment, as identified by
     * its index.
     * @param index The index of the fragment to test for.
     * @return true iff the store contains a fragment with the specified 
     * fragment index.
     * @throws XBRLException If the test cannot be conducted.
     */
	public synchronized boolean hasXMLResource(String index) throws XBRLException {
    	if (getXMLResource(index) == null) {
    		return false;
    	}
    	return true;
    }
	
    /**
     * Retrieves a fragment from an XBRL API data store.
     * The fragment will be created as a fragment of the original 
     * fragment type but will be returned as a straight fragment.
     * @param index The index of the fragment.
     * @return The fragment corresponding to the specified index or null if 
     * the fragment is not in the store.
     * @throws XBRLException if the fragment cannot be retrieved.
     */
    public synchronized <F extends XML> F getXMLResource(String index) throws XBRLException {
    	try {
    		XMLResource resource = (XMLResource) collection.getResource(index);
    		if (resource == null) return null;
    		Element root = getResourceRootElement(resource);
    		return FragmentFactory.<F>newFragment(this, root);
    	} catch (XMLDBException e) {
    		throw new XBRLException("The fragment with index " + index + " could not be retrieved.",e);
    	}
    }
    
    /**
     * @param resource The resource to get the root element from.
     * @return the root element of the resource.
     * @throws XMLDBException
     */
    private Element getResourceRootElement(XMLResource resource) throws XMLDBException {
		Node node = resource.getContentAsDOM();
    	if (node.getNodeType() == Element.DOCUMENT_NODE) {
    		return ((Document) node).getDocumentElement();
    	}
		return (Element) node;
    }
    


	/**
	 * Remove a fragment from the DTS.  
	 * If a fragment with the same ID does not already exist in the 
	 * DTs then no action is required.
	 * @param index The index of the fragment to be removed from the DTS store.
	 * @throws XBRLException if the fragment cannot be removed from the store.
	 */
    public synchronized void remove(String index) throws XBRLException {
        try {
            if (!hasXMLResource(index)) {
            	return;
            }

            Resource document = collection.getResource(index);
            collection.removeResource(document);
        
        }
        catch (XMLDBException e) {
        	throw new XBRLException("The fragment removal failed.", e);
        }
	}
	



	

	
	/**
	 * @see org.xbrlapi.data.Store#queryForXMLResources(String)
	 */
    @SuppressWarnings(value = "unchecked")
	public synchronized <F extends XML> List<F> queryForXMLResources(String query) throws XBRLException {
		
        String roots = "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
		ResourceSet resources = null;
		try {
	        for (URI namespace: this.namespaceBindings.keySet()) 
	            xqueryService.setNamespace(this.namespaceBindings.get(namespace), namespace.toString());
			resources = xqueryService.query(query);
		} catch (XMLDBException e) {
			throw new XBRLException("The query service failed.", e);
		}

		List<F> fragments = new Vector<F>();
		try {
			ResourceIterator iterator = resources.getIterator();
			while (iterator.hasMoreResources()) {
				Element root = getResourceRootElement((XMLResource) iterator.nextResource());
				fragments.add((F) FragmentFactory.newFragment(this, root));
			}
		} catch (XMLDBException e) {
			throw new XBRLException("The query failed.", e);
		}
		return fragments;
	}
    
    /**
     * @see org.xbrlapi.data.Store#queryCount(String)
     */
    public synchronized long queryCount(String query) throws XBRLException {
        
        String roots = "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
        ResourceSet resources = null;
        try {
            for (URI namespace: this.namespaceBindings.keySet()) 
                xqueryService.setNamespace(this.namespaceBindings.get(namespace), namespace.toString());
            resources = xqueryService.query(query);
            return resources.getSize();
        } catch (XMLDBException e) {
            throw new XBRLException("The query service failed.", e);
        }

    }    
    
    /**
     * @see org.xbrlapi.data.Store#queryForIndices(String)
     */
    public synchronized Set<String> queryForIndices(String query) throws XBRLException {

        String roots = "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
        ResourceSet resources = null;
        try {
            for (URI namespace: this.namespaceBindings.keySet()) 
                xqueryService.setNamespace(this.namespaceBindings.get(namespace), namespace.toString());
            resources = xqueryService.query(query);
        } catch (XMLDBException e) {
            throw new XBRLException("The XPath query service failed.", e);
        }

        Set<String> indices = new TreeSet<String>();
        try {
            ResourceIterator iterator = resources.getIterator();
            String regex = "<xbrlapi:fragment.*? index=\"(\\w+)\".*?>";
            Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
            while (iterator.hasMoreResources()) {
                XMLResource resource = (XMLResource) iterator.nextResource();
                String string = ((String) resource.getContent()).replaceAll("\n"," ");
                Matcher matcher = pattern.matcher(string);
                matcher.matches();
                String index = matcher.group(1);
                indices.add(index);
            }
        } catch (XMLDBException e) {
            throw new XBRLException("The query failed.", e);
        }
        return indices;
    }
    
    /**
     * @see org.xbrlapi.data.Store#queryForStrings(String)
     */
    public synchronized Set<String> queryForStrings(String query) throws XBRLException {

        String roots = "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
        ResourceSet resources = null;
        try {
            for (URI namespace: this.namespaceBindings.keySet()) 
                xqueryService.setNamespace(this.namespaceBindings.get(namespace), namespace.toString());
            resources = xqueryService.query(query);
        } catch (XMLDBException e) {
            throw new XBRLException("The XQuery service failed.", e);
        }

        Set<String> strings = new TreeSet<String>();
        try {
            ResourceIterator iterator = resources.getIterator();
            while (iterator.hasMoreResources()) {
                XMLResource resource = (XMLResource) iterator.nextResource();
                strings.add((String) resource.getContent());
            }
        } catch (XMLDBException e) {
            throw new XBRLException("The query failed.", e);
        }
        return strings;

    }    
	
	/**
	 * Run a query against the collection of all fragments in the store.
	 * @param query The XPath query to run against the set of fragments.
	 * @param fragments The list of fragments to be populated with the query results.
	 * @return a list of matching fragments or the empty list if no matching fragments
	 * exist.
	 * @throws XBRLException if the query cannot be executed.
	 */
/*	public <F extends Fragment> void query(String query) throws XBRLException {
		ResourceSet resources = null;
		try {
			resources = xpathService.query(query);
		} catch (XMLDBException e) {
			throw new XBRLException("The XPath query service failed.", e);
		}
		try {
			ResourceIterator iterator = resources.getIterator();
			while (iterator.hasMoreResources()) {
				XMLResource resource = (XMLResource) iterator.nextResource();
				fragments.add((F) FragmentFactory.newFragment(this, resource));
			}
		} catch (XMLDBException e) {
			throw new XBRLException("The XPath query of the DTS failed.", e);
		}
		return fragments;		
	}*/
	
    
	/**
	 * @param resource The XMLResource to be used to get the DOM document node.
	 * @return the org.w3.Document node from an XMLResource returned by the
	 * data store or null if no document node is available.
	 * @throws XBRLException if the resource content is not available as a DOM object.
	 */
	private Document getDocumentNode(XMLResource resource) throws XBRLException {
		Node node = null;
		try {
			node = resource.getContentAsDOM();
		} catch (XMLDBException e) {
			throw new XBRLException("The resource content was not available as a DOM object.", e);
		}
		
    	if (node.getNodeType() == Element.DOCUMENT_NODE) {
    		return (Document) node;
    	}
		return ((Element) node).getOwnerDocument();  // May return a null value!
	}    
 
    public String toString() {
        try {
            if (connection != null) return "eXist XML database at " + this.connection.getDatabaseURI() + this.collection.getName();
            return "eXist database " + super.toString();
        } catch (XMLDBException e) {
            return "eXist database " + super.toString();
        }
    }
    
}