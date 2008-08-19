package org.xbrlapi.xdt.tests;

import java.io.File;

import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.dom.StoreImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.xdt.LoaderImpl;
import org.xbrlapi.sax.EntityResolverImpl;
import org.xbrlapi.xdt.CustomLinkRecogniserImpl;
import org.xbrlapi.xdt.XLinkHandlerImpl;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xml.sax.EntityResolver;

/**
 * Provides a base test case for all XDT tests.
 *  
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public abstract class BaseTestCase extends org.xbrlapi.utilities.BaseTestCase {

	public BaseTestCase(String arg0) {
		super(arg0);
	}

	protected Store store = null;
	protected Loader loader = null;
	
	private XLinkHandlerImpl xlinkHandler = null;
	private XLinkProcessor xlinkProcessor = null;
	private EntityResolver entityResolver = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		store = new StoreImpl();
		xlinkHandler = new XLinkHandlerImpl();
		CustomLinkRecogniserImpl clr = new CustomLinkRecogniserImpl(); 
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
