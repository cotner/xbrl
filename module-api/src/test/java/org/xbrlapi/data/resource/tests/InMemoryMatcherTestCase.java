package org.xbrlapi.data.resource.tests;

import java.net.URL;

import org.xbrlapi.data.resource.InMemoryMatcherImpl;

/**
 * Provides a base test case for tests involving the XML DOM data store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class InMemoryMatcherTestCase extends BaseTestCase {
    
	protected void setUp() throws Exception {
		super.setUp();
        matcher = new InMemoryMatcherImpl(cache);
	}
	
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public InMemoryMatcherTestCase(String arg0) {
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
            String URL = "real.data.sec";
            URL url = new URL(this.getURL(URL));
            assertEquals(url,matcher.getMatch(url));
            assertEquals(url,matcher.getMatch(url));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }
}