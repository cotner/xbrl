package org.xbrlapi.data.exist.tests;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.exist.StoreImpl;
import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.sax.EntityResolverImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;
import org.xbrlapi.sax.EntityResolver;

/**
 * Provides a base test case for tests involving the eXist database.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

abstract public class BaseTestCase extends org.xbrlapi.utilities.BaseTestCase {


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
		store = createStore();
		stores.add(store);
		loader = createLoader(store);
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		for (int i=0; i<stores.size(); i++) {
			cleanup(stores.get(i));
		}
	}	
	
	public BaseTestCase(String arg0) {
		super(arg0);		
	}
	
	public Store createStore() throws XBRLException {
	    Store store = new StoreImpl(host,port,database,username,password,storeParentPath,dataCollectionName);
	    store.setMatcher(new InStoreMatcherImpl(store,new CacheImpl(new File(cache))));
	    return store;
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
	 * Helper method to clean up and shut down a data store.
	 * @param connection the connection to the database.
	 * @param store the store for the XBRL data.
	 * @throws XBRLException if the store cannot be deleted.
	 */
	public void cleanup(Store store) throws XBRLException {

	    if (store != null) {
			store.delete();
		}
		
	}
}