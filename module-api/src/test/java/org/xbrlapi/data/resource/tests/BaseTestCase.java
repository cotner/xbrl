package org.xbrlapi.data.resource.tests;

import java.io.File;

import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.resource.Matcher;

/**
 * Provides a base test case for tests involving the XML DOM data store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public abstract class BaseTestCase extends org.xbrlapi.utilities.BaseTestCase {
    
    protected Matcher matcher = null;
    protected CacheImpl cache = null;
	protected String cacheLocation = configuration.getProperty("local.cache");

	protected void setUp() throws Exception {
		super.setUp();
		File cacheFile = new File(cacheLocation);
        cache = new CacheImpl(cacheFile);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public BaseTestCase(String arg0) {
		super(arg0);
	}

}