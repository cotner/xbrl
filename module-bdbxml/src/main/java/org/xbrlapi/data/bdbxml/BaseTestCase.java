package org.xbrlapi.data.bdbxml;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
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
 * Provides a base test case for tests involving the BDB XML database.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public abstract class BaseTestCase extends org.xbrlapi.utilities.BaseTestCase {

    // Create the logger
    protected static Logger logger = Logger.getLogger(BaseTestCase.class);  

    protected String location = "";
	protected String containerName = "";
	protected String cache = "";
	
	protected Store store = null;
	protected Loader loader = null;

	protected List<Store> stores = new LinkedList<Store>();
	
	protected void setUp() throws Exception {
		super.setUp();
	    location = configuration.getProperty("bdbxml.store.location");
	    containerName = configuration.getProperty("bdbxml.container.name");
	    cache = configuration.getProperty("local.cache");
		store = createStore();
		stores.add(store);
		loader = createLoader(store);
	}

	protected void tearDown() throws Exception {
		super.tearDown();

        Set<Store> uniqueStores = new HashSet<Store>();
        uniqueStores.addAll(stores);
        logger.info("# unique stores = " + uniqueStores.size());
        logger.info("# stores = " + stores.size());
        
		STORE: for (Store s: stores) {
	        try {
	            for (Store us: uniqueStores) {
	                if (us == s) continue STORE;
	            }
                s.close();
                logger.info("closed a store.");
	        } catch (Exception e) {
	            ;
	        }
		}
		for (Store s: uniqueStores) {
		    s.delete();
		}
	}
	
	public BaseTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * @param myLocation The location to use for the new store.
	 * @param myContainerName The name to use for the XML container.
	 * @return the new store.
	 * @throws XBRLException
	 */
	public  StoreImpl createStore(String myLocation, String myContainerName) throws XBRLException {
		StoreImpl store = new StoreImpl(myLocation,myContainerName);
		store.setMatcher(new InStoreMatcherImpl(store,new CacheImpl(new File(cache))));
		return store;
	}
	
	/**
	 * @return the new store based on the default location and container name.
	 * @throws XBRLException
	 */
	public  StoreImpl createStore() throws XBRLException {
		return createStore(location, containerName);
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
		
        File cacheFile = new File(cache);
        
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
        EntityResolver entityResolver = new EntityResolverImpl(cacheFile,map);		
		
		Loader myLoader = new LoaderImpl(store,xlinkProcessor);
        myLoader.setCache(new CacheImpl(cacheFile));
		myLoader.setEntityResolver(entityResolver);
		xlinkHandler.setLoader(myLoader);
		return myLoader;
	}
	


}