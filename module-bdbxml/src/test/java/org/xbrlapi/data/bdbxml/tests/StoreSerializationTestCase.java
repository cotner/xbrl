package org.xbrlapi.data.bdbxml.tests;

import org.xbrlapi.data.Store;
import org.xbrlapi.data.bdbxml.BaseTestCase;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */
public class StoreSerializationTestCase extends BaseTestCase {
    
    private final String START = "test.data.small.schema";    
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public StoreSerializationTestCase(String arg0) {
		super(arg0);
	}
	
    public final void testStoreSerialization() {
        try {
            loader.discover(this.getURI(START));
            Store copy = (Store) getDeepCopy(store);
            stores.add(copy);
            this.assessCustomEquality(store,copy);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception. " + e.getMessage());
        }
    }	

}
