package org.xbrlapi.loader.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Stub;
import org.xbrlapi.data.dom.tests.BaseTestCase;

/**
 * Test the loader handling of partial loading situations.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class PartialLoadingTestCase extends BaseTestCase {

	private final String VALID_URI = "test.data.small.schema";
    private final String NONEXISTENT_URI = "nonexistent.uri";

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public PartialLoadingTestCase(String arg0) {
		super(arg0);
	}

	public void testLoaderHandlingOfAnIOException() throws Exception {
	    try {
	        loader.stashURI(getURI(this.NONEXISTENT_URI));
	        loader.stashURI(getURI(this.VALID_URI));
            loader.discover();
            List<Stub> stubs = store.getStubs();
            assertEquals(1,stubs.size());
            List<URI> uris = store.getDocumentsToDiscover();
            assertEquals(1,uris.size());
            assertEquals(uris.get(0),getURI(this.NONEXISTENT_URI));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

    public void testLoaderHandlingOfASAXException() throws Exception {
        try {
            loader.discover(new URI("http://www.xbrlapi.org/invalid.xml"),"<xbrl>");
            loader.stashURI(getURI(this.VALID_URI));
            loader.discover();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
	

	


}
