package org.xbrlapi.data.xindice;

import org.apache.xindice.client.xmldb.services.CollectionManager;
import org.apache.xindice.xml.dom.DOMParser;
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
	private final String SCHEME = "xmldb:xindice";   

	// The database instance
	private Database database = null;

	// The URI for the database connection
	private String databaseURI = null;
	
	private Collection rootCollection = null;
	
	// The URI for the collection
	private String collectionURI = null;
	
	/**
	 * Constructor registers the Xindice database with the XML:DB
	 * database manager.
	 * @param host Host DB server IP address or domain name 
	 * @param port Host DB server port
	 * @param database Database name
	 * @throws XBRLException when a connection cannot be established
	 */
	public DBConnectionImpl(String host, String port, String database) throws XBRLException {
		try {
			this.database = new org.apache.xindice.client.xmldb.DatabaseImpl();
			DatabaseManager.registerDatabase(this.database);
		} catch (XMLDBException e) {
			throw new XBRLException("No connection could be established to the XINDICE database.", e);
		}
		databaseURI = SCHEME + "://" + host + ":" + port + "/" + database;
		rootCollection = this.getCollection("/");
		if (rootCollection == null) {
			throw new XBRLException("The root collection of the XINDICE database connection could be obtained.");
		}
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

    		CollectionManager service = (CollectionManager) containerCollection.getService("CollectionManager", "1.0");
            String collectionConfig =
                "<collection compressed=\"true\" name=\"" + collectionName + "\">"
              + "   <filer class=\"org.apache.xindice.core.filer.BTreeFiler\"/>"
              + "</collection>";
            service.createCollection(collectionName, DOMParser.toDocument(collectionConfig));
            return getCollection(collectionName,containerCollection);
            
    	} catch (Exception e) {
    		try {
    			throw new XBRLException("The collection " + collectionName + " could not be created in the parent collection called " + containerCollection.getName(),e);
    		} catch (XMLDBException dbe) {
    			throw new XBRLException("The collection " + collectionName + " could not be created in the parent collection.",e);    			
    		}
    	}    	
    }
    
	/**
	 * Convenience method for creating a root collection.
	 * @param collectionName The name of the collection.
	 * @throws XBRLException
	 */
    public Collection createRootCollection(String collectionName) throws XBRLException {
		return createCollection(collectionName,rootCollection);    	
    }
    
    /**
	 * Delete a collection.
	 * @param collectionPath The identifier of the collection.
	 * @throws XBRLException
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
		try {
			collectionURI = databaseURI + collectionPath;
			return DatabaseManager.getCollection(collectionURI);
		} catch ( Exception e) {
			throw new XBRLException("Could not get collection " + collectionPath + " from the Xindice database.", e);
		}
	}

	/**
	 * Get a collection given the name of the collection (not its full path) and its container collection.
	 * @param collectionName The name of the collection (eg: for collection /db/shakespeare/plays this would be plays).
	 * @param container The container collection (eg: for collection /db/shakespeare/plays this would be the /db/shakespeare collection).
	 * @return The required collection.
	 * @throws XBRLException if the required collection cannot be retrieved.
	 */
	public Collection getCollection(String collectionName, Collection container) throws XBRLException {
		try {
			return container.getChildCollection(collectionName);
		} catch (XMLDBException e) {
			try {
				throw new XBRLException("Could not get collection " + collectionName + " from the container collection, " + container.getName(), e);
			} catch (XMLDBException xmldbe) {
				throw new XBRLException("Could not get collection " + collectionName + " from its container collection.", xmldbe);
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
			Collection c = this.getCollection(collectionName, containerCollection);
			if (c == null) {
				return false;
			}
			return true;
	}

	/**
	 * Returns the URI to access the Xindice database for this connection.
	 * @return The URI for accessing the Xindice database for this connection.
	 */
	public String getDatabaseURI() {
		return databaseURI;
	}

	/** 
	 * @see org.xbrlapi.data.xindice.DBConnection#close()
	 */
	public void close() throws XBRLException {
	}
	
}