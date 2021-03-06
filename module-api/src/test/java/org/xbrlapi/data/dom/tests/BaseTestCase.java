package org.xbrlapi.data.dom.tests;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.xbrlapi.cache.Cache;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.dom.StoreImpl;
import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.loader.LoaderImpl;
import org.xbrlapi.sax.EntityResolver;
import org.xbrlapi.sax.EntityResolverImpl;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xbrlapi.xlink.XLinkProcessorImpl;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;

/**
 * Provides a base test case for tests 
 * using the in-memory XML DOM data store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public abstract class BaseTestCase extends org.xbrlapi.utilities.BaseTestCase {

	protected String cache = "";
    protected File cacheFile = null;
    protected Cache cacheImpl = null;
	protected Store store = null;
	protected Loader loader = null;
	
	protected List<Store> stores = new LinkedList<Store>();
	
	protected void setUp() throws Exception {
		super.setUp();
		cache = configuration.getProperty("local.cache");
		cacheFile = new File(cache);
		cacheImpl = new CacheImpl(cacheFile);
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

	/**
	 * @return the new store.
	 * @throws XBRLException
	 */
	public StoreImpl createStore() throws XBRLException {
	    StoreImpl store = new StoreImpl();
	    store.setMatcher(new InStoreMatcherImpl(store,cacheImpl));
	    return store;
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
		
		// Rivet errors in the SEC XBRL data require these remappings.
        HashMap<URI,URI> map = new HashMap<URI,URI>();
        try {
            map.put(new URI("http://www.xbrl.org/2003/linkbase/xbrl-instance-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/instance/xbrl-instance-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/linkbase/xbrl-linkbase-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xbrl-linkbase-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/instance/xbrl-linkbase-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xbrl-linkbase-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/instance/xl-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xl-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/linkbase/xl-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xl-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/instance/xlink-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xlink-2003-12-31.xsd"));
            map.put(new URI("http://www.xbrl.org/2003/linkbase/xlink-2003-12-31.xsd"),new URI("http://www.xbrl.org/2003/xlink-2003-12-31.xsd"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("A URI has invalid syntax.");
        }
        EntityResolver resolver = new EntityResolverImpl(cacheFile,map);
		Loader myLoader = new LoaderImpl(store,xlinkProcessor);
		myLoader.setCache(cacheImpl);
		myLoader.setEntityResolver(resolver);
		xlinkHandler.setLoader(myLoader);
		return myLoader;
	}
	
	/**
	 * Helper method to clean up and shut down the data store.
	 * @param store the store for the XBRL data.
	 * @throws XBRLException if the store cannot be deleted 
	 */
	public void cleanup(Store store) throws XBRLException {
		store.delete();
	}

}