package org.xbrlapi.cache.tests;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.xbrlapi.cache.Cache;
import org.xbrlapi.cache.CacheImpl;
import org.xbrlapi.utilities.BaseTestCase;


public class CacheImplSerializationTestCase extends BaseTestCase {

	private String cacheRoot;

	
	protected void setUp() throws Exception {
		super.setUp();
		cacheRoot = configuration.getProperty("local.cache");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * @param arg0
	 */
	public CacheImplSerializationTestCase(String arg0) {
		super(arg0);
	}

	public final void testSerialization() {
		try {
			Cache cache = new CacheImpl(new File(cacheRoot));

			ByteArrayOutputStream memoryOutputStream = new ByteArrayOutputStream( );
			ObjectOutputStream serializer = new ObjectOutputStream(memoryOutputStream);
			serializer.writeObject(cache);
			serializer.flush( );

			ByteArrayInputStream memoryInputStream = new ByteArrayInputStream(memoryOutputStream.toByteArray( ));
	        ObjectInputStream deserializer = new ObjectInputStream(memoryInputStream);
	        Object copy = deserializer.readObject( );
	        
	        assertEquals(cache,copy);
            assertEquals(cache.hashCode(),copy.hashCode());
			
		} catch (Exception e) {
		    e.printStackTrace();
			fail("Unexpected exception. " + e.getMessage());
		}
	}
	
}
