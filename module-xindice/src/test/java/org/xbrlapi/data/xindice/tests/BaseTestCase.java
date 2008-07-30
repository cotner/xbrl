package org.xbrlapi.data.xindice.tests;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.xbrlapi.SAXHandlers.EntityResolverImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.xindice.DBConnection;
import org.xbrlapi.data.xindice.DBConnectionImpl;
import org.xbrlapi.data.xindice.StoreImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;
import org.xml.sax.EntityResolver;

/**
 * Provides a base test case for tests using the Xindice data store.
 * The base test case provides for creation of the data store itself
 * and gives access to the store and the fragment loader for the store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public abstract class BaseTestCase extends org.xbrlapi.utilities.BaseTestCase {

	protected DBConnection connection = null;
	protected Store store = null;
	protected Loader loader = null;
	
	protected String host = null;
	protected String port = null;
	protected String database = null;	
	protected String storeParentPath = null;
	protected String dataCollectionName = null;

	protected String cache = null;
	
	protected List<Store> stores = new LinkedList<Store>();
	protected List<DBConnection> connections = new LinkedList<DBConnection>();
	
	protected void setUp() throws Exception {
		super.setUp();
		host = configuration.getProperty("xindice.domain");
		port = configuration.getProperty("xindice.port");
		database = configuration.getProperty("xindice.database");
		storeParentPath = configuration.getProperty("xindice.store.parent.path");			
		dataCollectionName = configuration.getProperty("xindice.data.collection.name");
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
		DBConnectionImpl connection = new DBConnectionImpl(host,port,database);
		return connection;
	}

	/**
	 * Helper method to create an Xindice store for XBRL data.
	 * @param connection the connection to the database that will hold the store
	 * @return a Xindice data store.
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

	/**
	 * @param store The store to use for the loader.
	 * @return the loader
	 * @throws XBRLException
	 */
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
		if (connection == null) 
			throw new XBRLException("Trying to clean up a null database connection.");

		if (store != null) {
			try {
				store.delete();			
			} catch (XBRLException e) {
				logger.debug("The store deletion failed.  Probably another store was open and using the same database.");
			}
		}
				
		cleanup(connection);
	}
}
