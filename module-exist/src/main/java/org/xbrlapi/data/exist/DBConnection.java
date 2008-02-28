package org.xbrlapi.data.exist;

import org.xbrlapi.utilities.XBRLException;
import org.xmldb.api.base.Collection;

/**
 * Data store connection interface.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface DBConnection {
	    
	/**
	 * Create a collection.
	 * @param collectionName The name of the collection
	 * @param containerCollection The container collection itself
	 * @throws XBRLException
	 */
    public Collection createCollection(String collectionName, Collection containerCollection) throws XBRLException;
    
	/**
	 * Convenience method for creating a root collection.
	 * @param collectionName The name of the collection.
	 * @throws XBRLException
	 */
    public Collection createRootCollection(String collectionName) throws XBRLException;
    
    /**
	 * Delete a collection.
	 * @param collectionPath The identifier of the collection.
	 * @throws XBRLException
	 */
    public void deleteCollection(String collectionPath) throws XBRLException;
    
    /**
	 * Delete a collection.
	 * @param collectionName The name of the collection.
	 * @param containerCollection The collection containing the collection to be deleted.
	 * @throws XBRLException
	 */
    public void deleteCollection(String collectionName, Collection containerCollection) throws XBRLException;
    
	/**
	 * Get a specific collection from the database connection
	 * @param collectionPath The collection identifier
	 * @return the specified collection or null if it does not exist
	 * @throws XBRLException
	 */
	public Collection getCollection(String collectionPath) throws XBRLException;	
	
	/**
	 * Get a collection given the name of the collection (not its full path) and its container collection.
	 * @param collectionName The name of the collection (eg: for collection /db/shakespeare/plays this would be plays).
	 * @param container The container collection (eg: for collection /db/shakespeare/plays this would be the /db/shakespeare collection).
	 * @return The required collection.
	 * @throws XBRLException if the required collection cannot be retrieved.
	 */
	public Collection getCollection(String collectionName, Collection container) throws XBRLException;
	
	/**
	 * Test if a specific collection exists.
	 * @param collectionPath The name of the collection including its full path.
	 * @return true if the collection exists and false otherwise.
	 * @throws XBRLException if the existence of the collection cannot be ascertained.
	 */
	public boolean hasCollection(String collectionPath) throws XBRLException;

	/**
	 * Test if a specific collection exists.
	 * @param collectionName The name of the collection
	 * @param containerCollection The container collection for the collection being tested for.
	 * @return true if the collection exists and false otherwise.
	 * @throws XBRLException if the existence of the collection cannot be ascertained.
	 */
	public boolean hasCollection(String collectionName, Collection containerCollection) throws XBRLException;
	
	/**
	 * Returns the URI to access the Xindice database for this connection.
	 * @return The URI for accessing the Xindice database for this connection.
	 */
	public String getDatabaseURI();
	
	/** 
	 * Close down the connection to the database.
	 * @throws XBRLException if the connection does not close cleanly.
	 */
	public void close() throws XBRLException;

}