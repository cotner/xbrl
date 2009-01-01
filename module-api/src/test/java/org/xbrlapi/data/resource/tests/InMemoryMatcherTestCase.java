package org.xbrlapi.data.resource.tests;

import java.net.URI;

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
            URI uri = new URI(this.getURI("test.data.small.schema"));
            logger.info(matcher.getSignature(uri));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }
    
    public void testGetSignatureFromLargeFile() {
        try {
            URI uri = new URI(this.getURI("real.data.sec"));
            logger.info(matcher.getSignature(uri));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }    

    public void testRepeatedGetMatchFromLargeFile() {
        try {
            String URI = "real.data.sec";
            URI uri = new URI(this.getURI(URI));
            assertEquals(uri,matcher.getMatch(uri));
            assertEquals(uri,matcher.getMatch(uri));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown.");
        }
    }
}