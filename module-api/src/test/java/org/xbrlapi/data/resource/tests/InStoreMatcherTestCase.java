package org.xbrlapi.data.resource.tests;

import java.net.URI;

import org.xbrlapi.data.Store;
import org.xbrlapi.data.dom.StoreImpl;
import org.xbrlapi.data.resource.InStoreMatcherImpl;

/**
 * Provides a base test case for tests involving the XML DOM data store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class InStoreMatcherTestCase extends BaseTestCase {
    
    private Store store = null;

	protected void setUp() throws Exception {
		super.setUp();
		store = new StoreImpl();
        matcher = new InStoreMatcherImpl(store,cache);
	}
	
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public InStoreMatcherTestCase(String arg0) {
        super(arg0);
    }	
	
    public void testGetSignatureFromSmallFile() {
        try {
            URI uri = getURI("test.data.small.schema");
            logger.info(matcher.getSignature(uri));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }
    
    public void testGetSignatureFromLargeFile() {
        try {
            URI uri = getURI("real.data.sec");
            logger.info(matcher.getSignature(uri));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }    

    public void testRepeatedGetMatchFromLargeFile() {
        try {
            URI uri1 = getURI("real.data.sec.usgaap.1");
            assertEquals(uri1,matcher.getMatch(uri1));

            URI uri2 = getURI("real.data.sec.usgaap.2");
            assertEquals(uri1,matcher.getMatch(uri2));

            store.serialize(store.getStoreAsDOM().getDocumentElement());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }
    


	

}