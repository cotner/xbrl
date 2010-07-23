package org.xbrlapi;

import java.io.File;

import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.dom.StoreImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xbrlapi.utilities.EntityResolver;
import org.xbrlapi.utilities.EntityResolverImpl;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;

/**
 * Provides a base test case for all tests involving
 * loading of data into a data store.  A DOM data store
 * is established and a loader is set up.  The loader
 * is made available via the getLoader() method.  This 
 * gives access to the data store in the usual manner.
 * 
 * Use for small scale loading operations because it 
 * relies on the DOM data store implementation.
 * 
 * Specific implementations then just need to provide the 
 * loader with the URIs to discover and then trigger the 
 * discovery process.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public abstract class DOMLoadingTestCase extends BaseTestCase {

	public DOMLoadingTestCase(String arg0) {
		super(arg0);
	}

	protected Store store = null;
	protected Loader loader = null;
	
	private XBRLXLinkHandlerImpl xlinkHandler = null;
	private XLinkProcessor xlinkProcessor = null;
	private EntityResolver entityResolver = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		store = new StoreImpl();
		xlinkHandler = new XBRLXLinkHandlerImpl();
		XBRLCustomLinkRecogniserImpl clr = new XBRLCustomLinkRecogniserImpl(); 
		xlinkProcessor = new XLinkProcessorImpl(xlinkHandler ,clr);
		File cacheFile = new File(configuration.getProperty("local.cache"));
		entityResolver = new EntityResolverImpl(cacheFile);
		loader = new LoaderImpl(store,xlinkProcessor);
		loader.setCache(new CacheImpl(cacheFile));
		loader.setEntityResolver(entityResolver);
		xlinkHandler.setLoader(loader);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
}
