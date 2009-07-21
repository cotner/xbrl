package org.xbrlapi.data.resource.tests;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */

import java.io.File;

import org.xbrlapi.cache.Cache;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.dom.StoreImpl;
import org.xbrlapi.data.resource.DefaultMatcherImpl;
import org.xbrlapi.data.resource.InMemoryMatcherImpl;
import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.utilities.BaseTestCase;


public class MatcherSerializationTestCase extends BaseTestCase {
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public MatcherSerializationTestCase(String arg0) {
		super(arg0);
	}

	public final void testDefaultMatcherSerialization() {
		try {
		    Object object = new DefaultMatcherImpl();
		    Object copy = getDeepCopy(object);
		    this.assessCustomEquality(object,copy);
		} catch (Exception e) {
		    e.printStackTrace();
			fail("Unexpected exception. " + e.getMessage());
		}
	}
	
    public final void testInStoreMatcherSerialization() {
        try {
            Store store = new StoreImpl();
            Cache cache = new CacheImpl(new File(this.cachePath));
            Object object = new InStoreMatcherImpl(store, cache);
            Object copy = getDeepCopy(object);
            this.assessCustomEquality(object,copy);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception. " + e.getMessage());
        }
    }	
	
    public final void testInMemoryMatcherSerialization() {
        try {
            Cache cache = new CacheImpl(new File(this.cachePath));
            Object object = new InMemoryMatcherImpl(cache);
            Object copy = getDeepCopy(object);
            this.assessCustomEquality(object,copy);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception. " + e.getMessage());
        }
    }   

	
}
