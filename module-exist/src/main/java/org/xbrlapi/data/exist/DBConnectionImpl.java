package org.xbrlapi.data.exist;

import org.xbrlapi.utilities.XBRLException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class DBConnectionImpl implements DBConnection {
	    
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
	public DBConnectionImpl(String host, String port, String database, String username, String password) throws XBRLException {

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

		// Force a failure if the root collection is a dud.
		Collection rootCollection = this.getRootCollection();
		try {
			int children = rootCollection.getChildCollectionCount();
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