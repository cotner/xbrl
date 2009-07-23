package org.xbrlapi.data.resource.tests;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */

import java.io.File;
import java.net.URI;

import org.xbrlapi.cache.Cache;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.dom.StoreImpl;
import org.xbrlapi.data.resource.DefaultMatcherImpl;
import org.xbrlapi.data.resource.InMemoryMatcherImpl;
import org.xbrlapi.data.resource.InStoreMatcherImpl;
import org.xbrlapi.data.resource.Matcher;
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
		    Matcher matcher = new DefaultMatcherImpl();
            Object copy = getDeepCopy(matcher);
            this.assessCustomEquality(matcher,copy);
		} catch (Exception e) {
		    e.printStackTrace();
			fail("Unexpected exception. " + e.getMessage());
		}
	}
	
    public final void testInStoreMatcherSerialization() {
        try {
            Store store = new StoreImpl();
            Cache cache = new CacheImpl(new File(this.cachePath));
            Matcher matcher = new InStoreMatcherImpl(store, cache);
            doTest(matcher);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception. " + e.getMessage());
        }
    }
    
    private void doTest(Matcher matcher) {
        try {
            fillMatcher(matcher);
            assertTrue(matcher.hasURI(new URI("http://www.xbrlapi.org/1")));
            assertFalse(matcher.hasURI(new URI("http://www.xbrlapi.org/200")));
            Object copy = getDeepCopy(matcher);
            this.assessCustomEquality(matcher,copy);
            assertTrue(((Matcher) copy).hasURI(new URI("http://www.xbrlapi.org/1")));
            assertFalse(((Matcher) copy).hasURI(new URI("http://www.xbrlapi.org/200")));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
	
    public final void testInMemoryMatcherSerialization() {
        try {
            Cache cache = new CacheImpl(new File(this.cachePath));
            Matcher matcher = new InMemoryMatcherImpl(cache);
            doTest(matcher);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception. " + e.getMessage());
        }
    }
    
    private void fillMatcher(Matcher matcher) {
        try {
            for (int i=1; i<2; i++) {
                URI uri = new URI("http://www.xbrlapi.org/" + i);
                logger.info(uri);
                matcher.getMatch(uri);
            }        
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception. " + e.getMessage());
        }        
    }

	
}
