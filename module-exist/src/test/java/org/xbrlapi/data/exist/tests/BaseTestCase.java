package org.xbrlapi.data.exist.tests;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.xbrlapi.SAXHandlers.EntityResolverImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.exist.DBConnection;
import org.xbrlapi.data.exist.DBConnectionImpl;
import org.xbrlapi.data.exist.StoreImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;
import org.xml.sax.EntityResolver;

/**
 * Provides a base test case for tests involving the eXist database.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

abstract public class BaseTestCase extends org.xbrlapi.utilities.BaseTestCase {

	protected DBConnection connection = null;
	protected Store store = null;
	protected Loader loader = null;
	
	protected String host;
	protected String port;
	protected String database;
	protected String storeParentPath;
	protected String dataCollectionName;
	protected String username;
	protected String password;
	
	protected String cache = null;
	
	protected final String configurationRoot = "/system/config";	

	protected List<Store> stores = new LinkedList<Store>();
	protected List<DBConnection> connections = new LinkedList<DBConnection>();

	protected void setUp() throws Exception {
		super.setUp();
		host = configuration.getProperty("exist.domain");
		port = configuration.getProperty("exist.port");
		database = configuration.getProperty("exist.database");
		storeParentPath = configuration.getProperty("exist.store.parent.path");			
		dataCollectionName = configuration.getProperty("exist.data.collection.name");
		username = configuration.getProperty("exist.username");
		password = configuration.getProperty("exist.password");

		cache = configuration.getProperty("local.cache");

		connection = createConnection();
		connections.add(connection);
		store = createStore(connection);
		stores.add(store);
		loader = createLoader(store);
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		for (int i=0; i<stores.size(); i++) {
			cleanup(connections.get(i),stores.get(i));
		}
	}	
	
	public BaseTestCase(String arg0) {
		super(arg0);		
	}
	
	/**
	 * Helper method to establish an Xindice database connection.
	 * Ensures that the containerCollection to be used is empty by 
	 * deleting and then recreating it if it already exists.
	 * @return a connection to the tests Xindice database.
	 */
	public  DBConnectionImpl createConnection() throws XBRLException {
		DBConnectionImpl connection = new DBConnectionImpl(host,port,database, username, password);
		return connection;
	}

	/**
	 * Helper method to create an Xindice store for XBRL data.
	 * @param connection the connection to the database that will hold the store
	 * @return a connection to the tests Xindice database.
	 */
	public  StoreImpl createStore(DBConnection connection) throws XBRLException {

		if (connection == null) {
			throw new XBRLException("The store cannot be created using a null database connection.");
		}

		if (! connection.hasCollection(storeParentPath)) {
			throw new XBRLException("The parent collection for the data store does not exist.");
		}
		//Collection parentCollection = connection.getCollection(storeParentPath);
	
		return new StoreImpl((DBConnectionImpl) connection, storeParentPath, dataCollectionName);

	}	

	public Loader createLoader(Store store) throws XBRLException {
		XBRLXLinkHandlerImpl xlinkHandler = new XBRLXLinkHandlerImpl();
		XBRLCustomLinkRecogniserImpl clr = new XBRLCustomLinkRecogniserImpl(); 
		XLinkProcessor xlinkProcessor = new XLinkProcessorImpl(xlinkHandler ,clr);
		EntityResolver entityResolver = new EntityResolverImpl(new File(cache));
		Loader myLoader = new LoaderImpl(store,xlinkProcessor);
		myLoader.setEntityResolver(entityResolver);
		xlinkHandler.setLoader(myLoader);
		return myLoader;
	}	
	
	/**
	 * Helper method to clean up and shut down an Xindice database connection.
	 * @param connection the connection to the Xindice database.
	 */
	public void cleanup(DBConnection connection) throws XBRLException {
		try {
			if (connection.hasCollection(storeParentPath + dataCollectionName)) {
				connection.deleteCollection(storeParentPath + dataCollectionName);
			}
			
			if (connection.hasCollection(configurationRoot + storeParentPath + dataCollectionName)) {
				connection.deleteCollection(configurationRoot + storeParentPath + dataCollectionName);
			}	
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
		if (connection != null)
			connection.close();
	}	
	
	/**
	 * Helper method to clean up and shut down an Xindice database connection and data store.
	 * @param connection the connection to the Xindice database.
	 * @param store the store for the XBRL data.
	 * @throws XBRLException if the store cannot be deleted 
	 */
	public void cleanup(DBConnection connection, Store store) throws XBRLException {

		if (connection == null) {
			throw new XBRLException("Trying to clean up a null database connection.");
		}
		
		if (store != null) {
			store.delete();
		}
		
		// Delete the indexes
		if (connection.hasCollection(configurationRoot + storeParentPath + dataCollectionName)){
			connection.deleteCollection(configurationRoot + storeParentPath + dataCollectionName);
		}

		cleanup(connection);
		
	}
}