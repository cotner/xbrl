package org.xbrlapi.data.resource.tests;

import java.net.URL;

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
            String URL = "test.data.small.schema";
            URL url = new URL(this.getURL(URL));
            logger.info(matcher.getSignature(url));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }
    
    public void testGetSignatureFromLargeFile() {
        try {
            String URL = "real.data.sec";
            URL url = new URL(this.getURL(URL));
            logger.info(matcher.getSignature(url));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }    

    public void testRepeatedGetMatchFromLargeFile() {
        try {
            URL url1 = new URL(this.getURL("real.data.sec.usgaap.1"));
            assertEquals(url1,matcher.getMatch(url1));

            URL url2 = new URL(this.getURL("real.data.sec.usgaap.2"));
            assertEquals(url1,matcher.getMatch(url2));

            store.serialize(store.getStoreAsDOM().getDocumentElement());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }
    


	

}